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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.insertcode.portfolio.data.Resource
import eu.insertcode.portfolio.data.model.Project

/**
 * Created by maartendegoede on 11/09/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 */
class ProjectsViewModel : ViewModel() {

    private val _projects = MutableLiveData<Resource<List<Project>, Exception?>>()
    val projects: LiveData<Resource<List<Project>, Exception?>>
        get() = _projects

    init {
        _projects.value = Resource.success(listOf(
                //todo: get data from web api
                Project(
                        id = "0",
                        title = "Lorem Ipsum",
                        shortDescription = "",
                        fullDescription = "",
                        date = "",
                        images = emptyList(),
                        tags = emptyList()
                ),
                Project(
                        id = "1",
                        title = "Dolor Sit",
                        shortDescription = "",
                        fullDescription = "",
                        date = "",
                        images = emptyList(),
                        tags = emptyList()
                )
        ))
    }
}