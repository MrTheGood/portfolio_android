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

import com.google.firebase.firestore.*
import eu.insertcode.portfolio.main.data.*

class AndroidFirestoreService : FirestoreService {
    private val firestore
        get() = FirebaseFirestore.getInstance()

    override fun removeListener(listener: Any) {
        if (listener is ListenerRegistration) {
            listener.remove()
        }
    }

    // Create
    override fun createPath(path: String) =
            firestore.collection(path).document().path

    override fun createPath(reference: Reference) =
            (reference as DocumentReference).path

    override fun createReference(path: String): Reference =
            firestore.document(path)

    override fun createItem(
            item: CollectionItem,
            merge: Boolean,
            onComplete: (exception: Exception?) -> Unit
    ) {
        firestore.document(item.path).run {
            if (merge) set(item.data.toFirestoreMap(), SetOptions.merge())
            else set(item.data.toFirestoreMap())
        }.addOnCompleteListener {
            onComplete(it.exception)
        }
    }

    override fun createItems(
            items: List<CollectionItem>,
            onComplete: (exception: Exception?) -> Unit
    ) {
        firestore.batch().run {
            items.forEach { set(firestore.document(it.path), it.data.toFirestoreMap()) }

            commit().addOnCompleteListener {
                onComplete(it.exception)
            }
        }
    }

    // Read
    override fun <T : CollectionItem> observeDocument(
            path: String,
            transform: (FirestoreDocument) -> T,
            onNext: (result: Resource<T, Exception>) -> Unit
    ): FirestoreListener {
        onNext(Resource.loading())
        return FirestoreListener(
                firestore.document(path)
                        .addSnapshotListener { snapshot, error ->
                            error?.let {
                                onNext(Resource.error(error))
                            } ?: snapshot?.data?.let { data ->
                                val item = transform(FirestoreDocument(path, data.toLocalMap()))
                                onNext(Resource.success(item))
                            } ?: onNext(Resource.notFound())
                        }
        )
    }

    override fun <T : CollectionItem> getDocument(
            path: String,
            transform: (FirestoreDocument) -> T,
            onComplete: (result: Resource<T, Exception>) -> Unit
    ) {
        onComplete(Resource.loading())
        firestore.document(path)
                .get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful)
                        onComplete(Resource.error(task.exception ?: IllegalStateException()))
                    else task.result?.data?.let { data ->
                        val item = transform(FirestoreDocument(path, data.toLocalMap()))
                        onComplete(Resource.success(item))
                    } ?: onComplete(Resource.notFound())
                }
    }

    override fun <T : CollectionItem> observeCollection(
            path: String,
            order: Order?,
            limit: Int?,
            equalityQueryParams: Map<String, Any>,
            greaterThanQueryParams: Map<String, Any>,
            lessThanQueryParams: Map<String, Any>,
            transform: (FirestoreDocument) -> T,
            onNext: (result: Resource<List<T>, Exception>) -> Unit
    ): FirestoreListener {
        onNext(Resource.loading())
        return FirestoreListener(
                (firestore.collection(path) as Query)
                        .let { query ->
                            when (order) {
                                is Order.Ascending -> query.orderBy(order.field, Query.Direction.ASCENDING)
                                is Order.Descending -> query.orderBy(order.field, Query.Direction.DESCENDING)
                                else -> query
                            }
                        }
                        .let { query -> limit?.let { query.limit(it.toLong()) } ?: query }
                        .let {
                            var query = it
                            equalityQueryParams.forEach { (key, value) -> query = query.whereEqualTo(key, value.toFirestoreValue()) }
                            greaterThanQueryParams.forEach { (key, value) -> query = query.whereGreaterThan(key, value.toFirestoreValue()) }
                            lessThanQueryParams.forEach { (key, value) -> query = query.whereLessThan(key, value.toFirestoreValue()) }
                            query
                        }
                        .addSnapshotListener { snapshots, error ->
                            error?.let {
                                onNext(Resource.error(error))
                            } ?: snapshots!!.documents.let { firestoreDocuments ->
                                val documents = firestoreDocuments.map { transform(FirestoreDocument(it.reference.path, it.data!!.toLocalMap())) }
                                onNext(Resource.success(documents))
                            }
                        }
        )
    }

    override fun <T : CollectionItem> getCollection(
            path: String,
            order: Order?,
            limit: Int?,
            equalityQueryParams: Map<String, Any>,
            greaterThanQueryParams: Map<String, Any>,
            lessThanQueryParams: Map<String, Any>,
            transform: (FirestoreDocument) -> T,
            onComplete: (result: Resource<List<T>, Exception>) -> Unit
    ) {
        onComplete(Resource.loading())
        (firestore.collection(path) as Query)
                .let { query ->
                    when (order) {
                        is Order.Ascending -> query.orderBy(order.field, Query.Direction.ASCENDING)
                        is Order.Descending -> query.orderBy(order.field, Query.Direction.DESCENDING)
                        else -> query
                    }
                }
                .let { query -> limit?.let { query.limit(it.toLong()) } ?: query }
                .let {
                    var query = it
                    equalityQueryParams.forEach { (key, value) -> query = query.whereEqualTo(key, value.toFirestoreValue()) }
                    greaterThanQueryParams.forEach { (key, value) -> query = query.whereGreaterThan(key, value.toFirestoreValue()) }
                    lessThanQueryParams.forEach { (key, value) -> query = query.whereLessThan(key, value.toFirestoreValue()) }
                    query
                }
                .get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful)
                        onComplete(Resource.error(task.exception ?: IllegalStateException()))
                    else task.result!!.documents.let { firestoreDocuments ->
                        val documents = firestoreDocuments.map { transform(FirestoreDocument(it.reference.path, it.data!!.toLocalMap())) }
                        onComplete(Resource.success(documents))
                    }
                }
    }

    // Update
    override fun updateItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit) {
        firestore.document(item.path)
                .update(item.data.toFirestoreMap())
                .addOnCompleteListener {
                    onComplete(it.exception)
                }
    }

    override fun updateItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit) {
        firestore.batch().run {
            items.forEach { update(firestore.document(it.path), it.data.toFirestoreMap()) }

            commit().addOnCompleteListener {
                onComplete(it.exception)
            }
        }
    }

    // Delete
    override fun deleteItem(path: String, onComplete: (exception: Exception?) -> Unit) {
        firestore.document(path)
                .delete()
                .addOnCompleteListener {
                    onComplete(it.exception)
                }
    }

    override fun deleteItems(paths: List<String>, onComplete: (exception: Exception?) -> Unit) {
        firestore.batch().run {
            paths.forEach { delete(firestore.document(it)) }

            commit().addOnCompleteListener {
                onComplete(it.exception)
            }
        }
    }

    // Util

    private fun MutableData.toLocalMap() =
            mapValues { it.value?.toLocalValue() }.toMutableMap()


    private fun List<*>.toLocalList() =
            map { it?.toLocalValue() }.toMutableList()

    private fun Any.toLocalValue(): Any = when (this) {
        is com.google.firebase.Timestamp -> Timestamp(seconds)
        is List<*> ->
            this.toMutableList().toLocalList()
        is Map<*, *> ->
            @Suppress("UNCHECKED_CAST")
            (this as Map<String, *>).toMutableMap().toLocalMap()
        else -> this
    }

    private fun MutableData.toFirestoreMap(): MutableData =
            mapValues { it.value?.toFirestoreValue() }.toMutableMap()

    private fun List<*>.toFirestoreList(): MutableList<*> =
            map { it?.toFirestoreValue() }.toMutableList()

    private fun Any.toFirestoreValue() = when (this) {
        is Timestamp -> com.google.firebase.Timestamp(seconds, seconds.toInt() * 1000000000)
        is List<*> ->
            this.toMutableList().toFirestoreList()
        is Map<*, *> ->
            @Suppress("UNCHECKED_CAST")
            (this as Map<String, *>).toMutableMap().toFirestoreMap()
        is Item -> data.toFirestoreMap()
        else -> this
    }
}