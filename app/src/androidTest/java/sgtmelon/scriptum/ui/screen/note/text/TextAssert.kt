package sgtmelon.scriptum.ui.screen.note.text

import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.panel.NotePanelUi
import sgtmelon.scriptum.ui.widget.panel.STATE

class TextAssert : BasicMatch() {

    fun onDisplayContent(state: STATE) { // TODO добавить наполнение в зависимости от открывающегося экрана заметки (ENUM)
        NotePanelUi { assert { onDisplayContent(state) } }
    }

}