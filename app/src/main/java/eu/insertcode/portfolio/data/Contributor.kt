package eu.insertcode.portfolio.data

import org.json.JSONObject
import java.io.Serializable

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
data class Contributor(val title: String) : Serializable {
    companion object {
        fun builder(o: JSONObject) = Contributor(o.optString("title", "")!!)
    }
    //TODO: Implement Contributors
}