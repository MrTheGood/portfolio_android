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
import eu.insertcode.portfolio.main.data.MutableData
import eu.insertcode.portfolio.main.data.Resource

/**
 * Created by maartendegoede on 2019-09-23.
 * Copyright © 2019 Maarten de Goede. All rights reserved.
 */
interface FirestoreService {
    fun removeListener(listener: Any)

    // Create

    /**
     * This method creates a new Firestore document under the provided collection [path].
     *
     * @param path the collection path where to create the document
     * @return the path for the newly created document
     */
    fun createPath(path: String): String

    /**
     * This method takes a [reference] and returns it's path.
     */
    fun createPath(reference: Reference): String
    fun createReference(path: String): Reference

    /**
     * This method creates a new document.
     *
     * If the [CollectionItem.path] is the same as an existing document, the document will be overwritten entirely
     * Unless [merge] = true, then only the specified fields will be overwritten and all other fields will remain the same.
     *
     * @param onComplete will be executed upon completion. exception will be null on success.
     */
    fun createItem(item: CollectionItem, merge: Boolean, onComplete: (exception: Exception?) -> Unit)
    fun createItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Read

    fun <T : CollectionItem> getDocument(path: String, transform: (FirestoreDocument) -> T, onComplete: (result: Resource<T, Exception>) -> Unit)
    fun <T : CollectionItem> observeDocument(path: String, transform: (FirestoreDocument) -> T, onNext: (result: Resource<T, Exception>) -> Unit): FirestoreListener
    fun <T : CollectionItem> getCollection(path: String, order: Order? = null, limit: Int? = null, equalityQueryParams: Map<String, Any> = emptyMap(), greaterThanQueryParams: Map<String, Any> = mapOf(), lessThanQueryParams: Map<String, Any> = mapOf(), transform: (FirestoreDocument) -> T, onComplete: (results: Resource<List<T>, Exception>) -> Unit)
    fun <T : CollectionItem> observeCollection(path: String, order: Order? = null, limit: Int? = null, equalityQueryParams: Map<String, Any> = emptyMap(), greaterThanQueryParams: Map<String, Any> = mapOf(), lessThanQueryParams: Map<String, Any> = mapOf(), transform: (FirestoreDocument) -> T, onNext: (results: Resource<List<T>, Exception>) -> Unit): FirestoreListener

    // Update

    fun updateItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit)
    fun updateItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit)

    // Delete

    fun deleteItem(path: String, onComplete: (exception: Exception?) -> Unit)
    fun deleteItems(paths: List<String>, onComplete: (exception: Exception?) -> Unit)
}


typealias Reference = Any

class FirestoreListener(private val listener: Any) {
    fun remove() = ServiceProvider.firestoreService.removeListener(listener)
}

data class FirestoreDocument(
        val path: String,
        val data: MutableData = mutableMapOf()
)

sealed class Order {
    data class Ascending(val field: String) : Order()
    data class Descending(val field: String) : Order()
}