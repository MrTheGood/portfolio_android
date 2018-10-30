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
import com.google.gson.GsonBuilder
import eu.insertcode.portfolio.data.Resource
import eu.insertcode.portfolio.data.isSuccess
import eu.insertcode.portfolio.data.model.ProjectsList
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Created by maartendegoede on 26/09/2018.
 * Copyright © 2018 insetCode.eu. All rights reserved.
 */
object ProjectRepository {

    @Suppress("ObjectPropertyName")
    private val _projects = MutableLiveData<Resource<ProjectsList, Exception>>()
    val projects: LiveData<Resource<ProjectsList, Exception>>
        get() = _projects

    // TODO: Use Retrofit instead for api requests..
    // TODO: Find out how Retrofit works..
    fun loadProjects(force: Boolean = false) {
        if (_projects.value.isSuccess && !force) return
        _projects.value = Resource.loading(_projects.value?.data)

        GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT) {
            try {
                val stream = URL("https://portfolio.insertcode.eu/gateway/v2alpha/projects.php").openStream()
                val result = stream.bufferedReader().use { it.readText() }.also { stream.close() }

                val projects = GsonBuilder().create().fromJson(result, ProjectsList::class.java)
                _projects.postValue(Resource.success(projects))
            } catch (e: Exception) {
                e.printStackTrace()
                _projects.postValue(Resource.error(e, _projects.value?.data))
            }
        }
    }
}
