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

package eu.insertcode.portfolio.main.viewmodels

/**
 * Created by maartendegoede on 2019-09-22.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
data class TagViewState(
        private val text: String,

        val tagLabelText: String = text.toLowerCase(),
        val tagBackgroundColor: String = tagColorFromString(tagLabelText)
)


private val tags: ArrayList<String> = ArrayList()
private val tagColors = listOf(
        "673AB7", "43A047",
        "1E88E5", "26C6DA",
        "AB47BC", "E91E63",
        "F4511E", "FFA000",
        "4054B2"
)

private fun tagColorFromString(input: String): String {
    val tag = input.toLowerCase()
    if (!tags.contains(tag))
        tags.add(tag)

    return tagColors[tags.indexOf(tag) % tagColors.size]
}