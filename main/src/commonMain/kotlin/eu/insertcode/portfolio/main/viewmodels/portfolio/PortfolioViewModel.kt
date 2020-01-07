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

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import eu.insertcode.portfolio.main.data.*
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.repositories.ProjectRepository
import eu.insertcode.portfolio.main.viewmodels.HighlightViewState
import eu.insertcode.portfolio.main.viewmodels.TimelineViewState
import eu.insertcode.portfolio.main.viewmodels.TimelineViewState.TimelineViewError

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
class PortfolioViewModel : ViewModel() {
    private lateinit var projects: Resource<List<Project>, PortfolioViewState.Error>

    // UI
    private val _viewState = MutableLiveData<PortfolioViewState?>(null)
    val viewState: LiveData<PortfolioViewState?> = _viewState

    // Configure

    fun configure() {
        ProjectRepository.getProjects()
        ProjectRepository.projects.addObserver {
            projects = it.transformError {
                val isInternetAvailable = true // todo: implement isInternetAvailable check
                if (isInternetAvailable) PortfolioViewState.Error.UnknownError else PortfolioViewState.Error.NoInternet
            }
            updateViewState()
        }
    }

    private fun updateViewState() {
        val timelineViewStates = projects.data?.map { TimelineViewState(it) } ?: emptyList()
        val highlightViewStates = projects.data?.map { HighlightViewState(it) } ?: emptyList()

        _viewState.value = PortfolioViewState(
                errorViewError = projects.error,
                isLoading = projects.isLoading,

                isTimelineVisible = projects.isSuccess,
                timelineViewStates = timelineViewStates,
                timelineViewError = TimelineViewError.NoContent.takeIf { timelineViewStates.isEmpty() },

                isHighlightsVisible = projects.isSuccess && highlightViewStates.isNotEmpty(),
                highlightViewStates = highlightViewStates
        )
    }

    // Actions

    fun onProjectItemTapped(projectId: String) {
        navigateToProject.newEvent(projectId)
    }

    fun onErrorViewTapped() {
        projects = Resource.Success(listOf(
                //todo: actual data
        ))
    }

    fun onRefreshViewSwiped() {
        ProjectRepository.getProjects(true)
    }

    // Reactions
    /** @sample navigateToProject(projectId) */
    val navigateToProject: MutableLiveData<Event<String?>> = MutableLiveData(Event(null))
}