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
import eu.insertcode.portfolio.main.data.models.AboutMe
import eu.insertcode.portfolio.main.data.toLoading
import eu.insertcode.portfolio.main.services.ServiceProvider

/**
 * Created by maartendegoede on 2019-09-23.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 *
 * NOTE: This file is under heavy development
 */
object AboutRepository {
    private const val ABOUT_ME = "about/me"

    private val firestoreService
        get() = ServiceProvider.firestoreService

    private val _about: MutableLiveData<Resource<AboutMe, Exception>> = MutableLiveData(Resource.loading())
    val about: LiveData<Resource<AboutMe, Exception>> = _about


    init {
        observeAbout()
    }


    private fun observeAbout() {
        _about.value = _about.value.toLoading()

        firestoreService.observeDocument(ABOUT_ME, transform = { AboutMe(it) }) { result ->
            _about.value = result
        }
    }
}