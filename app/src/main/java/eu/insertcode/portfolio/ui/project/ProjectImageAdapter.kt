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
import androidx.core.util.Pools
import androidx.viewpager.widget.PagerAdapter
import eu.insertcode.portfolio.databinding.ItemProjectImageBinding
import timber.log.Timber


/**
 * Created by maartendegoede on 11/10/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ProjectImageAdapter : PagerAdapter() {
    private val viewPool = Pools.SimplePool<ItemProjectImageBinding>(3)

    var projectImages = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun instantiateItem(container: ViewGroup, position: Int): ItemProjectImageBinding {
        val binding = viewPool.acquire()
                ?: ItemProjectImageBinding.inflate(
                        LayoutInflater.from(container.context),
                        container,
                        false
                )

        binding.image = projectImages[position]
        container.addView(binding.root)

        return binding
    }

    override fun destroyItem(container: ViewGroup, position: Int, binding: Any) {
        binding as ItemProjectImageBinding

        container.removeView(binding.root)
        if (!viewPool.release(binding)) {
            Timber.w("ItemProjectImage not released.. Consider increasing pool size.")
        }
    }

    override fun getCount() = projectImages.size

    override fun isViewFromObject(view: View, item: Any) =
            view === (item as ItemProjectImageBinding).root
}
