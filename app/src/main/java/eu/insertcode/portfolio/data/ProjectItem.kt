package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
data class ProjectItem(
        val title: String,
        val images: List<MediaItem>,
        val shortDescription: String,
        val fullDescription: String,
        val copyright: String,
        val layout: String,
        val tags: List<String>,
        val date: String?
) : Serializable {
    companion object {
        fun builder(o: JSONObject) = ProjectItem(
                o.optString("title", "")!!,
                try {
                    (0 until o.optJSONArray("images").length()).map {
                        val item = o.getJSONArray("images").get(it)
                        if (item is JSONObject) {
                            MediaItem.builder(item)
                        } else {
                            MediaItem.builder(item as String)
                        }
                    }
                } catch (e: NullPointerException) {
                    emptyList<MediaItem>()
                },
                o.getString("shortDescription")!!,
                o.getString("fullDescription")!!,
                o.getString("copyright")!!,
                o.optString("layout", "")!!,
                try {
                    (0 until o.optJSONArray("tags").length()).map {
                        o.getJSONArray("tags").getString(it)
                    }
                } catch (e: NullPointerException) {
                    emptyList<String>()
                },
                o.optString("date", null)
        )
    }
}