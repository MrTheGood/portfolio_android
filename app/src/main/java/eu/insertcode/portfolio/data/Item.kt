package eu.insertcode.portfolio.data

import org.json.JSONObject

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
open class Item(o: JSONObject) {
    val title = o.getString("title")!!
}

data class CategoryItem(private val o: JSONObject) : Item(o) {
    val items = (0 until o.getJSONArray("items").length()).map {
        val item = o.getJSONArray("items").getJSONObject(it)
        when {
            item.getString("type") == "project" -> ProjectItem(item)
            else -> Item(o)
        }
    }
}
data class ProjectItem(private val o: JSONObject) : Item(o) {
    val img: String? = o.optString("img", null)//TODO: Make list
    val shortDescription = o.getString("shortDescription")!!
    val fullDescription = o.getString("fullDescription")!!
    val copyright: String? = o.optString("copyright", null)
    val tags = (0 until o.getJSONArray("tags").length()).map {
        o.getJSONArray("tags").getString(it)
    }
    val team = (0 until o.getJSONArray("team").length()).map {
        Contributor(o.getJSONArray("team").getJSONObject(it))
    }
    val date: String? = o.optString("date", null)
}

data class Contributor(private val o: JSONObject) {
    //TODO
}