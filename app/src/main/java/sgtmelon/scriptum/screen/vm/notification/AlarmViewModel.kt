package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.media.MediaPlayer
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

    private var melodyPlayer: MediaPlayer? = null

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { callback?.finish() }

    // TODO #RELEASE Обработка id = -1
    // TODO #RELEASE Убирать уведомление из бд при старте (чтобы не было индикатора на заметке) и потом уже обрабатывать остановку приложения, нажатие на кнопки
    override fun onSetup(bundle: Bundle?) {
        callback?.setupView(iPreferenceRepo.theme)

        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
            signalState = iPreferenceRepo.signalState

            if (signalState.isMelody) {
                melodyPlayer = MediaPlayer.create(context, iPreferenceRepo.melodyUri.toUri())
                melodyPlayer?.apply {
                    isLooping = true

                    if (iPreferenceRepo.volumeIncrease) {
                        setVolume(0.5f, 0.5f)
                    } else {
                        setVolume(0.5f, 0.5f)
                    }
                }
            }
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

        melodyPlayer?.start()

        if (signalState.isVibration) callback?.vibrateStart(vibrationPattern, vibrationRepeat)

        longWaitHandler.postDelayed(longWaitRunnable, CANCEL_DELAY)
    }

    override fun onPause() {
        melodyPlayer?.pause()

        if (signalState.isVibration) callback?.vibrateCancel()
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        melodyPlayer?.stop()

        if (signalState.isVibration) callback?.vibrateCancel()

        longWaitHandler.removeCallbacks(longWaitRunnable)
    }

    // TODO убираем уведомление из бд
    override fun onClickNote() {
        val noteEntity = noteModel.noteEntity

        callback?.apply {
            startActivity(NoteActivity.getInstance(context, noteEntity.type, noteEntity.id))
            finish()
        }
    }

    override fun onClickDisable() {
        context.showToast(text = "CLICK DISABLE")
        callback?.finish()
    }

    override fun onClickPostpone() {
        context.showToast(text = "CLICK POSTPONE")
        callback?.finish()
    }

    companion object {
        private const val CANCEL_DELAY = 15000L

        private val vibrationPattern = longArrayOf(500, 750, 500, 750, 500, 0)
        private val vibrationRepeat = (CANCEL_DELAY / vibrationPattern.sum()).toInt()
    }

}