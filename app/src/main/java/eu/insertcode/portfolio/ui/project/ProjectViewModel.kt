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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import eu.insertcode.portfolio.data.model.Project
import eu.insertcode.portfolio.repository.ProjectRepository

/**
 * Created by maartendegoede on 04/10/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
class ProjectViewModel(
        private val projectId: String
) : ViewModel() {

    private val _project = MediatorLiveData<Project>()
    val project: LiveData<Project> get() = _project

    init {
        val liveProjects = Transformations.map(ProjectRepository.projects) { p ->
            p.data?.projects?.find { it.id == projectId }
        }
        _project.addSource(liveProjects, _project::setValue)

        ProjectRepository.loadProjects()
    }

}