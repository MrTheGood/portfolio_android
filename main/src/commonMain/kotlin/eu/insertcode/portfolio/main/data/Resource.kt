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
 * Copyright © 2018 Maarten de Goede. All rights reserved.
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
sealed class Resource<out D, out E>(val data: D?, val error: E?) {
    class Loading<D, E>(data: D? = null, error: E? = null) : Resource<D, E>(data, error)
    class Success<D, E>(data: D, error: E? = null) : Resource<D, E>(data, error)
    class Error<D, E>(error: E, data: D? = null) : Resource<D, E>(data, error)
}

val Resource<Any, Any>?.isError get() = this is Resource.Error<*, *>
val Resource<Any, Any>?.isSuccess get() = this is Resource.Success<*, *>
val Resource<Any, Any>?.isLoading get() = this is Resource.Loading<*, *>

fun <D, E> Resource<D, E>.toLoading(data: D? = this.data, error: E? = this.error) = Resource.Loading(data, error)
fun <D, E> Resource<D, E>.toSuccess(data: D? = this.data, error: E? = this.error) = Resource.Success(data, error)
fun <D, E> Resource<D, E>.toError(error: E? = this.error, data: D? = this.data) = Resource.Error(data, error)