/*
 *    Copyright 2019 Maarten de Goede
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

import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.main.data.models.ProjectType

/**
 * Created by maartendegoede on 09/10/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
@BindingAdapter("visibleIf")
fun bindVisibleIf(view: View, condition: Boolean) {
    view.visibleIf(condition)
}


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
    } else {
        view.setImageResource(R.drawable.ic_error)
    }
}


@BindingAdapter("transitionName")
fun bindTransitionName(view: View, transitionName: String?) {
    ViewCompat.setTransitionName(view, transitionName)
}


@BindingAdapter("elevation")
fun bindElevation(view: View, elevation: Float) {
    ViewCompat.setElevation(view, elevation)
}

@BindingAdapter("typeIndicator")
fun bindTypeIndicator(view: ImageView, type: ProjectType?) {
    if (type != null) {
        @DrawableRes
        val icon =
                when (type) {
                    ProjectType.APP -> R.drawable.ic_type_app
                    ProjectType.GAME -> R.drawable.ic_type_game
                    ProjectType.WEB -> R.drawable.ic_type_web
                    ProjectType.WATCH -> R.drawable.ic_type_watch
                    ProjectType.OTHER -> R.drawable.ic_type_other
                }

        @ColorRes
        val color =
                when (type) {
                    ProjectType.APP -> R.color.indicatorType_app
                    ProjectType.GAME -> R.color.indicatorType_game
                    ProjectType.WEB -> R.color.indicatorType_web
                    ProjectType.WATCH -> R.color.indicatorType_watch
                    ProjectType.OTHER -> R.color.indicatorType_other
                }

        ViewCompat.setBackgroundTintList(view, view.getColorStateList(color))
        view.setImageResource(icon)
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("textFromHtml")
fun bindTextFromHtml(view: TextView, text: String?) {
    if (text == null) return

    view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(text)
    }
}


@BindingAdapter("tagBackgroundTint")
fun bindTagBackgroundTint(view: View, color: String?) {
    val color = color ?: return
    ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf("#$color".toColorInt()))
}


enum class WindowInset { Padding, Margin }

@BindingAdapter(
        "leftSystemWindowInsets",
        "rightSystemWindowInsets",
        "topSystemWindowInsets",
        "bottomSystemWindowInsets",
        requireAll = false
)
fun applySystemWindowInsets(
        view: View,
        insetLeft: WindowInset?,
        insetRight: WindowInset?,
        insetTop: WindowInset?,
        insetBottom: WindowInset?
) {
    view.doOnApplyWindowInsets { view, insets, padding, margin ->
        val left = insets.systemWindowInsetLeft
        val right = insets.systemWindowInsetRight
        val top = insets.systemWindowInsetTop
        val bottom = insets.systemWindowInsetBottom

        view.setPadding(
                padding.left + (left.takeIf { insetLeft == WindowInset.Padding } ?: 0),
                padding.top + (top.takeIf { insetTop == WindowInset.Padding } ?: 0),
                padding.right + (right.takeIf { insetRight == WindowInset.Padding } ?: 0),
                padding.bottom + (bottom.takeIf { insetBottom == WindowInset.Padding } ?: 0)
        )

        (view.layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(
                margin.left + (left.takeIf { insetLeft == WindowInset.Margin } ?: 0),
                margin.top + (top.takeIf { insetTop == WindowInset.Margin } ?: 0),
                margin.right + (right.takeIf { insetRight == WindowInset.Margin } ?: 0),
                margin.bottom + (bottom.takeIf { insetBottom == WindowInset.Margin } ?: 0)
        )
    }
}