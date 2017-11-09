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

data class ProjectItem(val o: JSONObject) : Item(o) {
    val images = try {
        (0 until o.optJSONArray("images").length()).map {
            o.getJSONArray("images").getString(it)
        }
    } catch (e: NullPointerException) {
        emptyList<String>()
    }
    val shortDescription = o.getString("shortDescription")!!
    val fullDescription = o.getString("fullDescription")!!
    val copyright: String? = o.optString("copyright", null)

    val tags = try {
        (0 until o.optJSONArray("tags").length()).map {
            o.getJSONArray("tags").getString(it)
        }
    } catch (e: NullPointerException) {
        emptyList<String>()
    }
    @Suppress("unused")
    val contributors = try { //TODO: Implement Contributors
        (0 until o.optJSONArray("contributors").length()).map {
            Contributor(o.getJSONArray("contributors").getJSONObject(it))
        }
    } catch (e: NullPointerException) {
        emptyList<String>()
    }

    val date: String? = o.optString("date", null)
}

data class Contributor(private val o: JSONObject) {
    //TODO: Implement Contributors
}