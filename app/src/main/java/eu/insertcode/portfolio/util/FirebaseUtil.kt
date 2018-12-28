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
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import eu.insertcode.portfolio.data.model.Project

/**
 * Created by maartendegoede on 08/11/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
fun Context.analyticsSelectProject(project: Project) {
    logEvent(Event.SELECT_CONTENT, project.toBundle())
}

fun Context.analyticsViewProjectImage(project: Project, image: String) {
    logEvent(Event.SELECT_CONTENT, project.toBundle().apply {
        putString("project_image", image)
    })
}

fun Fragment.analyticsShareProject(project: Project) {
    context?.logEvent(Event.SHARE, project.toBundle())
}

fun Fragment.clickProjectLink(project: Project, link: String) {
    context?.logEvent("click_project_url", project.toBundle().apply {
        putString("project_link", link)
        putString("project_link_type", when (link) {
            project.links?.playstore -> "playstore"
            project.links?.github -> "github"
            project.links?.link -> "link"
            else -> "unknown"
        })
    })
}

private fun Context.logEvent(name: String, bundle: Bundle) =
        FirebaseAnalytics.getInstance(this).logEvent(name, bundle)


private fun Project.toBundle() =
        Bundle().apply {
            putString(Param.ITEM_ID, id)
            putString(Param.ITEM_NAME, title)
            putString(Param.ITEM_CATEGORY, type)
            putString(Param.CONTENT_TYPE, "project")
        }