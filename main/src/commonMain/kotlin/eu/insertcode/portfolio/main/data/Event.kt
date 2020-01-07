package eu.insertcode.portfolio.main.data

import dev.icerock.moko.mvvm.livedata.MutableLiveData

/**
 * Used as a wrapper for data that is exposed via [dev.icerock.moko.mvvm.livedata.LiveData] that represents an event.
 * Some examples of events are:
 * - Navigation
 * - Toasts
 * - Dialogs
 * - Snackbars
 *
 * Source: https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
open class Event<out T>(
        private val content: T
) {
    val hasBeenHandled = AtomicBoolean(false)

    /**
     * Calls [handleContent] with [content] if the content has not been handled yet.
     *
     * This differs from the original [getContentIfNotHandled] seen in the original source to allow `nullable` [content] values.
     */
    fun getContentIfNotHandled(handleContent: (T) -> Unit) {
        if (!hasBeenHandled.get()) {
            hasBeenHandled.set(true)
            handleContent(content)
        }
    }

    /**
     * Use [getContentIfNotHandled] if the event only needs to be handled once.
     * @return [content], even if it has already been handled.
     */
    fun peekContent() = content
}


/**
 * Use this if your Event does not take any parameters
 */
class VoidEvent : Event<Any?>(null)


/**
 * Extension functions for better readability
 */
fun MutableLiveData<VoidEvent>.newEvent() {
    value = VoidEvent()
}

inline fun <reified T> MutableLiveData<Event<T>>.newEvent(v: T) {
    value = Event(v)
}

inline fun <reified A, reified B> MutableLiveData<Event<Pair<A, B>>>.newEvent(a: A, b: B) {
    value = Event(a to b)
}

inline fun <reified A, reified B, reified C> MutableLiveData<Event<Triple<A, B, C>>>.newEvent(a: A, b: B, c: C) {
    value = Event(Triple(a, b, c))
}