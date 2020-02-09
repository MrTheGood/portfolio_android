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

import eu.insertcode.portfolio.main.data.Resource
import eu.insertcode.portfolio.main.data.isLoading
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.data.models.ProjectType
import eu.insertcode.portfolio.main.data.models.toProjectType
import eu.insertcode.portfolio.main.viewmodels.TagViewState

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 *
 * Based on: https://ryanharter.com/blog/encapsulating-view-state/
 */
data class ProjectViewState(
        private val project: Resource<Project, Error>,
        val id: String,

        val errorViewError: Error? = project.error,
        val isLoading: Boolean = project.isLoading,

        val titleLabelText: String? = project.data?.title,

        val isImageVisible: Boolean = project.data?.images?.isNotEmpty() ?: false,
        val imageUrl: String? = project.data?.images?.firstOrNull(),

        val projectType: ProjectType? = project.data?.type?.toProjectType(),

        val locationLabelText: String? = project.data?.location,
        val lastUpdateDateLabelText: String? = project.data?.run { updatedAt ?: endedAt ?: startedAt ?: listedAt }?.toReadableString(),

        val descriptionLabelText: String? = project.data?.description,

        val webLink: String? = project.data?.links?.link,
        val webLinkText: String? = project.data?.links?.link?.replaceFirst("https://", "")?.replaceFirst("http://", ""),
        val isWebLinkVisible: Boolean = webLink != null,
        val playstoreLink: String? = project.data?.links?.playstore,
        val isPlaystoreLinkVisible: Boolean = playstoreLink != null,
        val githubLink: String? = project.data?.links?.github,
        val isGithubLinkVisible: Boolean = githubLink != null,


        val isTagCollectionVisible: Boolean = isImageVisible,
        val tagViewStates: List<TagViewState> = project.data?.tags?.map { TagViewState(it) } ?: emptyList()
) {
    enum class Error {
        NotFound, NoInternet, UnknownError
    }
}