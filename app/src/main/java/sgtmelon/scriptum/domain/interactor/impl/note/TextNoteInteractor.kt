package sgtmelon.scriptum.domain.interactor.impl.note

import sgtmelon.extension.getText
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteBridge
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel
import java.util.*

/**
 * Interactor for [ITextNoteViewModel].
 */
class TextNoteInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val alarmRepo: IAlarmRepo,
        private val rankRepo: IRankRepo,
        private val noteRepo: INoteRepo,
        @RunPrivate var callback: IParentNoteBridge?
) : ParentInteractor(),
        ITextNoteInteractor {

    private var rankIdVisibleList: List<Long>? = null

    private suspend fun getRankIdVisibleList(): List<Long> {
        return rankIdVisibleList ?: rankRepo.getIdVisibleList().also { rankIdVisibleList = it }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override fun getSaveModel(): SaveControl.Model = with(preferenceRepo) {
        return@with SaveControl.Model(pauseSaveOn, autoSaveOn, savePeriod)
    }

    @Theme override val theme: Int get() = preferenceRepo.theme

    @Color override val defaultColor: Int get() = preferenceRepo.defaultColor


    override suspend fun getItem(id: Long): NoteItem.Text? {
        val noteItem = noteRepo.getItem(id, isOptimal = false)

        if (noteItem !is NoteItem.Text) return null

        callback?.notifyNoteBind(noteItem, getRankIdVisibleList(), preferenceRepo.sort)

        return noteItem
    }

    override suspend fun getRankDialogItemArray(emptyName: String): Array<String> {
        return rankRepo.getDialogItemArray(emptyName)
    }


    override suspend fun getRankId(check: Int): Long = rankRepo.getId(check)

    override suspend fun getDateList(): List<String> = alarmRepo.getList().map { it.alarm.date }

    override suspend fun clearDate(noteItem: NoteItem.Text) {
        alarmRepo.delete(noteItem.id)
        callback?.cancelAlarm(noteItem.id)
    }

    override suspend fun setDate(noteItem: NoteItem.Text, calendar: Calendar) {
        alarmRepo.insertOrUpdate(noteItem, calendar.getText())
        callback?.setAlarm(calendar, noteItem.id)
    }

    override suspend fun convertNote(noteItem: NoteItem.Text) {
        noteRepo.convertNote(noteItem)
    }


    override suspend fun restoreNote(noteItem: NoteItem.Text) = noteRepo.restoreNote(noteItem)

    override suspend fun updateNote(noteItem: NoteItem.Text, updateBind: Boolean) {
        noteRepo.updateNote(noteItem)

        if (updateBind) {
            callback?.notifyNoteBind(noteItem, getRankIdVisibleList(), preferenceRepo.sort)
        }
    }

    override suspend fun clearNote(noteItem: NoteItem.Text) = noteRepo.clearNote(noteItem)

    override suspend fun saveNote(noteItem: NoteItem.Text, isCreate: Boolean) {
        noteRepo.saveNote(noteItem, isCreate)
        rankRepo.updateConnection(noteItem)

        callback?.notifyNoteBind(noteItem, getRankIdVisibleList(), preferenceRepo.sort)
    }

    override suspend fun deleteNote(noteItem: NoteItem.Text) {
        noteRepo.deleteNote(noteItem)

        callback?.cancelAlarm(noteItem.id)
        callback?.cancelNoteBind(noteItem.id)
    }

}