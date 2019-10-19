package sgtmelon.scriptum.repository.bind

import android.content.Context
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Repository of [RoomDb] which work with notes bind in status bar
 *
 * @param context for open [RoomDb]
 */
class BindRepo(override val context: Context) : IBindRepo, IRoomWork {

    /**
     * TODO Правилоьное положение заметок
     * Не получает информацию о будильнике, потому что она не нужна при биндинге
     */
    override fun getNoteList(): List<NoteModel> = ArrayList<NoteModel>().apply {
        inRoom {
            iNoteDao.getByChange(bin = false).forEach {
                add(NoteModel(it, iRollDao.getView(it.id), AlarmEntity(noteId = it.id)))
            }
        }
    }

    /**
     * Return empty list if don't have [RollEntity] for this [noteId]
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