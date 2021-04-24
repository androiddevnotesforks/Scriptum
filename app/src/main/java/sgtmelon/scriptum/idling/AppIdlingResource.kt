package sgtmelon.scriptum.idling

import android.util.Log
import androidx.test.espresso.IdlingRegistry
import sgtmelon.scriptum.extension.validIndexOf

/**
 * Class for maintain test work while app is freeze without Thread.sleep(...)
 */
class AppIdlingResource : ParentIdlingResource(), AppIdlingCallback {

    /**
     * isEmpty - не надо ждать операции, isNotEmpty - надо ждать окончание операции
     *
     * Список сделан для тех случаев, когда происходит параллельный запрос к сервера. И ответ
     * может прийти раньше другого.
     */
    private val idleList = mutableListOf<String>()

    override fun getName(): String = TAG

    override fun isIdleNow() = idleList.isEmpty()

    override fun startWork(@IdlingTag tag: String) {
        idleList.add(tag)
    }

    override fun stopWork(@IdlingTag tag: String) {
        val index = idleList.validIndexOf { it == tag }
        if (index != null) {
            idleList.removeAt(index)
        }

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }

    override fun changeWork(isWork: Boolean, @IdlingTag tag: String) {
        if (isWork) startWork(tag) else stopWork(tag)
    }

    override fun register() {
        IdlingRegistry.getInstance().register(this)
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