package org.ndp.task_postman

import org.ndp.task_postman.posts.*


fun main(args: Array<String>) {
    when (args[0]) {
        "ip-test" -> IPTestPost.post()
        "ip-test2" -> IPTestPost2.post()
        "port-scan" -> PortScanPost.post()
        "page-crawl" -> PageCrawlPost.post()
        "url-crawl" -> UrlCrawlPost.post()
        "dnssecure" -> DnssecurePost.post()
        else -> println("nothing to do")
    }
}