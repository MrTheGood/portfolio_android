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
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import eu.insertcode.portfolio.databinding.ItemTagBinding
import eu.insertcode.portfolio.databinding.ItemTimelineBinding
import eu.insertcode.portfolio.databinding.ViewAboutBinding
import eu.insertcode.portfolio.main.viewmodels.portfolio.AboutViewState
import eu.insertcode.portfolio.main.viewmodels.portfolio.TimelineItemViewState
import timber.log.Timber

/**
 * Created by maartendegoede on 18/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class PortfolioAdapter(
        private val lifecycleOwner: LifecycleOwner,

        private val onTimelineItemTapped: (id: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_ABOUT = 0
        const val TYPE_TIMELINE = 1
    }

    init {
        setHasStableIds(true)
    }

    private val tagViewPool = SimplePool<ItemTagBinding>(20)

    var timelineItemViewStates = listOf<TimelineItemViewState>()
        set(value) {
            field = value
            dataChanged()
        }

    var aboutViewState: AboutViewState? = null
        set(value) {
            field = value
            dataChanged()
        }

    private var viewStates = listOf<Any?>()
    private fun dataChanged() {
        viewStates = listOf<Any?>(aboutViewState) + timelineItemViewStates
        notifyDataSetChanged()
    }

    override fun getItemCount() = viewStates.count()

    override fun getItemViewType(position: Int) =
            when {
                viewStates[position] is AboutViewState -> TYPE_ABOUT
                viewStates[position] is TimelineItemViewState -> TYPE_TIMELINE
                else -> throw IllegalArgumentException("Unsupported view type")
            }

    override fun getItemId(position: Int) =
            (viewStates[position] as? TimelineItemViewState)?.id?.hashCode()?.toLong()
                ?: getItemViewType(position).toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                TYPE_ABOUT -> AboutViewHolder(
                        ViewAboutBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent, false
                        ).also {
                            it.lifecycleOwner = lifecycleOwner
                        }
                )

                TYPE_TIMELINE -> ViewHolder(
                        ItemTimelineBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent, false
                        ).also {
                            it.lifecycleOwner = lifecycleOwner
                        }, tagViewPool)
                else -> throw IllegalArgumentException("Unsupported view type")
            }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AboutViewHolder -> holder.binding.apply {
                viewState = viewStates[position] as AboutViewState
                executePendingBindings()
            }
            is ViewHolder -> holder.binding.apply {
                val item = viewStates[position] as TimelineItemViewState
                viewState = item
                onClickListener = View.OnClickListener { onTimelineItemTapped(item.id) }
                holder.bindTags()
                executePendingBindings()
            }
        }
    }

    class ViewHolder(
            val binding: ItemTimelineBinding,
            private val tagViewPool: SimplePool<ItemTagBinding>
    ) : RecyclerView.ViewHolder(binding.root) {
        var tagBindings = ArrayList<ItemTagBinding>()

        fun bindTags() {
            val tagsLayout = binding.projectTagsLayout

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

    class AboutViewHolder(val binding: ViewAboutBinding) : RecyclerView.ViewHolder(binding.root)
}