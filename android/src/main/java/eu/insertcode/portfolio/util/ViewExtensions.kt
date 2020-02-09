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

import android.app.Activity
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 21/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
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

fun Context.startTextShareIntent(text: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)))
}

fun Fragment.startTextShareIntent(text: String) =
        context?.startTextShareIntent(text)

fun View.startTextShareIntent(text: String) =
        context?.startTextShareIntent(text)


fun ViewPager.addSimpleOnPageChangeListener(
        onPageSelected: (position: Int) -> Unit = {},
        onPageScrollStateChanged: (state: Int) -> Unit = {},
        onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> }
) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) =
                onPageSelected(position)

        override fun onPageScrollStateChanged(state: Int) =
                onPageScrollStateChanged(state)

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) =
                onPageScrolled(position, positionOffset, positionOffsetPixels)
    })
}

fun ViewPager.addOnPageSelectedListener(onPageSelected: (position: Int) -> Unit) =
        addSimpleOnPageChangeListener(onPageSelected = onPageSelected)

fun ViewPager.addOnPageScrollStateChangedListener(onPageScrollStateChanged: (state: Int) -> Unit) =
        addSimpleOnPageChangeListener(onPageScrollStateChanged = onPageScrollStateChanged)

fun ViewPager.addOnPageScrolledListener(addOnPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) =
        addSimpleOnPageChangeListener(onPageScrolled = addOnPageScrolled)


// openInBrowser
fun Activity.startUrlIntent(url: String) {
    val uri = Uri.parse(url)
    Intent(Intent.ACTION_VIEW, uri).apply {
        resolveActivity(packageManager)?.let { startActivity(this) }
            ?: run { openInBrowser(uri)}
    }
}

fun Activity.openInBrowser(uri: Uri) {
    setDeepLinkingState(PackageManager.COMPONENT_ENABLED_STATE_DISABLED)

    CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setInstantAppsEnabled(false)
            .build()
            .launchUrl(this, uri)

    setDeepLinkingState(PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
}

private fun Activity.setDeepLinkingState(state: Int) {
    applicationContext.packageManager.setComponentEnabledSetting(
            ComponentName(packageName, "$packageName.SplashActivity"),
            state,
            PackageManager.DONT_KILL_APP
    )
}


// doOnApplyWindowInsets
// source: https://chris.banes.dev/2019/04/12/insets-listeners-to-layouts/

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding, InitialMargin) -> Unit) {
    if (Build.VERSION.SDK_INT < 20) return

    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, WindowInsetsCompat.toWindowInsetsCompat(insets), initialPadding, initialMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

data class InitialMargin(val left: Int, val top: Int,
                         val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
        view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun recordInitialMarginForView(view: View) = InitialMargin(
        view.marginLeft, view.marginTop, view.marginRight, view.marginBottom)


fun View.requestApplyInsetsWhenAttached() {
    if (Build.VERSION.SDK_INT < 20) return

    if (isAttachedToWindow)
        requestApplyInsets()
    else addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            v.removeOnAttachStateChangeListener(this)

            if (Build.VERSION.SDK_INT < 20) return
            v.requestApplyInsets()
        }

        override fun onViewDetachedFromWindow(v: View) = Unit
    })
}