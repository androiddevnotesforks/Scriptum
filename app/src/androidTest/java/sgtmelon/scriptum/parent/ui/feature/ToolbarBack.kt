package sgtmelon.scriptum.parent.ui.feature

import sgtmelon.scriptum.parent.ui.parts.toolbar.ToolbarPart

interface ToolbarBack {

    val toolbar: ToolbarPart

    fun clickClose() = toolbar.clickButton()

}