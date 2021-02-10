package sgtmelon.scriptum.idling

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import sgtmelon.scriptum.extension.validIndexOf

/**
 * Class for maintain test work while app is freeze without Thread.sleep(...)
 */
class AppIdlingResource : IdlingResource, AppIdlingCallback {

    /**
     * isEmpty - не надо ждать операции, isNotEmpty - надо ждать окончание операции
     *
     * Список сделан для тех случаев, когда происходит параллельный запрос к сервера. И ответ
     * может прийти раньше другого.
     */
    private val idleList = mutableListOf<String>()

    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = AppIdlingResource::class.java.simpleName

    override fun isIdleNow() = idleList.isEmpty()

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    override fun startHardWork(@IdlingTag tag: String) {
        idleList.add(tag)
    }

    override fun stopHardWork(@IdlingTag tag: String) {
        val index = idleList.validIndexOf { it == tag }
        if (index != null) {
            idleList.removeAt(index)
        }

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }

    override fun clearWork() {
        idleList.clear()

        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }
    }

    companion object {

        val worker by lazy {
            AppIdlingResource().apply {
                IdlingRegistry.getInstance().register(this)
            }
        }
    }
}