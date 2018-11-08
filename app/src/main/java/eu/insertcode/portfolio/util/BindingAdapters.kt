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

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.model.Project

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

@BindingAdapter("typeIndicator")
fun bindTypeIndicator(view: ImageView, type: Project.Type?) {
    if (type != null) {
        view.backgroundTintList = view.getColorStateList(type.color)
        view.setImageResource(type.icon)
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

@BindingAdapter("isRefreshing")
fun bindIsRefreshing(view: SwipeRefreshLayout, boolean: Boolean) {
    view.isRefreshing = boolean
}

@BindingAdapter("onRefresh")
fun bindOnRefresh(view: SwipeRefreshLayout, listener: OnRefreshListener) {
    view.setOnRefreshListener(listener)
}