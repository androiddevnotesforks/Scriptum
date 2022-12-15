package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.record

/**
 * Class for record (send to firebase) custom exceptions.
 */
class NoteExceptionRecorder {

    fun onCreate(id: Long?, type: NoteType?, color: Color?) {
        val description = "Null values on create: " +
                "id=${id == null}, type=${type == null}, " +
                "color=${color == null}"
        NullPointerException(description).record()
    }

    fun showFragment(isEdit: Boolean?, noteState: NoteState?) {
        val description = "Null values on fragment display: " +
                "isEdit=${isEdit == null}, noteState=${noteState == null}"
        NullPointerException(description).record()
    }

    fun convertNote(id: Long?, type: NoteType?, color: Color?, newType: NoteType?) {
        val description = "Null values on convert: " +
                "id=${id == null}, type=${type == null}, " +
                "color=${color == null}, newType=${newType == null}"
        NullPointerException(description).record()
    }
}