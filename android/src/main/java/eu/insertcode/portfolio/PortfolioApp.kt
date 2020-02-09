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

package eu.insertcode.portfolio

import androidx.multidex.MultiDexApplication
import eu.insertcode.portfolio.main.services.AndroidConnectivityService
import eu.insertcode.portfolio.main.services.AndroidFirestoreService
import eu.insertcode.portfolio.main.services.ServiceProvider
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * Created by maartendegoede on 23/08/2018.
 * Copyright © 2018 Maarten de Goede. All rights reserved.
 */
@Suppress("unused")
class PortfolioApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        ServiceProvider.firestoreService = AndroidFirestoreService()
        ServiceProvider.connectivityService = AndroidConnectivityService(this)
    }
}