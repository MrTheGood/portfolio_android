package eu.insertcode.portfolio.comms

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.annotation.StringRes
import eu.insertcode.portfolio.MainActivity
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.SplashActivity
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.data.Item
import eu.insertcode.portfolio.data.ProjectItem
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by maartendegoede on 16/10/17.
 * Copyright © 2017 insertCode.eu. All rights reserved.
 */
class GetDataAsync(private val context: WeakReference<SplashActivity>?) : AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val inputStream: InputStream? = null
        return try {
            val url = URL(params[0])
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            conn.connect()

            conn.inputStream.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    override fun onPostExecute(result: String?) {
        try {
            val json = JSONArray(result)

            MainActivity.items = (0 until json.length()).map {
                val o = json.getJSONObject(it)
                when (o.getString("type")) {
                    "item" -> ProjectItem(o)
                    "category" -> CategoryItem(o)
                    else -> Item(o)
                }
            }
            startProjectsActivity()
        } catch (e: JSONException) {
            showErrorDialog(R.string.error_server_title, R.string.error_server_msg, { _, _ -> throw e })
            return
        } catch (e: NullPointerException) {
            val ctx = context?.get()
            if (ctx != null) {
                if (ctx.isNetworkConnected()) {
                    showErrorDialog(R.string.error_unexpected_title, R.string.error_unexpected_msg, { _, _ -> throw e })
                } else {
                    showErrorDialog(R.string.error_connectionLost_title, R.string.error_connectionLost_msg, { _, _ -> ctx.finish() })
                }
            }
            return
        }
    }

    private fun showErrorDialog(@StringRes title: Int, @StringRes message: Int, listener: (Any, Any) -> Unit) {
        val ctx = context?.get()
        if (ctx != null) {
            AlertDialog.Builder(ctx)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show()
        }
    }

    private fun startProjectsActivity() {
        val ctx = context?.get()
        if (ctx != null) {
            val intent = Intent(ctx, MainActivity::class.java)
            ctx.startActivity(intent)
            ctx.finish()
        }
    }
}