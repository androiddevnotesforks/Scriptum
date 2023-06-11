package sgtmelon.scriptum.source.ui.feature

import sgtmelon.scriptum.source.ui.parts.toolbar.ToolbarPart

interface ToolbarBack {

    val toolbar: ToolbarPart

    fun clickClose() = toolbar.clickButton()

}