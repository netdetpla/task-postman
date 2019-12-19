package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object IPTestPost2 : BasePost() {

    private val tag = Settings.setting["ipTest"] as String
    private val limit = Integer.parseInt(Settings.setting["ipTestLimit"] as String)

    override fun post() {
        val targets = DatabaseHandler.getIPByDomainIP(limit)
        for (t in targets) {
            post(
                    postUrl,
                    "ip-test",
                    tag,
                    taskName,
                    priority,
                    t
            )
        }
    }
}