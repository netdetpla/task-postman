package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object UrlCrawlPost : BasePost() {

    private val tag = Settings.setting["urlCrawl"] as String
    private val limit = Settings.setting["urlCrawlLimit"] as String

    override fun post() {
        val urls = DatabaseHandler.getUrlByUrlFlag(Integer.parseInt(limit)).joinToString(",")
        post(
                postUrl,
                "url-crawl",
                tag,
                taskName,
                priority,
                urls,
                ""
        )
    }
}