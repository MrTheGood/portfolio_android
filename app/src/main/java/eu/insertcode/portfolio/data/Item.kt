@file:Suppress("unused", "LeakingThis")

package eu.insertcode.portfolio.data

import org.json.JSONObject

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
open class Item(o: JSONObject) {
    val title = o.getString("title")!!
}

data class Category(val o: JSONObject): Item(o) {
    val items = (0..o.length()).map {
        val item = o.getJSONArray("items").getJSONObject(it)
        when {
            item.getString("type") == "item" -> Project(o)
            item.getString("type") == "subcategory" -> Subcategory(o)
            else -> Item(o)
        }
    }
}


data class Subcategory(val o: JSONObject): Item(o) {
    val items = (0..o.length()).map {
        val item = o.getJSONArray("items").getJSONObject(it)
        when {
            item.getString("type") == "item" -> Project(o)
            item.getString("type") == "subcategory" -> Subcategory(o)
            else -> Item(o)
        }
    }
}

data class Project(val o: JSONObject): Item(o) {
    val img = o.getString("img")!!
    val shortDescription = o.getString("shortDescription")!!
    val fullDescription = o.getString("fullDescription")!!
    val date = o.getString("date")!!
}