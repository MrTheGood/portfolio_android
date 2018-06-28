/*
 *    Copyright 2018 Maarten de Goede
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.insertcode.portfolio.comms

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.annotation.StringRes
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.CategoryItem
import eu.insertcode.portfolio.view.activity.MainActivity
import eu.insertcode.portfolio.view.activity.SplashActivity
import eu.insertcode.portfolio.view.fragment.AboutFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by MrTheGood on 16/10/17.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
class GetDataAsync(private val context: WeakReference<SplashActivity>) : AsyncTask<String, Int, String>() {
    companion object {
        const val RETRY_DELAY_MS = 100L
        const val MAX_RETRIES = 3
    }

    private var retries = 0
    private var url = ""
    private var cachePath = ""

    /**
     *  Downloads and caches app content.
     *  If downloading fails, it retries 3 times.
     *  If it still fails, it loads a cache if any exists.
     *  If there isn't a cache, it warns the user.
     */
    override fun doInBackground(vararg params: String): String {
        try {
            cachePath = context.get()?.cacheDir?.absolutePath + "cache.txt"

            url = params[0]
            val conn = URL(url).openConnection() as HttpsURLConnection
            conn.requestMethod = "GET"
            conn.connect()

            val result = conn.inputStream.bufferedReader().use { it.readText() }
            saveCache(result)
            return result
        } catch (e: IOException) {
            e.printStackTrace()
            if (retries < MAX_RETRIES) {
                retries++
                Thread.sleep(RETRY_DELAY_MS)
                return doInBackground(*params)
            } else {
                @Suppress("LiftReturnOrAssignment")
                if (hasCache()) {
                    return getCache()
                } else {
                    val errorMsg = if (context.get()?.isNetworkConnected() == true) {
                        context.get()?.getString(R.string.error_unexpected)
                    } else {
                        context.get()?.getString(R.string.error_connectionLost)
                    }

                    return "{\"success\":\"false\", \"error\":\"$errorMsg\"}"
                }
            }
        }
    }

    private fun hasCache() = File(cachePath).exists()

    private fun getCache(): String {
        val inputStream = File(cachePath).inputStream()
        val text = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()

        return text
    }

    private fun saveCache(json: String) {
        val stream = FileOutputStream(File(cachePath))
        stream.write(json.toByteArray())
        stream.close()
    }

    override fun onPostExecute(result: String) {
        try {
            val json = JSONObject(result)

            val success = json.getBoolean("success")
            val error = json.optString("error", "null")
            val categories = json.optJSONArray("categories")
            val aboutMe = json.optString("about_me")

            if (!success) {
                showErrorDialog(R.string.error_title_general, error, true)
                return
            }

            MainActivity.categories = (0 until categories.length()).map {
                CategoryItem.builder(categories.getJSONObject(it))
            }
            AboutFragment.aboutMeText = aboutMe

            startProjectsActivity()
        } catch (e: JSONException) {
            showErrorDialog(R.string.error_server_title, R.string.error_server_msg, false)
        }
    }

    private fun showErrorDialog(@StringRes title: Int, @StringRes message: Int, retryable: Boolean) {
        val msg = context.get()?.getString(message)
        if (msg != null) {
            showErrorDialog(title, msg, retryable)
        }
    }

    private fun showErrorDialog(@StringRes title: Int, message: String, retryable: Boolean) {
        val ctx = context.get()
        if (ctx != null) {
            val dialog = AlertDialog.Builder(ctx)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert)

            if (retryable) {
                dialog.setPositiveButton(R.string.dialog_retry) { _, _ -> GetDataAsync(context).execute(url) }
                dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> ctx.finish() }
            } else {
                dialog.setPositiveButton(android.R.string.ok) { _, _ -> ctx.finish() }
                dialog.setCancelable(false)
            }

            dialog.show()
        }
    }

    private fun startProjectsActivity() {
        val ctx = context.get()
        if (ctx != null && !ctx.paused) {
            val intent = Intent(ctx, MainActivity::class.java)
            ctx.startActivity(intent)
            ctx.finish()
        }
    }
}