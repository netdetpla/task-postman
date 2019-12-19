package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object PortScanPost : BasePost() {

    private val tag = Settings.setting["portScan"] as String
    private val limit = Integer.parseInt(Settings.setting["portScanLimit"] as String)
    private val ports = Settings.setting["portScanPort"] as String

    override fun post() {
        val ips = DatabaseHandler.getIPByPortScanFlag(limit).joinToString(",")
        post(
                postUrl,
                "port-scan",
                tag,
                taskName,
                priority,
                ips,
                ports
        )
    }
}