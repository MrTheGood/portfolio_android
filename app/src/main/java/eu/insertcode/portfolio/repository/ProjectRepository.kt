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

package eu.insertcode.portfolio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import eu.insertcode.portfolio.data.Resource
import eu.insertcode.portfolio.data.isSuccess
import eu.insertcode.portfolio.data.model.Project
import java.lang.Exception as JavaException


/**
 * Created by maartendegoede on 26/09/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
object ProjectRepository {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    @Suppress("ObjectPropertyName")
    private val _projects = MutableLiveData<Resource<List<Project>, JavaException>>()
    val projects: LiveData<Resource<List<Project>, JavaException>>
        get() = _projects

    @Suppress("ObjectPropertyName")
    private val _project = MutableLiveData<Resource<Project?, JavaException>>()
    val project: LiveData<Resource<Project?, JavaException>>
        get() = _project

    fun loadProjects(force: Boolean = false) {
        if (_projects.value.isSuccess && !force) return
        _projects.value = Resource.loading(_projects.value?.data)

        firestore.collection("projects")
                .whereEqualTo("listed", true)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _projects.postValue(Resource.success(task.result!!.map { it.toObject(Project::class.java) }.sortedByDescending { it.importance }))
                    } else {
                        task.exception!!.printStackTrace()
                        _projects.postValue(Resource.error(task.exception!!, _projects.value?.data))
                    }
                }
    }

    fun loadProject(id: String, force: Boolean = false) {
        val project = _projects.value?.data?.find { it.id == id }
        if (project != null && !force) {
            _project.value = Resource.success(project)
            return
        }
        _project.value = Resource.loading(project)

        firestore.document("projects/$id")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _project.postValue(Resource.success(task.result?.toObject(Project::class.java)))
                    } else {
                        task.exception!!.printStackTrace()
                        _project.postValue(Resource.error(task.exception!!, _project.value?.data))
                    }
                }
    }
}
