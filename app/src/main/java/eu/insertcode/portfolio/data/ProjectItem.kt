package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by MrTheGood on 16/10/17.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
data class ProjectItem(
        val title: String,
        val images: List<MediaItem>,
        val shortDescription: String,
        val fullDescription: String,
        val tags: List<String>,
        val date: String?
) : Serializable {
    companion object {
        fun builder(o: JSONObject) = ProjectItem(
                o.getString("title"),
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
                o.getString("shortDescription"),
                o.getString("fullDescription"),
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