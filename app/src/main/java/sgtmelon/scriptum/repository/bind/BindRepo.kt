package sgtmelon.scriptum.repository.bind

import android.content.Context
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb

/**
 * Репозиторий обработки данных [RoomDb] для работы с закреплением заметки в StatusBar'е
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class BindRepo(private val context: Context) : IBindRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override fun getRollList(noteItem: NoteItem) = ArrayList<RollItem>().apply {
        if (noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply { addAll(getRollDao()[noteItem.id]) }.close()
    }

    override fun unbindNoteItem(id: Long): NoteItem {
        val noteItem: NoteItem

        openRoom().apply {
            noteItem = getNoteDao()[id].apply { isStatus = false }
            getNoteDao().update(noteItem)
        }.close()

        return noteItem
    }

    companion object {
        fun getInstance(context: Context): IBindRepo = BindRepo(context)
    }

}