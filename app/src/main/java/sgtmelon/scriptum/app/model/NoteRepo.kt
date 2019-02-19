package sgtmelon.scriptum.app.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.office.annot.DbAnn
import sgtmelon.scriptum.office.annot.def.CheckDef

/**
 * Репозиторий заметки
 */
class NoteRepo(@field:Embedded var noteItem: NoteItem,
               @field:Relation(parentColumn = DbAnn.Note.ID, entityColumn = DbAnn.Roll.NOTE_ID) var listRoll: MutableList<RollItem>,
               @field:Ignore var statusItem: StatusItem) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(@CheckDef rollCheck: Int) = listRoll.forEach { it.isCheck = rollCheck == CheckDef.done }

    fun updateStatus(status: Boolean) = when (status) {
        true -> statusItem.notifyNote()
        false -> statusItem.cancelNote()
    }

}