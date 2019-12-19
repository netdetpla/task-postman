package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object DnssecurePost : BasePost() {

    private val tag = Settings.setting["dnssecure"] as String
    private val limit = Integer.parseInt(Settings.setting["dnssecureLimit"] as String)
    private val dnsServer = Settings.setting["dnsServer"] as String

    override fun post() {
        val urls = DatabaseHandler.getUrlByDnssecureFlag(limit)
        post(
                postUrl,
                "dnssecure",
                tag,
                taskName,
                priority,
                urls.joinToString("+"),
                dnsServer
        )
    }
}