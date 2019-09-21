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
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.databinding.FragmentProjectBinding
import eu.insertcode.portfolio.ui.anim.ProjectCollapse
import eu.insertcode.portfolio.ui.anim.ProjectExpand
import eu.insertcode.portfolio.util.*
import kotlinx.android.synthetic.main.fragment_project.*


/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class ProjectFragment : Fragment() {
    private lateinit var projectViewModel: ProjectViewModel
    private val args: ProjectFragmentArgs by navArgs()
    private var currentItem: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val projectId = args.projectId

        val factory = InjectorUtils.provideProjectViewModelFactory(projectId)
        projectViewModel = ViewModelProviders.of(this, factory)[ProjectViewModel::class.java]

        val binding = FragmentProjectBinding.inflate(inflater, container, false).apply {
            viewModel = projectViewModel
            lifecycleOwner = this@ProjectFragment
        }

        projectViewModel.project.observe(this, Observer { project ->
            project_tags.removeAllViews()
            project.data?.tags?.forEach { tag ->
                project_tags.addView(TagColourHelper.getChipForTag(tag, requireContext()))
            }
            project_tags.goneIf(project.data?.tags?.isEmpty() == true)
        })

        val adapter = ProjectImageAdapter(
                onItemClickListener = { position: Int ->
                    projectViewModel.project.value?.data?.run {
                        val direction = ProjectFragmentDirections.actionProjectImage(id!!, position)
                        view?.findNavController()?.navigate(direction)
                        context?.analyticsViewProjectImage(this, images[position])
                    }
                }
        )
        binding.projectImages.adapter = adapter
        binding.projectImages.addOnPageSelectedListener { position -> currentItem = position }
        subscribeUi(adapter, savedInstanceState?.getInt("currentItem") ?: 0)

        sharedElementEnterTransition = ProjectExpand()
        sharedElementReturnTransition = ProjectCollapse()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_project, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.apply {
            val links = projectViewModel.project.value?.data?.links
            findItem(R.id.menu_link).isVisible = links?.link != null
            findItem(R.id.menu_get_app).isVisible = links?.playstore != null
            findItem(R.id.menu_github).isVisible = links?.github != null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val project = projectViewModel.project.value?.data
            ?: return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.menu_github -> {
                startOpenUrlIntent(project.links?.github!!)
                clickProjectLink(project, project.links.github)
            }
            R.id.menu_link -> {
                startOpenUrlIntent(project.links?.link!!)
                clickProjectLink(project, project.links.link)
            }
            R.id.menu_get_app -> {
                startOpenUrlIntent(project.links?.playstore!!)
                clickProjectLink(project, project.links.playstore)
            }
            R.id.menu_share -> {
                startTextShareIntent(getString(R.string.string_share_project, project.id))
                analyticsShareProject(project)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
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