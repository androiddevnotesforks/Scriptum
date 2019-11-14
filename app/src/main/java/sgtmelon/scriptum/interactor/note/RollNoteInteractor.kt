package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteBridge
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interactor for [RollNoteViewModel]
 */
class RollNoteInteractor(context: Context, private var callback: IRollNoteBridge?) :
        ParentInteractor(context),
        IRollNoteInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)

    private val rankIdVisibleList: List<Long> = iRankRepo.getIdVisibleList()

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel() = with(iPreferenceRepo) {
        SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor


    override fun isRankEmpty() = iRankRepo.isEmpty()

    override fun getModel(id: Long, updateBind: Boolean): NoteModel? {
        val model = iRoomRepo.getNoteModel(id)

        if (updateBind && model != null) callback?.notifyNoteBind(model, rankIdVisibleList)

        return model
    }

    override fun getRankDialogItemArray() = iRankRepo.getDialogItemArray()


    override fun updateRollCheck(noteModel: NoteModel, rollEntity: RollEntity) {
        iRoomRepo.updateRollCheck(noteModel.noteEntity, rollEntity)
        callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override fun updateRollCheck(noteModel: NoteModel, check: Boolean) {
        iRoomRepo.updateRollCheck(noteModel.noteEntity, check)
        callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override fun getRankId(check: Int): Long = iRankRepo.getId(check)

    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteModel: NoteModel) {
        iAlarmRepo.delete(noteModel.alarmEntity.noteId)
        callback?.cancelAlarm(noteModel.noteEntity.id)
    }

    override suspend fun setDate(noteModel: NoteModel, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteModel.alarmEntity)
        callback?.setAlarm(calendar, noteModel.noteEntity.id)
    }

    override fun convert(noteModel: NoteModel) = iRoomRepo.convertToText(noteModel)


    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun updateNote(noteModel: NoteModel, updateBind: Boolean) {
        iRoomRepo.updateNote(noteModel.noteEntity)

        if (updateBind) callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

    override fun saveNote(noteModel: NoteModel, isCreate: Boolean) {
        iRoomRepo.saveRollNote(noteModel, isCreate)
        iRankRepo.updateConnection(noteModel)

        callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        iRoomRepo.deleteNote(noteModel)

        callback?.cancelAlarm(noteModel.noteEntity.id)
        callback?.cancelNoteBind(noteModel.noteEntity.id.toInt())
    }

}