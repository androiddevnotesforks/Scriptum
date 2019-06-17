package sgtmelon.scriptum.repository.bind

import android.content.Context
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Репозиторий обработки данных [RoomDb] для работы с закреплением заметки в StatusBar'е
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class BindRepo(private val context: Context) : IBindRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override fun getRollList(noteEntity: NoteEntity) = ArrayList<RollEntity>().apply {
        if (noteEntity.type != NoteType.ROLL)
            throw ClassCastException("This method only for ROLL type")

        openRoom().apply { addAll(getRollDao()[noteEntity.id]) }.close()
    }

    override fun unbindNote(id: Long): NoteEntity {
        val noteEntity: NoteEntity

        openRoom().apply {
            noteEntity = getNoteDao()[id].apply { isStatus = false }
            getNoteDao().update(noteEntity)
        }.close()

        return noteEntity
    }

    companion object {
        fun getInstance(context: Context): IBindRepo = BindRepo(context)
    }

}