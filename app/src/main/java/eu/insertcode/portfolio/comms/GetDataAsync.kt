package eu.insertcode.portfolio.comms

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import eu.insertcode.portfolio.data.Category
import eu.insertcode.portfolio.data.Item
import eu.insertcode.portfolio.data.Project
import eu.insertcode.portfolio.data.Subcategory
import eu.insertcode.portfolio.items
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 AppStudio.nl. All rights reserved.
 */
class GetDataAsync(val context: Context) : AsyncTask<String, Int, String>() {
    var success = false
    val progressDialog = ProgressDialog(context)

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.setMessage("Connecting.. Please wait..")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

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
        try {
            progressDialog.hide()
            val json = try {
                JSONArray(result)
            } catch (e: JSONException) {
                success = false
                return
            }

            items = (0 until json.length()).map {
                val o = json.getJSONObject(it)
                when (o.getString("type")) {
                    "item" -> Project(o)
                    "subcategory" -> Subcategory(o)
                    "category" -> Category(o)
                    else -> Item(o)
                }
            }
            success = true
        } catch (e: JSONException) {
            success = false
            Toast.makeText(context, "Oops, something went wrong. Please try again later.", Toast.LENGTH_LONG).show()
            throw e
        }
    }

}