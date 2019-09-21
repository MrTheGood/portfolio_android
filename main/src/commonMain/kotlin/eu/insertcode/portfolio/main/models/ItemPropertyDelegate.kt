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

package eu.insertcode.portfolio.main.models

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
class ItemPropertyDelegate<T : Item>(
        private val data: MutableData,
        private val transform: (MutableMap<String, Any?>) -> T
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        val map = (data[property.name] as Map<String, Any?>).toMutableMap()
        return transform(map)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        data[property.name] = value
    }
}