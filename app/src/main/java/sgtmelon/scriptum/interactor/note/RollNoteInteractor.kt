package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.room.entity.NoteEntity
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

    private val rankIdVisibleList: List<Long> = iRoomRepo.getRankIdVisibleList()

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel() = with(iPreferenceRepo) {
        SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    @Color override val defaultColor: Int get() = iPreferenceRepo.defaultColor


    override fun isRankEmpty() = iRoomRepo.isRankEmpty()

    override fun getModel(id: Long, updateBind: Boolean): NoteModel? {
        val model = iRoomRepo.getNoteModel(id)

        if (updateBind && model != null) callback?.notifyBind(model, rankIdVisibleList)

        return model
    }

    override fun getRankDialogItemArray() = iRoomRepo.getRankDialogItemArray()


    override fun updateRollCheck(noteModel: NoteModel, rollEntity: RollEntity) {
        iRoomRepo.updateRollCheck(noteModel.noteEntity, rollEntity)
        callback?.notifyBind(noteModel, rankIdVisibleList)
    }

    override fun updateRollCheck(noteModel: NoteModel, check: Boolean) {
        iRoomRepo.updateRollCheck(noteModel.noteEntity, check)
        callback?.notifyBind(noteModel, rankIdVisibleList)
    }

    override fun getRankId(check: Int): Long = if (check != NoteEntity.ND_RANK_PS) {
        iRoomRepo.getRankIdList()[check]
    } else {
        NoteEntity.ND_RANK_ID
    }

    override suspend fun getDateList() = iAlarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteModel: NoteModel) {
        iAlarmRepo.delete(noteModel.alarmEntity.noteId)
        callback?.cancelAlarm(AlarmReceiver[noteModel.noteEntity])
    }

    override suspend fun setDate(noteModel: NoteModel, calendar: Calendar) {
        iAlarmRepo.insertOrUpdate(noteModel.alarmEntity)
        callback?.setAlarm(calendar, AlarmReceiver[noteModel.noteEntity])
    }

    override fun convert(noteModel: NoteModel) = iRoomRepo.convertToText(noteModel)


    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun updateNote(noteModel: NoteModel, updateBind: Boolean) {
        iRoomRepo.updateNote(noteModel.noteEntity)
        if (updateBind) callback?.notifyBind(noteModel, rankIdVisibleList)
    }

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

    override fun saveNote(noteModel: NoteModel, isCreate: Boolean) {
        iRoomRepo.saveRollNote(noteModel, isCreate)
        callback?.notifyBind(noteModel, rankIdVisibleList)
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        callback?.cancelAlarm(AlarmReceiver[noteModel.noteEntity])
        callback?.cancelBind(noteModel.noteEntity.id.toInt())

        iRoomRepo.deleteNote(noteModel)
    }

}