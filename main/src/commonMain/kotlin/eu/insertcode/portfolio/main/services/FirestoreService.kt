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

package eu.insertcode.portfolio.main.services

import eu.insertcode.portfolio.main.data.*

/**
 * Created by maartendegoede on 2019-09-23.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
expect class FirestoreService {
    fun removeListener(listener: Any)

    // Create

    fun createPath(path: String): String
    fun createPath(reference: Any): String
    fun createReference(path: String): Any
    fun createItem(item: CollectionItem, merge: Boolean, onComplete: (exception: Exception?) -> Unit)
    fun createItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Read

    fun observeDocument(path: String, transform: (FirestoreDocument) -> Item, onResult: (result: Resource<Item?, Exception>) -> Unit): Any
    fun observeCollection(path: String, order: Order? = null, limit: Int? = null, transform: (FirestoreDocument) -> CollectionItem, onResult: (result: Resource<List<CollectionItem>, Exception>) -> Unit): Any
    fun observeCollectionChanges(path: String, onNext: (results: Resource<List<FirestoreDocumentChange>, Exception>) -> Unit): Any

    // Update

    fun updateItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit)
    fun updateItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Delete

    fun deleteItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit)
    fun deleteItemsByPath(paths: List<String>, onComplete: (exception: Exception?) -> Unit)
}