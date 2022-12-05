package sgtmelon.test.idling.impl

import androidx.test.espresso.IdlingRegistry
import sgtmelon.test.idling.BuildConfig
import sgtmelon.test.idling.callback.AppIdlingCallback
import timber.log.Timber

/**
 * Class for maintain test work while app is freeze without Thread.sleep(...).
 *
 * Tag - used for detect where work was running.
 */
class AppIdlingResource : ParentIdlingResource(), AppIdlingCallback {

    /**
     * isEmpty    - don't need wait operation end.
     * isNotEmpty - otherwise need wait.
     *
     * List created for cases when [start] calls from different places in one time.
     * And [stop] may come not at the same time.
     */
    private val idleList = mutableListOf<String>()

    override fun getName(): String = TAG

    override fun isIdleNow() = idleList.isEmpty()

    override fun start(tag: String) {
        if (!BuildConfig.DEBUG) return

        idleList.add(tag)
    }

    override fun stop(tag: String) {
        if (!BuildConfig.DEBUG) return

        val index = idleList.indexOfLast { it == tag }
        if (index in idleList.indices) {
            idleList.removeAt(index)
        } else {
            Timber.e(message = "Not found idling tag: $tag")
        }

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }

    override fun change(isWork: Boolean, tag: String) {
        if (isWork) start(tag) else stop(tag)
    }

    override fun unregister() {
        Timber.i(idleList.joinToString())

        idleList.clear()

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }

        IdlingRegistry.getInstance().unregister(this)
        worker = null
    }

    companion object {
        private val TAG = AppIdlingResource::class.java.simpleName

        private var worker: AppIdlingCallback? = null

        fun getInstance(): AppIdlingCallback {
            return worker ?: AppIdlingResource().also { worker = it }
        }
    }
}