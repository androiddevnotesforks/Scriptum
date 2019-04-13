package sgtmelon.scriptum.ui

import sgtmelon.scriptum.ui.basic.BasicAction

/**
 * Родительский класс для доступа к стандартному функционалу ui
 *
 * @author SerjantArbuz
 */
abstract class ParentUi {

    protected fun action(func: BasicAction.() -> Unit) = BasicAction().apply { func() }

    protected fun wait(time: Long, func: () -> Unit = {}) {
        Thread.sleep(time)
        func()
    }

}