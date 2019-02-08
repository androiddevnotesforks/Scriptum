package sgtmelon.scriptum.ui.screen.note.roll

import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.panel.STATE

class RollAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) { // TODO добавить наполнение в зависимости от открывающегося экрана заметки (ENUM)
        NotePanelUi { assert { onDisplayContent(state) } }
    }

}