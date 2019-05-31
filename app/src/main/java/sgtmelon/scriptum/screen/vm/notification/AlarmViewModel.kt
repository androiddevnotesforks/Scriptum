package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.office.utils.showToast
import sgtmelon.scriptum.screen.callback.notification.AlarmCallback
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: AlarmCallback

    private var id: Long = NoteData.Default.ID
    private var color: Int = preference.defaultColor

    private lateinit var noteModel: NoteModel

    // TODO Обработка id = -1
    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, preference.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
        }

        callback.finishOnLong(millis = 5000)
        callback.setupNote(noteModel)
        callback.showControl()
    }

    fun onSaveData(bundle: Bundle) = with(bundle) { putLong(NoteData.Intent.ID, id) }

    fun onClickNote() = beforeFinish { context.showToast(text = "CLICK NOTE") }

    fun onClickDisable() = beforeFinish { context.showToast(text = "CLICK DISABLE") }

    fun onClickPostpone() = beforeFinish { context.showToast(text = "CLICK POSTPONE") }

    private fun beforeFinish(func: () -> Unit) {
        func()
        callback.finishAlarm()
    }

}