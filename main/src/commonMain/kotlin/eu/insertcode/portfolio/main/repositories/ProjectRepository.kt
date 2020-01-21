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

package eu.insertcode.portfolio.main.repositories

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import eu.insertcode.portfolio.main.data.Resource
import eu.insertcode.portfolio.main.data.isSuccess
import eu.insertcode.portfolio.main.data.models.Project
import eu.insertcode.portfolio.main.data.toLoading
import eu.insertcode.portfolio.main.services.ServiceProvider

/**
 * Created by maartendegoede on 2019-09-23.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 *
 * NOTE: This file is under heavy development
 */
object ProjectRepository {
    private const val PROJECTS = "projects"
    private val firestoreService
        get() = ServiceProvider.firestoreService

    private val _projects: MutableLiveData<Resource<List<Project>, Exception>> =
            MutableLiveData(Resource.loading())
    val projects: LiveData<Resource<List<Project>, Exception>> = _projects

    fun getProjects(forceUpdate: Boolean = false) {
        if (_projects.value.isSuccess && !forceUpdate) return
        _projects.value = _projects.value.toLoading()

        //todo: remove v4_support
        firestoreService.getCollection(PROJECTS,
                equalityQueryParams = mapOf("listed" to true, "v4_support" to true),
                transform = { Project(it) }) { result ->
            _projects.value = result.copy(data = result.data?.sortedByDescending { it.updatedAt?.seconds ?: it.listedAt.seconds })
        }
    }


    fun getProjectDocument(projectId: String, forceUpdate: Boolean = false, onComplete: (Resource<Project?, Exception>) -> Unit) {
        val project = _projects.value.data?.find { it.id == projectId }
        if (_projects.value.isSuccess && project != null && !forceUpdate) {
            onComplete(Resource.success(project))
            return
        }
        onComplete(Resource.loading(project))

        firestoreService.getDocument("$PROJECTS/$projectId", transform = { Project(it) }) { result ->
            //todo: how can it be successful but with `null` as project? What is the case?
            onComplete(result)
        }
    }
}