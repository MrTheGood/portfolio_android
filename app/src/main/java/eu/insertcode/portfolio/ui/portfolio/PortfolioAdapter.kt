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

package eu.insertcode.portfolio.ui.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.insertcode.portfolio.data.model.Project
import eu.insertcode.portfolio.databinding.ItemProjectBinding
import eu.insertcode.portfolio.util.analyticsSelectProject

/**
 * Created by maartendegoede on 18/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class PortfolioAdapter : ListAdapter<Project, PortfolioAdapter.ViewHolder>(ProjectCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = getItem(position)
        holder.apply {
            bind(createOnClickListener(project), project)
            itemView.tag = project
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    private fun createOnClickListener(project: Project) =
            View.OnClickListener { v ->
                val direction = PortfolioFragmentDirections.ActionProjectDetail(project.id!!)
                val extras = FragmentNavigatorExtras(v to project.id)
                v.findNavController().navigate(direction, extras)
                v.context.analyticsSelectProject(project)
            }

    class ViewHolder(
            private val binding: ItemProjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Project) {
            binding.apply {
                clickListener = listener
                project = item
                executePendingBindings()
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