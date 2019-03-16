package sgtmelon.scriptum.app.repository

import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem

/**
 * Интерфейс для общения с [RoomRepo]
 */
interface IRoomRepo {

    fun getNoteModelList(fromBin: Boolean): MutableList<NoteModel>

    fun clearBin()

    fun restoreNoteItem(id: Long)

    fun clearNoteItem(id: Long)

    /**
     *
     */

    fun getRankVisibleList(): List<Long>

    fun getNoteModel(id: Long): NoteModel

    fun getRankDialogName(): Array<String>

    fun getRankCheck(rankId: List<Long>): BooleanArray

    fun convertToRoll(noteItem: NoteItem)

    fun convertToText(noteItem: NoteItem)

    fun getRankId(): Array<Long>

    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    fun insertRank(p: Int, rankItem: RankItem): Long

    fun updateRollCheck(rollItem: RollItem, noteItem: NoteItem)

    fun updateRollCheck(noteItem: NoteItem, isAll: Boolean)

    /**
     *
     */


    /**
     *
     */

    fun updateNoteItemCheck(noteItem: NoteItem, check: Boolean)

    fun updateNoteItemBind(id: Long, status: Boolean)

    fun updateNoteItem(noteItem: NoteItem)

    fun deleteNoteItem(id: Long)

}