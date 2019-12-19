package org.ndp.task_postman.posts

import org.apache.http.Consts
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.ndp.task_postman.utils.Settings
import java.lang.Exception
import java.net.URI
import java.nio.charset.StandardCharsets

abstract class BasePost {
    protected val postUrl = Settings.setting["serverUrl"] as String
    protected val taskName = Settings.setting["defaultTaskName"] as String
    protected val priority = Settings.setting["defaultPriority"] as String

    abstract fun post()

    protected fun post(
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
}