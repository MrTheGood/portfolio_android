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
 * A generic class that holds a value with its state.
 * This allows the possibility to still retain the data, even while it's loading, or after an error occurred.
 * The reasoning behind this is that, during pagination loading, the data still exists while it's loading.
 * Or an error might not actually be a breaking error, but simply a lost connection, which does not mean all data should be thrown away.
 *
 * @param data The data in this [Resource]
 * @param error An error, if any
 * @param D The type of [data] this [Resource] can contain
 * @param E The type of [error] this [Resource] can contain
 */
data class Resource<out D, out E>(val state: State, val data: D?, val error: E?) {
    companion object {
        fun <D, E> loading(data: D? = null, error: E? = null) = Resource(State.LOADING, data, error)
        fun <D, E> notFound(data: D? = null, error: E? = null) = Resource(State.NOT_FOUND, data, error)
        fun <D, E> success(data: D, error: E? = null) = Resource(State.SUCCESS, data, error)
        fun <D, E> error(error: E, data: D? = null) = Resource(State.ERROR, data, error)
    }
}

val Resource<Any, Any>?.isError get() = this?.state == State.ERROR
val Resource<Any, Any>?.isSuccess get() = this?.state == State.SUCCESS
val Resource<Any, Any>?.isNotFound get() = this?.state == State.NOT_FOUND
val Resource<Any, Any>?.isLoading get() = this?.state == State.LOADING

fun <D, E> Resource<D, E>.toLoading(data: D? = this.data, error: E? = this.error) = Resource.loading(data, error)
fun <D, E> Resource<D, E>.toSuccess(data: D, error: E? = this.error) = Resource.success(data, error)
fun <D, E> Resource<D, E>.toError(error: E, data: D? = this.data) = Resource.error(error, data)


/**
 * State of a [Resource].
 *
 * [SUCCESS] indicates that the loading of the data has been successful.
 * [NOT_FOUND] indicates that the resource could not be found or has no data, meaning that it might have been deleted.
 * [ERROR] indicates that an error has occurred. These errors could include, but are not limited to: IOExceptions, timeouts, or anything else.
 * [LOADING] indicates that the data is still loading, or that a reload has occurred due to pagination or a reload click
 */
enum class State {
    SUCCESS,
    NOT_FOUND,
    ERROR,
    LOADING
}