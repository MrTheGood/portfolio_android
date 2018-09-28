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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.model.Project
import eu.insertcode.portfolio.util.getColorStateList
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * Created by maartendegoede on 18/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class ProjectsAdapter : ListAdapter<Project, ProjectsAdapter.ProjectViewHolder>(ProjectCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false))

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProjectViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(project: Project) {
            view.run {
                setOnClickListener {
                    findNavController().navigate(R.id.action_project_detail, bundleOf("project_id" to project.id))
                }

                Glide.with(project_image)
                        .load(project.images.firstOrNull())
                        .into(project_image)
                project_date.text = project.date
                project_title.text = project.title

                project_typeIndicator.backgroundTintList = view.getColorStateList(project.type.color)
                project_typeIndicator.setImageResource(project.type.icon)
            }
        }
    }

    class ProjectCallback : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Project, newItem: Project) =
                oldItem == newItem

    }
}