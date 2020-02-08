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

package eu.insertcode.portfolio.main.viewmodels

import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.data.models.ProjectType
import eu.insertcode.portfolio.main.data.models.toProjectType

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
data class TimelineItemViewState(
        private val project: Project,

        val id: String = project.id,
        val titleLabelText: String = project.title,

        val isThumbnailImageVisible: Boolean = project.images.isNotEmpty(),
        val thumbnailImageUrl: String? = project.images.firstOrNull(),

        val isDescriptionLabelVisible: Boolean = !isThumbnailImageVisible,
        val descriptionLabelText: String = project.description,

        val projectType: ProjectType = project.type.toProjectType(),

        val locationLabelText: String = project.location,
        val dateLabelText: String = project.startedAt?.toString()
            ?: "Sometime", //todo: implement readable string thing

        val isTagCollectionVisible: Boolean = isThumbnailImageVisible,
        val tagViewStates: List<TagViewState> = project.tags.map { TagViewState(it) }
)