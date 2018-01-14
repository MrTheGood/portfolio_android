package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
data class CategoryItem(
        val title: String,
        val projects: List<ProjectItem>,
        val icon: String
) : Serializable {
    companion object {
        fun builder(o: JSONObject) = CategoryItem(
                o.getString("title"),
                (0 until o.getJSONArray("items").length()).map {
                    ProjectItem.builder(o.getJSONArray("items").getJSONObject(it))
                },
                o.getString("icon")
        )
    }
}