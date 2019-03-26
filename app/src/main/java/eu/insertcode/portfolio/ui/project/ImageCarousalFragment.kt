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

package eu.insertcode.portfolio.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.util.InjectorUtils
import eu.insertcode.portfolio.util.addOnPageSelectedListener
import kotlinx.android.synthetic.main.fragment_image_carousal.view.*
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Created by maartendegoede on 19/12/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ImageCarousalFragment : Fragment() {
    private lateinit var projectViewModel: ProjectViewModel
    private val args: ImageCarousalFragmentArgs by navArgs()
    private var currentItem: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        currentItem = savedInstanceState?.getInt("currentItem", args.currentItem)
            ?: args.currentItem

        val factory = InjectorUtils.provideProjectViewModelFactory(args.projectId)
        projectViewModel = ViewModelProviders.of(this, factory)[ProjectViewModel::class.java]

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_image_carousal, container, false)

        val adapter = ProjectImageAdapter(zoomable = true)
        view.project_images.adapter = adapter
        view.project_images.addOnPageSelectedListener { position -> currentItem = position }
        subscribeUi(adapter, currentItem)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentItem", currentItem)
        super.onSaveInstanceState(outState)
    }

    private fun subscribeUi(adapter: ProjectImageAdapter, currentItem: Int) {
        projectViewModel.project.observe(viewLifecycleOwner, Observer { project ->
            val projectImages = project?.data?.images
            if (projectImages != null) {
                adapter.projectImages = projectImages
                project_images.currentItem = currentItem
            }
        })
    }
}
