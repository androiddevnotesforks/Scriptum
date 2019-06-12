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
    private var color: Int = iPreferenceRepo.defaultColor

    private lateinit var noteModel: NoteModel

    private val longWaitHandler = Handler()

    fun onSetup() = callback.setupView(iPreferenceRepo.theme)

    // TODO #RELEASE Обработка id = -1
    // TODO #RELEASE Убирать уведомление из бд при старте (чтобы не было индикатора на заметке) и потом уже обрабатывать остановку приложения, нажатие на кнопки
    fun onSetupData(bundle: Bundle?) {
        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
        }

        longWaitHandler.postDelayed({ callback.finish() }, 15000)

        callback.notifyDataSetChanged(noteModel)
    }


    fun onStart() = with(callback){
        val theme = iPreferenceRepo.theme
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