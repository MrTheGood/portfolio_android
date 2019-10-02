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

actual class FirestoreService {
    private val firestore
        get() = FirebaseFirestore.getInstance()

    actual fun removeListener(listener: Any) {
        if (listener is ListenerRegistration) {
            listener.remove()
        }
    }

    // Create
    actual fun createPath(path: String) =
            firestore.collection(path).document().path

    actual fun createPath(reference: Any) =
            (reference as DocumentReference).path

    actual fun createReference(path: String): Any =
            firestore.document(path)

    actual fun createItem(
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

    actual fun createItems(
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
    actual fun observeDocument(
            path: String,
            transform: (FirestoreDocument) -> Item,
            onResult: (result: Resource<Item?, Exception>) -> Unit
    ): Any = firestore.document(path)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    onResult(Resource.Error(error))
                } ?: snapshot?.data?.let {
                    val item = snapshot.data.run { transform(FirestoreDocument(path, it)) }
                    onResult(Resource.Success(item))
                } ?: onResult(Resource.Success(null))
            }

    actual fun observeCollection(
            path: String,
            order: Order?,
            limit: Int?,
            transform: (FirestoreDocument) -> CollectionItem,
            onResult: (result: Resource<List<CollectionItem>, Exception>) -> Unit
    ): Any = (firestore.collection(path) as Query)
            .let { query ->
                when (order) {
                    is Order.Ascending -> query.orderBy(order.field, Query.Direction.ASCENDING)
                    is Order.Descending -> query.orderBy(order.field, Query.Direction.DESCENDING)
                    else -> query
                }
            }
            .let { query -> limit?.let { query.limit(it.toLong()) } ?: query }
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    onResult(Resource.Error(error))
                } ?: snapshot?.documents?.let { firestoreDocuments ->
                    val documents = firestoreDocuments.map { transform(FirestoreDocument(it.reference.path, it.data!!.toLocalMap())) }
                    onResult(Resource.Success(documents))
                } ?: onResult(Resource.Success(emptyList()))
            }

    actual fun observeCollectionChanges(
            path: String,
            onNext: (results: Resource<List<FirestoreDocumentChange>, Exception>) -> Unit
    ): Any = firestore.collection(path)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    onNext(Resource.Error(error))
                } ?: snapshot?.documentChanges?.let { documents ->
                    onNext(Resource.Success(documents.map { firestoreDocumentChange(it) }))
                } ?: onNext(Resource.Success(emptyList()))
            }

    // Update
    actual fun updateItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit) {
        firestore.document(item.path)
                .update(item.data.toFirestoreMap())
                .addOnCompleteListener {
                    onComplete(it.exception)
                }
    }

    actual fun updateItems(items: List<CollectionItem>, onComplete: (exception: Exception?) -> Unit) {
        firestore.batch().run {
            items.forEach { update(firestore.document(it.path), it.data.toFirestoreMap()) }

            commit().addOnCompleteListener {
                onComplete(it.exception)
            }
        }
    }

    // Delete
    actual fun deleteItem(item: CollectionItem, onComplete: (exception: Exception?) -> Unit) {
        firestore.document(item.path)
                .delete()
                .addOnCompleteListener {
                    onComplete(it.exception)
                }
    }

    actual fun deleteItemsByPath(paths: List<String>, onComplete: (exception: Exception?) -> Unit) {
        firestore.batch().run {
            paths.forEach { delete(firestore.document(it)) }

            commit().addOnCompleteListener {
                onComplete(it.exception)
            }
        }
    }

    // Util

    private fun firestoreDocumentChange(documentChange: DocumentChange) =
            FirestoreDocumentChange(
                    firestoreDocumentChangeType(documentChange.type),
                    FirestoreDocument(
                            documentChange.document.reference.path,
                            documentChange.document.data.toLocalMap()
                    )
            )

    private fun firestoreDocumentChangeType(documentChangeType: DocumentChange.Type) =
            when (documentChangeType) {
                DocumentChange.Type.ADDED -> FirestoreDocumentChangeType.ADDED
                DocumentChange.Type.MODIFIED -> FirestoreDocumentChangeType.MODIFIED
                DocumentChange.Type.REMOVED -> FirestoreDocumentChangeType.REMOVED
            }

    private fun MutableData.toLocalMap() =
            mapValues { it.value?.toLocalValue() }.toMutableMap()

    private fun Any.toLocalValue(): Any = when (this) {
        is com.google.firebase.Timestamp -> Timestamp(seconds)
        is Map<*, *> ->
            @Suppress("UNCHECKED_CAST")
            (this as Map<String, *>).toMutableMap().toLocalMap()
        else -> this
    }

    private fun MutableData.toFirestoreMap(): MutableData {
        return mapValues { it.value?.toFirestoreValue() }.toMutableMap()
    }

    private fun List<Any>.toFirestoreList(): List<Any> {
        return map { it.toFirestoreValue() }
    }

    private fun Any.toFirestoreValue() = when (this) {
        is Timestamp -> com.google.firebase.Timestamp(seconds, seconds.toInt() * 1000000000)
        is Map<*, *> ->
            @Suppress("UNCHECKED_CAST")
            (this as Map<String, *>).toMutableMap().toFirestoreMap()
        is List<*> ->
            @Suppress("UNCHECKED_CAST")
            (this as List<Any>).toFirestoreList()
        is Item -> data.toFirestoreMap()
        else -> this
    }
}