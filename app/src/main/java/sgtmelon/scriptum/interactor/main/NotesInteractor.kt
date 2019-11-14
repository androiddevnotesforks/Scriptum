package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.main.INotesBridge
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interactor for [NotesViewModel]
 */
class NotesInteractor(context: Context, private var callback: INotesBridge?) :
        ParentInteractor(context),
        INotesInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iBindRepo: IBindRepo = BindRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)


    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = false)

    override fun isListHide() = iRoomRepo.isListHide(bin = false)

    override fun updateNote(noteEntity: NoteEntity) {
        iRoomRepo.updateNote(noteEntity)

        val noteModel = NoteModel(noteEntity, iBindRepo.getRollList(noteEntity.id))
        callback?.notifyNoteBind(noteModel, iRankRepo.getIdVisibleList())
    }

    override fun convert(noteModel: NoteModel): NoteModel {
        when (noteModel.noteEntity.type) {
            NoteType.TEXT -> iRoomRepo.convertToRoll(noteModel)
            NoteType.ROLL -> iRoomRepo.convertToText(noteModel)
        }

        callback?.notifyNoteBind(noteModel, iRankRepo.getIdVisibleList())

        /**
         * Optimisation for get only first 4 items
         */
        if (noteModel.rollList.size > 4) {
            noteModel.rollList.dropLast(n = noteModel.rollList.size - 4)
        }

        return noteModel
    }


    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteModel: NoteModel) {
        iAlarmRepo.delete(noteModel.alarmEntity.noteId)
        callback?.cancelAlarm(noteModel.noteEntity.id)
    }

    override suspend fun setDate(noteModel: NoteModel, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteModel.alarmEntity)
        callback?.setAlarm(calendar, noteModel.noteEntity.id)
    }


    override suspend fun copy(noteEntity: NoteEntity) {
        callback?.copyClipboard(iRoomRepo.getCopyText(noteEntity))
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        iRoomRepo.deleteNote(noteModel)

        callback?.cancelAlarm(noteModel.noteEntity.id)
        callback?.cancelNoteBind(noteModel.noteEntity.id.toInt())
    }


    override suspend fun getAlarm(id: Long) = iAlarmRepo.get(id)

}