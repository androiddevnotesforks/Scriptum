package sgtmelon.scriptum.parent.ui.screen.parent.feature

import sgtmelon.scriptum.parent.ui.screen.parent.toolbar.ToolbarPart

interface ToolbarBack {

    val toolbar: ToolbarPart

    fun clickClose() = toolbar.clickButton()

}