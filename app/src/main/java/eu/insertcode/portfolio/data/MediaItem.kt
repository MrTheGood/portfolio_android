package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by maarten on 2018-01-03.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
data class MediaItem(
        val image: String?,
        val youtubeVideo: String?
) : Serializable {
    companion object {
        fun builder(o: JSONObject) = MediaItem(null, o.getString("youtube"))
        fun builder(s: String) = MediaItem(s, null)
    }

    fun hasYoutubeVideo() = (youtubeVideo != null && !youtubeVideo.isEmpty())
    fun hasImage() = (image != null && !image.isEmpty())
}