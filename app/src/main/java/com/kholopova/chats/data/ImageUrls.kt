package com.kholopova.chats.data

import android.net.Uri

object ImageUrls {
    private const val ORIGIN = "https://faerytea.name"

    fun thumbnail(link: String): String = url("thumb", link)

    fun fullscreen(link: String): String = url("img", link)

    private fun url(folder: String, link: String): String {
        val encoded = link.trim().trimStart('/').split("/").joinToString("/") { Uri.encode(it) }
        return "$ORIGIN/$folder/$encoded"
    }
}
