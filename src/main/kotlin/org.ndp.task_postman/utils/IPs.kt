package org.ndp.task_postman.utils

import java.util.*

object IPs {
    fun iNetString2Number(ipStr: String): Long {
        return Arrays.stream(ipStr.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                .map { java.lang.Long.parseLong(it) }
                .reduce(0L) { x, y -> (x!! shl 8) + y!! }
    }

    fun parseIPEnd(ipStart: Long, mask: Int): Long {
        val endMask = ((1 shl 32 - mask) - 1).toLong()
        return ipStart or endMask
    }

    fun iNetNumber2String(ipLong: Long): String {
        var origin = ipLong
        val segments = ArrayList<String>()
        for (i in 0..3) {
            segments.add((origin % 256L).toString())
            origin /= 256
        }
        segments.reverse()
        return segments.joinToString(".")
    }
}