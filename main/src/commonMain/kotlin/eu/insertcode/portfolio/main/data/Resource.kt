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

package eu.insertcode.portfolio.main.data

/**
 * Created by maartendegoede on 23/08/2018.
 * Copyright © 2020 Maarten de Goede. All rights reserved.
 * Based on https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
 *
 * A generic class that holds a value with its state, and whether or not
 * This allows the possibility to still retain the data, even while it's reloading.
 *
 * @param data The data of this Resource
 * @param error An error, if any
 * @param D Data type
 * @param E Error type
 */
data class Resource<out D, out E>(val state: State, val data: D?, val error: E?) {
    companion object {
        fun <D, E> loading(data: D? = null, error: E? = null) = Resource(State.LOADING, data, error)
        fun <D, E> success(data: D, error: E? = null) = Resource(State.SUCCESS, data, error)
        fun <D, E> error(error: E, data: D? = null) = Resource(State.ERROR, data, error)
    }
}

val Resource<Any, Any>?.isError get() = this?.state == State.ERROR
val Resource<Any, Any>?.isSuccess get() = this?.state == State.SUCCESS
val Resource<Any, Any>?.isLoading get() = this?.state == State.LOADING

fun <D, E> Resource<D, E>.toLoading(data: D? = this.data, error: E? = this.error) = Resource.loading(data, error)
fun <D, E> Resource<D, E>.toSuccess(data: D? = this.data, error: E? = this.error) = Resource.success(data, error)
fun <D, E> Resource<D, E>.toError(error: E? = this.error, data: D? = this.data) = Resource.error(error, data)


/**
 * State of a resource that is provided to the UI.
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */
enum class State {
    SUCCESS,
    ERROR,
    LOADING
}