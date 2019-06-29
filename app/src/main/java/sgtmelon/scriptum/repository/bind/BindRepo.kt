package sgtmelon.scriptum.repository.bind

import android.content.Context
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

    /**
     * Возвращает пустой список если нет пунктов по данному id
     */
    override fun getRollList(noteId: Long) = ArrayList<RollEntity>().apply {
        openRoom().apply { addAll(getRollDao()[noteId]) }.close()
    }

    override fun unbindNote(id: Long): NoteEntity? {
        val noteEntity: NoteEntity?

        openRoom().apply {
            noteEntity = getNoteDao()[id]?.apply { isStatus = false }
            noteEntity?.let { getNoteDao().update(noteEntity) }
        }.close()

        return noteEntity
    }

    companion object {
        fun getInstance(context: Context): IBindRepo = BindRepo(context)
    }

}