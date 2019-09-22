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

import eu.insertcode.portfolio.main.data.Property
import eu.insertcode.portfolio.main.data.Resource
import eu.insertcode.portfolio.main.data.isLoading
import eu.insertcode.portfolio.main.data.isSuccess
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.viewmodels.HighlightViewState
import eu.insertcode.portfolio.main.viewmodels.TimelineViewState
import eu.insertcode.portfolio.main.viewmodels.TimelineViewState.TimelineViewError

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
class PortfolioViewModel(
        viewState: Property<PortfolioViewState>,

        // Reactions
        private val navigateToProject: (projectId: String) -> Unit
) {
    private lateinit var projects: Resource<List<Project>, PortfolioViewState.Error>

    // UI
    var viewState by viewState


    // Configure

    fun configure() {
        //TODO: Use Firebase

        projects = Resource.Loading()
        updateViewState()

        temporaryUpdateFunction()
    }

    private fun temporaryUpdateFunction() {
        //todo: remove
        projects = Resource.Error(PortfolioViewState.Error.NoInternet)
        updateViewState()
    }

    private fun updateViewState() {
        val timelineViewStates = projects.data?.map { TimelineViewState(it) } ?: emptyList()
        val highlightViewStates = projects.data?.map { HighlightViewState(it) } ?: emptyList()

        viewState = PortfolioViewState(
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
        navigateToProject(projectId)
    }

    fun onErrorViewTapped() {
        projects = Resource.Success(listOf(
                //todo: actual data
        ))
    }

    fun onRefreshViewSwiped() {
        temporaryUpdateFunction()
    }
}