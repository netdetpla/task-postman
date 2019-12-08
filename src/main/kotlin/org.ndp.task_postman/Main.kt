package org.ndp.task_postman

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import org.apache.http.Consts
import org.ndp.task_postman.beans.Block
import org.ndp.task_postman.beans.Location
import java.io.File
import java.util.*
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.ndp.task_postman.beans.IP
import java.lang.Exception
import java.net.URI
import java.nio.charset.StandardCharsets
import kotlin.collections.ArrayList


fun postTask(
        url: String,
        imageName: String,
        tag: String,
        taskName: String,
        priority: String,
        vararg ps: String
) {
    val params = ArrayList<NameValuePair>()
    params.add(BasicNameValuePair("image-name", imageName))
    params.add(BasicNameValuePair("tag", tag))
    params.add(BasicNameValuePair("task-name", taskName))
    params.add(BasicNameValuePair("priority", priority))
    for (p in ps) {
        params.add(BasicNameValuePair("params[]", p))
    }
    val httpPost = HttpPost()
    httpPost.uri = URI.create(url)
    httpPost.entity = UrlEncodedFormEntity(params, Consts.UTF_8)
    val httpClient = HttpClients.createMinimal()
    try {
        val response = httpClient.execute(httpPost)
        val entity = response.entity
        val result = EntityUtils.toString(entity, StandardCharsets.UTF_8)
        println(result)
    } catch (e: Exception) {

    }
}

fun postIPTestTask(limit: Int) {
    Database.Companion.connect(
            "jdbc:mysql://192.168.118.120:3306/ndp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
            "com.mysql.jdbc.Driver",
            "root",
            "password"
    )
    val save = File("./config.txt")
    val offset = save.readText().toInt()
//    val limit = args[0].toInt()
    val target = Block
            .select(Block.network, Block.geoNameID)
            .where {
                Block.geoNameID inList
                        Location.select(Location.geoNameID).where { Location.countryISOCode eq "CN" }
            }
            .limit(offset, limit)
    save.writeText((offset + limit).toString())
    for (t in target) {
        val iNet4 = t[Block.network]!!
        postTask(
                "http://192.168.118.120:8080/task",
                "ip-test",
                "1.02",
                "test$iNet4",
                "5",
                iNet4
        )
    }
}

fun postPortScanTask(limit: Int, ports: String) {
    Database.Companion.connect(
            "jdbc:mysql://192.168.118.120:3306/ndp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
            "com.mysql.jdbc.Driver",
            "root",
            "password"
    )
    val target = IP
            .select(IP.id, IP.ip)
            .where { IP.portScanFlag eq 0 }
            .limit(0, limit)
    val intIPSet = ArrayList<Int>()
    val strIPSet = ArrayList<String>()
    for (t in target) {
        intIPSet.add(t[IP.id]!!)
        strIPSet.add(t[IP.ip]!!)
    }
    val ips = strIPSet.joinToString(",")
    postTask(
            "http://192.168.118.120:8080/task",
            "port-scan",
            "1.01",
            "test",
            "5",
            ips,
            ports
    )
    IP.batchUpdate {
                for (i in intIPSet) {
                    item {
                        it.portScanFlag to 1
                        where { it.id eq i }
                    }
                }
            }
}

fun main(args: Array<String>) {
    when (args[0]) {
        "ip-test" -> postIPTestTask(Integer.parseInt(args[1]))
        "port-scan" -> postPortScanTask(Integer.parseInt(args[1]), args[2])
        else -> println("nothing to do")
    }
}