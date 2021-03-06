package org.ndp.task_postman.posts

import org.ndp.task_postman.utils.DatabaseHandler
import org.ndp.task_postman.utils.Settings

object IPTestPost : BasePost() {

    private val tag = Settings.setting["ipTest"] as String
    private val limit = Integer.parseInt(Settings.setting["ipTestLimit"] as String)
    private val count = Integer.parseInt(Settings.setting["ipCount"] as String)
    private val country = Settings.setting["ipCountry"] as String

    override fun post() {
        val targets = DatabaseHandler.getIPByCountry(country, count, limit)
        Settings.setting["ipCount"] = count + limit
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