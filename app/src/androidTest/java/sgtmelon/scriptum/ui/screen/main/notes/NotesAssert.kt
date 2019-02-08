package sgtmelon.scriptum.ui.screen.main.notes

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class NotesAssert : BasicMatch() {

    fun onDisplayContent() { // TODO(Отображение списка/информации от bool)
        onDisplayToolbar(R.id.toolbar, R.string.title_notes)
        onDisplay(R.id.item_preference)
    }

    fun onDisplayList() = onDisplay(R.id.notes_recycler)

    fun doesNotDisplayList() = doesNotDisplay(R.id.notes_recycler)

    fun onDisplayInfo() {
        onDisplay(R.id.info_title_text, R.string.info_notes_title)
        onDisplay(R.id.info_details_text, R.string.info_notes_details)
    }

    fun doesNotDisplayInfo() {
        doesNotDisplay(R.id.info_title_text, R.string.info_notes_title)
        doesNotDisplay(R.id.info_details_text, R.string.info_notes_details)
    }

}