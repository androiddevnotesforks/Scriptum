package sgtmelon.test.idling.impl

import android.util.Log
import androidx.test.espresso.IdlingRegistry
import sgtmelon.test.idling.BuildConfig
import sgtmelon.test.idling.callback.AppIdlingCallback

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
     * List created for cases when [startWork] calls from different places in one time.
     * And [stopWork] may come not at the same time.
     */
    private val idleList = mutableListOf<String>()

    override fun getName(): String = TAG

    override fun isIdleNow() = idleList.isEmpty()

    override fun startWork(tag: String) {
        if (!BuildConfig.DEBUG) return

        idleList.add(tag)
    }

    override fun stopWork(tag: String) {
        if (!BuildConfig.DEBUG) return

        val index = idleList.indexOfFirst { it == tag }
        if (index in idleList.indices) {
            idleList.removeAt(index)
        }

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }

    override fun changeWork(isWork: Boolean, tag: String) {
        if (isWork) startWork(tag) else stopWork(tag)
    }

    override fun unregister() {
        Log.i(TAG, idleList.joinToString())

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