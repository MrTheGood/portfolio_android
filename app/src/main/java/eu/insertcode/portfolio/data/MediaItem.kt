package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by maarten on 2018-01-03.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
data class MediaItem(
        val image: String,
        val video: String?
) : Serializable {
    companion object {
        fun builder(o: JSONObject) = MediaItem(o.getString("image"), o.optString("video"))
        fun builder(s: String) = MediaItem(s, null)
    }

    fun hasVideo() = (video != null && !video.isEmpty())
}