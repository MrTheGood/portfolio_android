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
import androidx.navigation.fragment.navArgs
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import eu.insertcode.portfolio.BR
import eu.insertcode.portfolio.R
import eu.insertcode.portfolio.databinding.FragmentProjectBinding
import eu.insertcode.portfolio.databinding.ItemTagBinding
import eu.insertcode.portfolio.main.viewmodels.project.ProjectViewModel
import eu.insertcode.portfolio.main.viewmodels.project.ProjectViewState
import eu.insertcode.portfolio.ui.anim.ProjectCollapse
import eu.insertcode.portfolio.ui.anim.ProjectExpand
import eu.insertcode.portfolio.util.startUrlIntent
import kotlinx.android.synthetic.main.fragment_project.view.*


/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
class ProjectFragment : MvvmFragment<FragmentProjectBinding, ProjectViewModel>(), ProjectViewModel.EventsListener {

    override val layoutId = R.layout.fragment_project
    override val viewModelClass = ProjectViewModel::class.java
    override val viewModelVariableId = BR.viewModel

    override fun viewModelFactory() = createViewModelFactory { ProjectViewModel(eventsDispatcherOnMain()) }

    private val args: ProjectFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        viewModel.eventsDispatcher.bind(
                lifecycleOwner = this,
                listener = this
        )
        val projectId = args.projectId
        viewModel.configure(projectId)

        viewModel.viewState.addObserver { bindTags(it ?: return@addObserver) }

        sharedElementEnterTransition = ProjectExpand()
        sharedElementReturnTransition = ProjectCollapse()

        return view
    }

    private fun bindTags(viewState: ProjectViewState) {
        val tagsLayout = binding.root.project_tags_layout
        tagsLayout.removeAllViews()

        viewState.tagViewStates.forEach { viewState ->
            val tagView = ItemTagBinding.inflate(LayoutInflater.from(tagsLayout.context), tagsLayout, false).apply {
                lifecycleOwner = binding.lifecycleOwner
            }
            tagView.viewState = viewState
            tagView.executePendingBindings()
            tagsLayout.addView(tagView.root)
        }
    }

    override fun navigateToImageGallery(imageIndex: Int) {
        TODO("not implemented")
    }

    override fun openUrl(url: String) {
        requireActivity().startUrlIntent(url)
    }
}