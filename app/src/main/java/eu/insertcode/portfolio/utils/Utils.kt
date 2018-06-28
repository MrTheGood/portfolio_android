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

package eu.insertcode.portfolio.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by MrTheGood on 20/10/17.
 * Copyright © 2017 MrTheGood. All rights reserved.
 */
fun ImageView.loadImage(uri: String?) {
    if (uri != null) {
        Glide.with(this).asBitmap().load(uri).into(this)
    }
}

fun String.spanHtml(): Spanned =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }