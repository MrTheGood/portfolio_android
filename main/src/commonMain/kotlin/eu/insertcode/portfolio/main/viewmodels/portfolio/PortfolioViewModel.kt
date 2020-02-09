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

package eu.insertcode.portfolio.main.viewmodels.portfolio

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import eu.insertcode.portfolio.main.data.Resource
import eu.insertcode.portfolio.main.data.isError
import eu.insertcode.portfolio.main.data.isLoading
import eu.insertcode.portfolio.main.data.isSuccess
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.repositories.ProjectRepository
import eu.insertcode.portfolio.main.services.ServiceProvider
import eu.insertcode.portfolio.main.viewmodels.TimelineItemViewState
import eu.insertcode.portfolio.main.viewmodels.portfolio.PortfolioViewState.Error
import eu.insertcode.portfolio.main.viewmodels.portfolio.PortfolioViewState.TimelineCollectionError

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
class PortfolioViewModel(
        val eventsDispatcher: EventsDispatcher<EventsListener>
) : ViewModel() {
    private lateinit var projects: Resource<List<Project>, Error>

    private val isNetworkAvailable
        get() = ServiceProvider.connectivityService.isNetworkAvailable()

    // UI
    private val _viewState = MutableLiveData<PortfolioViewState?>(null)
    val viewState: LiveData<PortfolioViewState?> = _viewState.readOnly()

    private val _isNewProjectsLabelVisible = MutableLiveData(false)
    val isNewProjectsLabelVisible = _isNewProjectsLabelVisible.readOnly()

    // Configure
    fun configure() {
        ProjectRepository.observeProjects()

        ProjectRepository.projects.addObserver { resource ->
            projects = resource.run {
                val error = error?.let { if (isNetworkAvailable) Error.UnknownError else Error.NoInternet }
                Resource(state, data, error)
            }

            if (viewState.value?.timelineItemViewStates.isNullOrEmpty())
                updateViewState()
            else _isNewProjectsLabelVisible.value = true
        }
    }

    private fun updateViewState() {
        val timelineViewStates = projects.data?.map { TimelineItemViewState(it) } ?: emptyList()
        val highlightViewStates = projects.data?.map { TimelineItemViewState(it) } ?: emptyList()

        _isNewProjectsLabelVisible.value = false
        _viewState.value = PortfolioViewState(
                errorViewError = projects.error,
                isLoading = projects.isLoading && projects.data.isNullOrEmpty(),

                isTimelineVisible = !projects.isError && timelineViewStates.isNotEmpty(),
                timelineItemViewStates = timelineViewStates,
                timelineViewError = TimelineCollectionError.NoContent.takeIf { projects.isSuccess && timelineViewStates.isEmpty() },

                isHighlightsVisible = !projects.isError && highlightViewStates.isNotEmpty(),
                highlightViewStates = highlightViewStates
        )
    }


    // Actions

    fun onProjectItemTapped(projectId: String) {
        eventsDispatcher.dispatchEvent { navigateToProject(projectId) }
    }

    fun onErrorViewTapped() {
        ProjectRepository.observeProjects()
    }

    fun onNewProjectsLabelTapped() {
        updateViewState()
    }


    // Events

    interface EventsListener {
        fun navigateToProject(projectId: String)
    }
}