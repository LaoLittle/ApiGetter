package org.laolittle.plugin

import io.ktor.client.call.*
import io.ktor.client.request.*
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


internal fun getUTC8Date(): Date {
    return Calendar.getInstance(TimeZone.getTimeZone("UTC+8")).time
}

internal fun sanitizeDate(date: String?): String {
    if (date == null) return SimpleDateFormat("yyyyMMdd").format(getUTC8Date())
    SimpleDateFormat("yyyyMMdd").parse(date)
    return date
}

internal fun isSunday(date: String): Boolean {
    Calendar.getInstance().apply {
        time = SimpleDateFormat("yyyyMMdd").parse(date)
        return get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }
}


private val cacheFolder = ApiGetter.dataFolder.resolve("cache").also(File::mkdirs)
internal suspend fun downloadLoafersCalender(date: String? = null): InputStream? {
    val target = sanitizeDate(date)
    val file = cacheFolder.resolve("${target}.png")
    if (file.exists()) return file.inputStream()
    val response = httpClient.get("https://api.j4u.ink/proxy/redirect/moyu/calendar/${target}.png")
    if (!isSunday(target) && response.headers["etag"] == "\"6251bbbb-d2781\"")
        return null
    val body: ByteArray = response.body()
    file.writeBytes(body)
    return body.inputStream()
}