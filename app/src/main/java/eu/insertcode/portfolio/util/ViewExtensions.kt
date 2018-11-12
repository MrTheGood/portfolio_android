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

package eu.insertcode.portfolio.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 21/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */

val View.orientation get() = resources.configuration.orientation

fun View.isLandscapeOrientation() = orientation == Configuration.ORIENTATION_LANDSCAPE
fun View.isPortraitOrientation() = orientation == Configuration.ORIENTATION_PORTRAIT

fun View.visibleIf(condition: Boolean, alternative: Int = View.GONE) {
    visibility = if (condition) View.VISIBLE else alternative
}

fun View.goneIf(condition: Boolean) =
        visibleIf(!condition, View.GONE)

fun View.invisibleIf(condition: Boolean) =
        visibleIf(!condition, View.INVISIBLE)

fun View.getColorStateList(@ColorRes colorRes: Int) =
        ContextCompat.getColorStateList(context, colorRes)


fun Context.isNetworkAvailable() =
        (getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected == true

fun Fragment.startTextShareIntent(text: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)))
}

fun Fragment.startOpenUrlIntent(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}