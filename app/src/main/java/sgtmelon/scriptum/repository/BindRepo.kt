package sgtmelon.scriptum.repository

import android.content.Context
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb

/**
 * Репозиторий обработки данных [RoomDb] для [BindControl]
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class BindRepo(private val context: Context) : IBindRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override fun getNoteItem(id: Long): NoteItem {
        val noteItem: NoteItem

        openRoom().apply { noteItem = getNoteDao()[id] }.close()

        return noteItem
    }

    override fun getRollList(noteItem: NoteItem) = ArrayList<RollItem>().apply {
        if (noteItem.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply { addAll(getRollDao()[noteItem.id]) }.close()
    }

    companion object {
        fun getInstance(context: Context): IBindRepo = BindRepo(context)
    }

}