package sgtmelon.scriptum.ui.screen.main.notes

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch

class NotesAssert : BasicMatch() {

    fun onDisplayContent(count: Int) {
        onDisplay(R.id.notes_parent_container)

        onDisplayToolbar(R.id.toolbar_container, R.string.title_notes)
        onDisplay(R.id.item_preference)

        if (count == 0) {
            onDisplay(R.id.info_title_text, R.string.info_notes_title)
            onDisplay(R.id.info_details_text, R.string.info_notes_details)
            doesNotDisplay(R.id.notes_recycler)
        } else {
            doesNotDisplay(R.id.info_title_text, R.string.info_notes_title)
            doesNotDisplay(R.id.info_details_text, R.string.info_notes_details)
            onDisplay(R.id.notes_recycler)
        }
    }

}