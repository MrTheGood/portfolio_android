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

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 *
 * Based on: https://ryanharter.com/blog/encapsulating-view-state/
 */
data class PortfolioViewState(
        val errorViewError: Error?,
        val isLoading: Boolean,

        val isTimelineVisible: Boolean,
        val timelineItemViewStates: List<TimelineItemViewState>,

        val isHighlightsVisible: Boolean,
        val highlightViewStates: List<TimelineItemViewState>
) {
    enum class Error {
        NoInternet, UnknownError
    }
}