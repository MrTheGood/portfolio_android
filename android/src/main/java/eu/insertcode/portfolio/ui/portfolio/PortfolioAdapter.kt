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

package eu.insertcode.portfolio.ui.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pools.SimplePool
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.insertcode.portfolio.databinding.ItemTagBinding
import eu.insertcode.portfolio.databinding.ItemTimelineBinding
import eu.insertcode.portfolio.main.viewmodels.TimelineItemViewState
import kotlinx.android.synthetic.main.item_timeline.view.*
import timber.log.Timber

/**
 * Created by maartendegoede on 18/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class PortfolioAdapter(
        private val onTimelineItemTapped: (id: String) -> Unit
) : ListAdapter<TimelineItemViewState, PortfolioAdapter.ViewHolder>(ProjectCallback()) {
    private val tagViewPool = SimplePool<ItemTagBinding>(20)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            viewState = item
            onClickListener = View.OnClickListener { onTimelineItemTapped(item.id) }
            holder.bindTags()
            executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false), tagViewPool)

    class ViewHolder(
            val binding: ItemTimelineBinding,
            private val tagViewPool: SimplePool<ItemTagBinding>
    ) : RecyclerView.ViewHolder(binding.root) {
        var tagBindings = ArrayList<ItemTagBinding>()

        fun bindTags() {
            val tagsLayout = binding.root.project_tags_layout

            tagBindings.forEach { if (!tagViewPool.release(it)) Timber.w("Couldn't release tag view. Consider increasing pool size.") }
            tagBindings.clear()
            tagsLayout.removeAllViews()

            binding.viewState!!.tagViewStates.forEach { viewState ->
                val tagView = tagViewPool.acquire()
                    ?: ItemTagBinding.inflate(LayoutInflater.from(tagsLayout.context), tagsLayout, false).apply {
                        lifecycleOwner = binding.lifecycleOwner
                    }

                tagView.viewState = viewState
                tagView.executePendingBindings()
                tagsLayout.addView(tagView.root)
            }
        }
    }

    class ProjectCallback : DiffUtil.ItemCallback<TimelineItemViewState>() {
        override fun areItemsTheSame(oldItem: TimelineItemViewState, newItem: TimelineItemViewState) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TimelineItemViewState, newItem: TimelineItemViewState) =
                oldItem == newItem

    }
}