package sgtmelon.scriptum.ui

import sgtmelon.scriptum.ui.basic.BasicAction

/**
 * Родительский класс для доступа к стандартному функционалу ui
 *
 * @author SerjantArbuz
 */
abstract class ParentUi {

    protected fun action(func: BasicAction.() -> Unit) = BasicAction().apply { func() }

    protected fun waitBefore(time: Long, func: () -> Unit = {}) {
        Thread.sleep(time)
        func()
    }

    protected fun waitAfter(time: Long, func: () -> Unit = {}) {
        func()
        Thread.sleep(time)
    }

}