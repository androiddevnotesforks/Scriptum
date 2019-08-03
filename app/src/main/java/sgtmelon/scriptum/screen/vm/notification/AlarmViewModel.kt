package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import android.os.Handler
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel

/**
 * ViewModel for [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmViewModel(application: Application) : ParentViewModel<IAlarmActivity>(application),
        IAlarmViewModel {

    private var id: Long = NoteData.Default.ID
    private var color: Int = iPreferenceRepo.defaultColor

    private lateinit var noteModel: NoteModel
    private lateinit var signalState: SignalState

    private val vibrationHandler = Handler()
    private val vibrationRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibrationPattern)
            vibrationHandler.postDelayed(this, vibrationPattern.sum())
        }
    }

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { callback?.finish() }

    private var needRepeat = true

    // TODO #RELEASE Обработка id = -1
    // TODO #RELEASE Убирать уведомление из бд при старте (чтобы не было индикатора на заметке) и потом уже обрабатывать остановку приложения, нажатие на кнопки
    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupView(iPreferenceRepo.theme)
            setupMelodyPlayer(iPreferenceRepo.melodyUri.toUri())
        }

        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            // TODO убрать уведомление из бд

            noteModel = iRoomRepo.getNoteModel(id)
            signalState = iPreferenceRepo.signalState
        }

        callback?.notifyDataSetChanged(noteModel)
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.COLOR, color)
    }

    override fun onStart() {
        callback?.let {
            val theme = iPreferenceRepo.theme
            it.startRippleAnimation(theme, context.getAppSimpleColor(color,
                    if (theme == Theme.LIGHT) ColorShade.ACCENT else ColorShade.DARK
            ))

            it.startButtonFadeInAnimation()
        }

        if (signalState.isMelody) {
            callback?.melodyPlayerStart()
        }

        if (signalState.isVibration) {
            vibrationHandler.postDelayed(vibrationRunnable, START_DELAY)
        }

        if (signalState.isLight) {
            // TODO
        }

        longWaitHandler.postDelayed(longWaitRunnable, CANCEL_DELAY)
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        if (signalState.isMelody) {
            callback?.melodyPlayerStop()
        }

        if (signalState.isVibration) {
            callback?.vibrateCancel()
            vibrationHandler.removeCallbacks(vibrationRunnable)
        }

        if (signalState.isLight) {
            // TODO
        }

        longWaitHandler.removeCallbacks(longWaitRunnable)

        if (needRepeat) {
            // TODO запись в бд и установка alarm
        }
    }

    override fun onClickNote() {
        needRepeat = false

        val noteEntity = noteModel.noteEntity

        callback?.apply {
            startActivity(NoteActivity.getInstance(context, noteEntity.type, noteEntity.id))
            finish()
        }
    }

    override fun onClickDisable() {
        needRepeat = false

        context.showToast(text = "CLICK DISABLE")
        callback?.finish()
    }

    override fun onClickPostpone() {
        context.showToast(text = "CLICK POSTPONE")
        callback?.finish()
    }

    companion object {
        private const val START_DELAY = 0L
        private const val CANCEL_DELAY = 15000L

        private val vibrationPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }

}