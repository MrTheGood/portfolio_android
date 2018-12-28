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

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.core.util.Pools
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.chrisbanes.photoview.PhotoView
import eu.insertcode.portfolio.R
import timber.log.Timber


/**
 * Created by maartendegoede on 11/10/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ProjectImageAdapter(
        private var onItemClickListener: (position: Int) -> Unit = { },
        private val zoomable: Boolean = false
) : PagerAdapter() {
    private val viewPool = Pools.SimplePool<ImageView>(3)

    var projectImages = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private fun inflateView(container: ViewGroup) =
            if (zoomable) PhotoView(container.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                adjustViewBounds = true
            } else {
                ImageView(container.context).apply { layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT) }
            }.apply {
                setBackgroundColor(resources.getColor(R.color.image_background))
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

    override fun instantiateItem(container: ViewGroup, position: Int): ImageView {
        val view = viewPool.acquire()
                ?: inflateView(container)

        Glide.with(view.context)
                .load(projectImages[position])
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        view.setOnClickListener { onItemClickListener(position) }
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        view as ImageView

        container.removeView(view)
        if (!viewPool.release(view)) {
            Timber.w("ItemProjectImage not released.. Consider increasing pool size.")
        }
    }

    override fun getCount() = projectImages.size

    override fun isViewFromObject(view: View, item: Any) =
            view === item
}
