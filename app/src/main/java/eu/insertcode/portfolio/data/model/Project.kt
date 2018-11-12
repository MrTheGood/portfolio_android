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

package eu.insertcode.portfolio.data.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 23/08/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
data class Project(
        val id: String? = null,
        val title: String? = null,
        val type: String = "other",
        val images: List<String> = emptyList(),
        val description: String? = null,
        val tags: List<String> = emptyList(),
        val date: String? = null,
        val links: Link? = null
) {

    /**
     * @return the [type] string as [Type] object. [type] is a string because of the Cloud Firestore library.
     */
    fun type() = when (type) {
        Type.APP.type -> Type.APP
        Type.GAME.type -> Type.GAME
        Type.WEB.type -> Type.WEB
        Type.WATCH.type -> Type.WATCH
        else -> Type.OTHER
    }

    data class Link(
            val github: String? = null,
            val link: String? = null
    )

    enum class Type(val type: String) {
        APP("app"),
        GAME("game"),
        WEB("web"),
        WATCH("watch"),
        OTHER("other");


        val icon
            @DrawableRes
            get() =
                when (this) {
                    Project.Type.APP -> R.drawable.ic_type_app
                    Project.Type.GAME -> R.drawable.ic_type_game
                    Project.Type.WEB -> R.drawable.ic_type_web
                    Project.Type.WATCH -> R.drawable.ic_type_watch
                    Project.Type.OTHER -> R.drawable.ic_type_other
                }

        val color
            @ColorRes
            get() =
                when (this) {
                    Project.Type.APP -> R.color.indicatorType_app
                    Project.Type.GAME -> R.color.indicatorType_game
                    Project.Type.WEB -> R.color.indicatorType_web
                    Project.Type.WATCH -> R.color.indicatorType_watch
                    Project.Type.OTHER -> R.color.indicatorType_other
                }
    }
}