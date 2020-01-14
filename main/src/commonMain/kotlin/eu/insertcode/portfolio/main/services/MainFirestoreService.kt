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

import eu.insertcode.portfolio.main.data.CollectionItem
import eu.insertcode.portfolio.main.data.Item
import eu.insertcode.portfolio.main.data.MutableData
import eu.insertcode.portfolio.main.data.Resource

/**
 * Created by maartendegoede on 2019-09-23.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
interface MainFirestoreService {
    fun removeListener(listener: Any)

    // Create

    fun createPath(path: String): String
    fun createPath(reference: Any): String
    fun createReference(path: String): Any
    fun createItem(item: CollectionItem, merge: Boolean, onComplete: (exception: Exception?) -> Unit)
    fun createItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Read

    fun <T : Item> getDocument(path: String, transform: (FirestoreDocument) -> T, onComplete: (result: Resource<T?, Exception>) -> Unit)
    fun <T : Item> observeDocument(path: String, transform: (FirestoreDocument) -> T, onNext: (result: Resource<T?, Exception>) -> Unit): Any
    fun <T : CollectionItem> getCollection(path: String, order: Order? = null, limit: Int? = null, equalityQueryParams: Map<String, Any> = emptyMap(),transform: (FirestoreDocument) -> T, onComplete: (result: Resource<List<T>, Exception>) -> Unit)
    fun <T : CollectionItem> observeCollection(path: String, order: Order? = null, limit: Int? = null, equalityQueryParams: Map<String, Any> = emptyMap(), transform: (FirestoreDocument) -> T, onNext: (result: Resource<List<T>, Exception>) -> Unit): Any

    // Update

    fun updateItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit)
    fun updateItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Delete

    fun deleteItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit)
    fun deleteItemsByPath(paths: List<String>, onComplete: (exception: Exception?) -> Unit)
}


data class FirestoreDocument(
        val path: String,
        val data: MutableData = mutableMapOf()
)

sealed class Order {
    data class Ascending(val field: String) : Order()
    data class Descending(val field: String) : Order()
}