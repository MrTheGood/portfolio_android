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

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 27/09/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
object TagColourHelper {
    private val tags: ArrayList<String> = ArrayList()
    private val tagColors = listOf(
            R.color.tagBackground1, R.color.tagBackground2,
            R.color.tagBackground3, R.color.tagBackground4,
            R.color.tagBackground5, R.color.tagBackground6,
            R.color.tagBackground7, R.color.tagBackground8,
            R.color.tagBackground9
    )

    @ColorRes
    private fun getTagColorRes(tag: String): Int {
        if (!tags.contains(tag)) {
            tags.add(tag)
        }
        val pos = tags.indexOf(tag) % tagColors.size
        return tagColors[pos]
    }

    fun getTagColorSL(tag: String, context: Context) =
            ContextCompat.getColorStateList(context, getTagColorRes(tag))

    fun getTagColor(tag: String, context: Context) =
            ContextCompat.getColor(context, getTagColorRes(tag))
}