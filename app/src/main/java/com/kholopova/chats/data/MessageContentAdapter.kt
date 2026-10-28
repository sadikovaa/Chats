package com.kholopova.chats.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

internal class MessageContentAdapter : JsonDeserializer<MessageContent> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): MessageContent {
        if (!json.isJsonObject) return MessageContent.Text("")
        val payload = json.asJsonObject
        payload.getAsJsonObject("Text")?.let { text ->
            return MessageContent.Text(text.optString("text"))
        }
        payload.getAsJsonObject("Image")?.let { image ->
            return MessageContent.Image(image.optString("link"))
        }
        return MessageContent.Text("")
    }
}

private fun JsonObject.optString(name: String): String {
    val value = get(name) ?: return ""
    if (value.isJsonNull || !value.isJsonPrimitive) return ""
    return value.asString
}
