package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import android.os.Handler
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.screen.callback.notification.AlarmCallback
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
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
    private var color: Int = preference.getDefaultColor()

    private lateinit var noteModel: NoteModel

    private val longWaitHandler = Handler()

    fun onSetup() = callback.setupView(preference.getTheme())

    // TODO Обработка id = -1
    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, preference.getDefaultColor())
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
        }

        longWaitHandler.postDelayed({ callback.finish() }, 15000)

        callback.notifyDataSetChanged(noteModel)
    }


    fun onStart() = with(callback){
        val theme = preference.getTheme()
        startRippleAnimation(theme, context.getAppSimpleColor(color,
                if (theme == Theme.light) ColorShade.ACCENT else ColorShade.DARK
        ))

        startControlFadeAnimation()
    }

    fun onDestroy() = longWaitHandler.removeCallbacksAndMessages(null)

    fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.COLOR, color)
    }

    // TODO убираем уведомление из бд
    fun onClickNote() {
        callback.apply {
            startActivity(with(noteModel.noteItem) { context.getNoteIntent(type, id) })
            finish()
        }
    }

    fun onClickDisable() {
        context.showToast(text = "CLICK DISABLE")
        callback.finish()
    }

    fun onClickPostpone() {
        context.showToast(text = "CLICK POSTPONE")
        callback.finish()
    }

}