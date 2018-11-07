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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import eu.insertcode.portfolio.databinding.FragmentProjectBinding
import eu.insertcode.portfolio.ui.anim.ProjectCollapse
import eu.insertcode.portfolio.ui.anim.ProjectExpand
import eu.insertcode.portfolio.util.InjectorUtils
import eu.insertcode.portfolio.util.TagColourHelper
import eu.insertcode.portfolio.util.goneIf
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class ProjectFragment : Fragment() {
    private lateinit var projectViewModel: ProjectViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val projectId = ProjectFragmentArgs.fromBundle(arguments).projectId

        val factory = InjectorUtils.provideProjectViewModelFactory(projectId)
        projectViewModel = ViewModelProviders.of(this, factory)[ProjectViewModel::class.java]

        val binding = FragmentProjectBinding.inflate(inflater, container, false).apply {
            viewModel = projectViewModel
            setLifecycleOwner(this@ProjectFragment)
        }

        projectViewModel.project.observe(this, Observer { project ->
            project_tags.removeAllViews()
            project.tags?.forEach { tag ->
                project_tags.addView(TagColourHelper.getChipForTag(tag, requireContext()))
            }
            project_tags.goneIf(project.tags?.isEmpty() == true)
        })

        val adapter = ProjectImageAdapter()
        binding.projectImages.adapter = adapter
        subscribeUi(adapter, savedInstanceState?.getInt("currentItem") ?: 0)

        sharedElementEnterTransition = ProjectExpand()
        sharedElementReturnTransition = ProjectCollapse()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentItem", project_images.currentItem)
        super.onSaveInstanceState(outState)
    }

    private fun subscribeUi(adapter: ProjectImageAdapter, currentItem: Int) {
        projectViewModel.project.observe(viewLifecycleOwner, Observer { project ->
            val projectImages = project?.images
            if (projectImages != null) {
                adapter.projectImages = projectImages
                project_images.currentItem = currentItem
            }
        })
    }
}