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

package eu.insertcode.portfolio.main.data.models

import eu.insertcode.portfolio.main.data.CollectionItem
import eu.insertcode.portfolio.main.data.ItemPropertyDelegate
import eu.insertcode.portfolio.main.data.MutableData
import eu.insertcode.portfolio.main.data.models.ProjectType.OTHER
import eu.insertcode.portfolio.main.services.FirestoreDocument

/**
 * Created by maartendegoede on 2019-09-21.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
class Project(
        override val path: String,
        override var data: MutableData
) : CollectionItem() {

    val date: String? by data.withDefault { null }
    val description: String? by data.withDefault { null }
    val images: List<String> by data.withDefault { emptyList<String>() }
    val importance: Int by data.withDefault { 0 }
    val links by ItemPropertyDelegate(data) { ProjectLinks(it) }
    val tags: List<String> by data.withDefault { emptyList<String>() }
    val title: String? by data.withDefault { null }
    val type: String by data.withDefault { OTHER.type }

    constructor(document: FirestoreDocument) : this(document.path, document.data)
}