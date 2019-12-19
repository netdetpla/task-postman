package org.ndp.task_postman.utils

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.schema.Column
import org.ndp.task_postman.beans.*

object DatabaseHandler {
    private val dbUrl = Settings.setting["dbUrl"] as String
    private val dbDriver = Settings.setting["dbDriver"] as String
    private val dbUser = Settings.setting["dbUser"] as String
    private val dbPassword = Settings.setting["dbPassword"] as String

    init {
        Database.Companion.connect(
                dbUrl,
                dbDriver,
                dbUser,
                dbPassword
        )
    }

    fun getIPByCountry(country: String, offset: Int, limit: Int): List<String> {
        return Block
                .select(Block.network, Block.geoNameID)
                .where {
                    Block.geoNameID inList
                            Location.select(Location.geoNameID).where { Location.countryISOCode eq country }
                }
                .limit(offset, limit)
                .map {
                    it[Block.network]!!
                }
                .toCollection(ArrayList())
    }

    fun getIPByPortScanFlag(limit: Int): List<String> {
        val intIPSet = ArrayList<Int>()
        val strIPSet = ArrayList<String>()
        IP.select(IP.id, IP.ip)
                .where { IP.portScanFlag eq 0 }
                .limit(0, limit)
                .forEach {
                    intIPSet.add(it[IP.id]!!)
                    strIPSet.add(it[IP.ip]!!)
                }
        IP.batchUpdate {
            for (i in intIPSet) {
                item {
                    it.portScanFlag to 1
                    where { it.id eq i }
                }
            }
        }
        return strIPSet
    }

    fun getIPByDomainIP(limit: Int): List<String> {
        val id = ArrayList<Int>()
        val ips = ArrayList<String>()
        DomainIP.select(DomainIP.id, DomainIP.ipID)
                .where { DomainIP.ipTestFlag eq 0 }
                .limit(0, limit)
                .onEach { id.add(it[DomainIP.id]!!) }
                .map { IPs.iNetNumber2String(it[DomainIP.ipID]!!) + "/24" }
                .forEach { ips.add(it) }
        DomainIP.batchUpdate {
            for (i in id) {
                item {
                    DomainIP.ipTestFlag to 1
                    where { it.id eq i }
                }
            }
        }
        return ips
    }

    private fun getUrl(flag: Column<Int>, limit: Int): List<String> {
        val urlID = ArrayList<Int>()
        val urls = ArrayList<String>()
        Page.select(Page.id, Page.domain)
                .where { flag eq 0 }
                .limit(0, limit)
                .forEach {
                    urlID.add(it[Page.id]!!)
                    urls.add(it[Page.domain]!!)
                }
        Page.batchUpdate {
            for (i in urlID) {
                item {
                    flag to 1
                    where { it.id eq i }
                }
            }
        }
        return urls
    }

    fun getUrlByUrlFlag(limit: Int): List<String> {
        return getUrl(Page.urlFlag, limit)
    }

    fun getUrlByPageFlag(limit: Int): List<String> {
        return getUrl(Page.pageFlag, limit)
    }

    fun getUrlByDnssecureFlag(limit: Int): List<String> {
        return getUrl(Page.dnssecureFlag, limit)
    }
}