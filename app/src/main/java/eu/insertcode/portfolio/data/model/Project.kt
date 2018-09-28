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
import com.google.gson.annotations.SerializedName
import eu.insertcode.portfolio.R

/**
 * Created by maartendegoede on 23/08/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
data class Project(
        @field:SerializedName("id")
        val id: String,

        @field:SerializedName("title")
        val title: String,

        @field:SerializedName("type")
        val type: Type,

        @field:SerializedName("images")
        val images: List<String>,

        @field:SerializedName("description")
        val description: String,

        @field:SerializedName("tags")
        val tags: List<String>,

        @field:SerializedName("date")
        val date: String?
) {
    enum class Type(val type: String) {
        @SerializedName("app")
        APP("app"),

        @SerializedName("game")
        GAME("game"),

        @SerializedName("web")
        WEB("web"),

        @SerializedName("watch")
        WATCH("watch"),

        @SerializedName("other")
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