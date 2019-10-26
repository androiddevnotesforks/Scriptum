package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteBridge
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interactor for [TextNoteViewModel]
 */
class TextNoteInteractor(context: Context, private var callback: ITextNoteBridge?) :
        ParentInteractor(context),
        ITextNoteInteractor {

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

        if (updateBind && model != null) callback?.notifyNoteBind(model, rankIdVisibleList)

        return model
    }

    override fun getRankDialogItemArray() = iRoomRepo.getRankDialogItemArray()


    override fun getRankId(check: Int): Long = if (check != NoteEntity.ND_RANK_PS) {
        iRoomRepo.getRankIdList()[check]
    } else {
        NoteEntity.ND_RANK_ID
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

    override fun convert(noteModel: NoteModel) = iRoomRepo.convertToRoll(noteModel)


    override suspend fun restoreNote(noteModel: NoteModel) = iRoomRepo.restoreNote(noteModel)

    override suspend fun updateNote(noteModel: NoteModel, updateBind: Boolean) {
        iRoomRepo.updateNote(noteModel.noteEntity)
        if (updateBind) callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override suspend fun clearNote(noteModel: NoteModel) = iRoomRepo.clearNote(noteModel)

    override fun saveNote(noteModel: NoteModel, isCreate: Boolean) {
        iRoomRepo.saveTextNote(noteModel, isCreate)
        callback?.notifyNoteBind(noteModel, rankIdVisibleList)
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        callback?.cancelAlarm(noteModel.noteEntity.id)
        callback?.cancelNoteBind(noteModel.noteEntity.id.toInt())

        iRoomRepo.deleteNote(noteModel)
    }

}