package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.basic.BasicAction

abstract class ParentScreen {

    protected fun action(func: BasicAction.() -> Unit) = BasicAction().apply { func() }

}