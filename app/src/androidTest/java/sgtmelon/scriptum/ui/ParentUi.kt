package sgtmelon.scriptum.ui

import sgtmelon.scriptum.ui.basic.BasicAction

abstract class ParentUi {

    protected fun action(func: BasicAction.() -> Unit) = BasicAction().apply { func() }

}