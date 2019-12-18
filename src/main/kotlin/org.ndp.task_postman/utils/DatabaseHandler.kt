package org.ndp.task_postman.utils

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import org.ndp.task_postman.beans.Block
import org.ndp.task_postman.beans.Location

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
}