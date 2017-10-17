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

data class CategoryItem(val o: JSONObject) : Item(o) {
    val items = (0 until o.getJSONArray("items").length()).map {
        val item = o.getJSONArray("items").getJSONObject(it)
        when {
            item.getString("type") == "item" -> ProjectItem(item)
            item.getString("type") == "subcategory" -> SubcategoryItem(item)
            else -> Item(o)
        }
    }
}


data class SubcategoryItem(val o: JSONObject) : Item(o) {
    val items =
            (0 until o.getJSONArray("items").length()).map {
                val item = o.getJSONArray("items").getJSONObject(it)
                when {
                    item.getString("type") == "item" -> ProjectItem(item)
                    item.getString("type") == "subcategory" -> SubcategoryItem(item)
                    else -> Item(item)
                }
            }
}

data class ProjectItem(val o: JSONObject) : Item(o) {
    val img = o.getString("img")!!
    val description = o.getString(("description"))!!
    //TODO: Change
//    val shortDescription = o.getString("shortDescription")!!
//    val fullDescription = o.getString("fullDescription")!!
//    val date = o.getString("date")!!
}