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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.data.model.Project
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
            Glide.with(view)
                    .load(project.images.firstOrNull())
                    .into(view.project_image)
            view.project_date.text = project.date
            view.project_title.text = project.title
        }
    }

    class ProjectCallback : DiffUtil.ItemCallback<Project>() {
        override fun areItemsTheSame(oldItem: Project, newItem: Project) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Project, newItem: Project) =
                oldItem == newItem

    }
}