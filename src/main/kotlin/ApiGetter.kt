package org.laolittle.plugin

import io.ktor.client.request.*
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.error
import net.mamoe.mirai.utils.info

object ApiGetter : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.ApiGetter",
        name = "ApiGetter",
        version = "1.0-SNAPSHOT",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }

        globalEventChannel().subscribeGroupMessages {
            finding(Regex("[?？](.+)")) {
                when (it.groupValues[1]) {
                    "bird", "鸟" -> {
                        val jsonStr = client.get<String>("https://v2.yiff.rest/animals/birb")
                        Regex("\"url\":\"(.*?)\"").find(jsonStr)?.apply {
                            groups[1]?.let { result ->
                                logger.info { "url: ${result.value}" }
                                client.get<ByteArray>(result.value).toExternalResource().use { image ->
                                    subject.sendImage(image)
                                }
                            }
                        } ?: logger.error { "无法解析服务器返回的数据: $jsonStr" }
                    }
                    "cat", "猫" -> {
                        val jsonStr = client.get<String>("https://api.thecatapi.com/v1/images/search")
                        Regex("\"url\":\"(.*?)\"").find(jsonStr)?.apply {
                            groups[1]?.let { result ->
                                logger.info { "url: ${result.value}" }
                                client.get<ByteArray>(result.value).toExternalResource().use { image ->
                                    subject.sendImage(image)
                                }
                            }
                        } ?: logger.error { "无法解析服务器返回的数据: $jsonStr" }
                    }
                }
            }
        }
    }
}