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

package eu.insertcode.portfolio.ui.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.model.Project
import eu.insertcode.portfolio.repository.ProjectRepository
import eu.insertcode.portfolio.util.TagColourHelper
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class ProjectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_project, container, false)

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //todo: viewModel
        val project = ProjectRepository.projects.value?.data?.projects?.find { it.id == arguments!!.getString("project_id") }

        Glide.with(project_image)
                .load(project!!.images.firstOrNull())
                .into(project_image)
        project_title.text = project.title
        project_date.text = project.date
        project_description.text = project.description

        project_typeIndicator.backgroundTintList = TagColourHelper.getTagColorSL(project.type.toString(), requireContext())
        val resource = when (project.type) {
            Project.Type.APP -> R.drawable.ic_type_app
            Project.Type.GAME -> R.drawable.ic_type_game
            Project.Type.WEB -> R.drawable.ic_type_web
            Project.Type.WATCH -> R.drawable.ic_type_watch
            Project.Type.OTHER -> R.drawable.ic_type_other
        }
        project_typeIndicator.setImageResource(resource)
    }

}