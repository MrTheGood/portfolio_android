/*
 *    Copyright 2020 Maarten de Goede
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

package eu.insertcode.portfolio.main.viewmodels.project

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import eu.insertcode.portfolio.main.data.Resource
import eu.insertcode.portfolio.main.data.isNotFound
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.repositories.ProjectRepository

/**
 * Created by maartendegoede on 09/02/2020.
 * Copyright © 2020 Maarten de Goede. All rights reserved.
 */
class ProjectViewModel(
        val eventsDispatcher: EventsDispatcher<EventsListener>
) : ViewModel() {
    private lateinit var project: Resource<Project, ProjectViewState.Error>
    private lateinit var projectId: String

    // UI
    private val _viewState = MutableLiveData<ProjectViewState?>(null)
    val viewState: LiveData<ProjectViewState?> = _viewState.readOnly()

    // Configure
    fun configure(projectId: String) {
        this.projectId = projectId
        ProjectRepository.getProjectDocument(projectId, onComplete = { resource ->
            project = resource.run {
                Resource(state, data, if (isNotFound) ProjectViewState.Error.NotFound else ProjectViewState.Error.UnknownError)
            }
            updateViewState()
        })
    }

    private fun updateViewState() {
        _viewState.value = ProjectViewState(project, projectId)
    }


    // Actions

    fun onImageItemViewTapped(imageIndex: Int) {
        eventsDispatcher.dispatchEvent { navigateToImageGallery(imageIndex) }
    }

    fun onLinkItemTapped(url: String) {
        eventsDispatcher.dispatchEvent { openUrl(url) }
    }

    fun onErrorViewTapped() {
        configure(projectId)
    }


    // Events

    interface EventsListener {
        fun navigateToImageGallery(imageIndex: Int)
        fun openUrl(url: String)
    }
}