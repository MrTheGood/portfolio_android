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

package eu.insertcode.portfolio.data

/**
 * Created by maartendegoede on 23/08/2018.
 * Copyright © 2018 insertCode.eu. All rights reserved.
 * Based on https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
 *
 * A generic class that holds a value with its loading status.
 * @param state the current [State] of the Resource
 * @param data The data of this Resource
 * @param error An error, if any
 * @param D Data type
 * @param E Error type
 */
data class Resource<out D, out E>(val state: State, val data: D?, val error: E?) {

    val isError get() = state == State.ERROR
    val isSuccess get() = state == State.SUCCESS
    val isLoading get() = state == State.LOADING

    companion object {
        fun <D, E> loading(data: D? = null, error: E? = null) = Resource(State.LOADING, data, error)
        fun <D, E> success(data: D, error: E? = null) = Resource(State.SUCCESS, data, error)
        fun <D, E> error(error: E, data: D? = null) = Resource(State.ERROR, data, error)
    }
}

val Resource<Any, Any>?.isNull get() = this == null
val Resource<Any, Any>?.isError get() = this?.isError == true
val Resource<Any, Any>?.isSuccess get() = this?.isSuccess == true
val Resource<Any, Any>?.isLoading get() = this?.isLoading == true
