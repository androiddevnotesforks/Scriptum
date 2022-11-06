package sgtmelon.scriptum.ui.testing.parent.screen.feature

import sgtmelon.scriptum.ui.testing.parent.screen.toolbar.ToolbarPart

interface ToolbarBack {

    val toolbar: ToolbarPart

    fun clickClose() = toolbar.clickButton()

}