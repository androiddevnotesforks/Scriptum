package sgtmelon.scriptum.repository.room.bind

import android.content.Context
import sgtmelon.scriptum.room.IRoomWork
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
class BindRepo(override val context: Context) : IBindRepo, IRoomWork {

    /**
     * Возвращает пустой список если нет пунктов по данному id
     */
    override fun getRollList(noteId: Long) = ArrayList<RollEntity>().apply {
        inRoom { addAll(iRollDao[noteId]) }
    }

    override fun unbindNote(id: Long): NoteEntity? {
        val noteEntity: NoteEntity?

        openRoom().apply {
            noteEntity = iNoteDao[id]?.apply { isStatus = false }
            noteEntity?.let { iNoteDao.update(it) }
        }.close()

        return noteEntity
    }

}