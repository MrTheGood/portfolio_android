@file:Suppress("DEPRECATION")

package eu.insertcode.portfolio.comms

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import eu.insertcode.portfolio.MainActivity
import eu.insertcode.portfolio.ProjectsActivity
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.Item
import eu.insertcode.portfolio.data.ProjectItem
import eu.insertcode.portfolio.data.SubcategoryItem
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class GetDataAsync(private val context: MainActivity) : AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val inputStream: InputStream? = null
        return try {
            val url = URL(params[0])
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            conn.connect()

            conn.inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            null
        } finally {
            inputStream?.close()
        }
    }

    override fun onPostExecute(result: String?) {
        var success = false
        try {
            val json = try {
                JSONArray(result)
            } catch (e: JSONException) {
                startProjectsActivity(success)
                return
            }

            ProjectsActivity.items = (0 until json.length()).map {
                val o = json.getJSONObject(it)
                when (o.getString("type")) {
                    "item" -> ProjectItem(o)
                    "subcategory" -> SubcategoryItem(o)
                    "category" -> CategoryItem(o)
                    else -> Item(o)
                }
            }
            success = true
        } catch (e: JSONException) {
            AlertDialog.Builder(context)
                    .setTitle(R.string.error_somethingWrong_title)
                    .setMessage(R.string.error_somethingWrong_msg)
                    .setPositiveButton(android.R.string.ok) { _, _ -> throw e }
                    .show()
            return
        }

        startProjectsActivity(success)
    }

    private fun startProjectsActivity(success: Boolean) {
        val intent = Intent(context, ProjectsActivity::class.java)
        intent.putExtra("success", success)
        context.startActivity(intent)
    }

}