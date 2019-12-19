package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object PageCrawlPost : BasePost() {

    private val tag = Settings.setting["pageCrawl"] as String
    private val limit = Integer.parseInt(Settings.setting["pageCrawlLimit"] as String)

    override fun post() {
        val urls = DatabaseHandler.getUrlByPageFlag(limit).joinToString(",")
        post(
                postUrl,
                "page-crawl",
                tag,
                taskName,
                priority,
                urls,
                ""
        )
    }
}