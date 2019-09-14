package sgtmelon.scriptum.interactor.main.notes

import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Interactor for [NotesViewModel]
 */
class NotesInteractor(private val context: Context) : ParentInteractor(context), INotesInteractor {

    private val iBindRepo: IBindRepo = BindRepo(context)

    @Theme override val theme: Int get() = iPreferenceRepo.theme

    override fun getList() = iRoomRepo.getNoteModelList(bin = false)

    override fun isListHide() = iRoomRepo.isListHide(bin = false)

    override fun updateNote(noteEntity: NoteEntity) {
        iRoomRepo.updateNote(noteEntity)

        BindControl(context).setup(noteEntity, iBindRepo.getRollList(noteEntity.id)).updateBind()
    }

    override fun convert(noteModel: NoteModel): NoteModel {
        when (noteModel.noteEntity.type) {
            NoteType.TEXT -> iRoomRepo.convertToRoll(noteModel)
            NoteType.ROLL -> {
                iRoomRepo.convertToText(noteModel)

                if (noteModel.rollList.size > 4) {
                    noteModel.rollList.dropLast(n = noteModel.rollList.size - 4)
                }
            }
        }

        BindControl(context).setup(noteModel).updateBind()

        return noteModel
    }

    override suspend fun deleteNote(noteModel: NoteModel, callback: AlarmCallback.Cancel?) {
        iRoomRepo.deleteNote(noteModel)

        BindControl(context).setup(noteModel).cancelBind()
        callback?.cancelAlarm(AlarmReceiver[noteModel.noteEntity])
    }

}