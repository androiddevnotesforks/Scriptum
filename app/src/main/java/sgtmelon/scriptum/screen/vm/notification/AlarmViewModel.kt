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
import sgtmelon.scriptum.repository.preference.PreferenceRepo
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

    private var melodyPlayer: MediaPlayer? = null

    private val longWaitHandler = Handler()
    private var vibrationHandler: Handler? = null
    private var lightHandler: Handler? = null

    override fun onSetup() {
        callback?.setupView(iPreferenceRepo.theme)
    }

    // TODO #RELEASE Обработка id = -1
    // TODO #RELEASE Убирать уведомление из бд при старте (чтобы не было индикатора на заметке) и потом уже обрабатывать остановку приложения, нажатие на кнопки
    override fun onSetupData(bundle: Bundle?) {
        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            noteModel = iRoomRepo.getNoteModel(id)
        }

        iPreferenceRepo.signalCheck.forEachIndexed { i, bool ->
            if (!bool) return@forEachIndexed

            when (i) {
                PreferenceRepo.SIGNAL_MELODY -> {
                    melodyPlayer = MediaPlayer.create(context, iPreferenceRepo.melodyUri.toUri())
                    melodyPlayer?.apply {
                        isLooping = true

                        if (iPreferenceRepo.volumeIncrease) {
                            setVolume(100f, 100f)
                        } else {
                            setVolume(100f, 100f)
                        }
                    }
                }
                PreferenceRepo.SIGNAL_VIBRATION -> {
                    vibrationHandler = Handler()
                }
                PreferenceRepo.SIGNAL_LIGHT -> {
                    lightHandler = Handler()
                }
            }
        }

        callback?.notifyDataSetChanged(noteModel)
    }

    override fun onStart() {
        melodyPlayer?.start()

        longWaitHandler.postDelayed({ callback?.finish() }, CANCEL_TIME)
        vibrationHandler?.postDelayed({ callback?.vibrateStart(VIBRATION_PATTERN) }, VIBRATION_TIME)
        lightHandler?.postDelayed({ TODO("") }, 0)

        callback?.let {
            val theme = iPreferenceRepo.theme
            it.startRippleAnimation(theme, context.getAppSimpleColor(color,
                    if (theme == Theme.light) ColorShade.ACCENT else ColorShade.DARK
            ))

            it.startButtonFadeInAnimation()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        callback?.vibrateStop()

        melodyPlayer?.stop()

        longWaitHandler.removeCallbacksAndMessages(null)
        vibrationHandler?.removeCallbacksAndMessages(null)
        lightHandler?.removeCallbacksAndMessages(null)
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.COLOR, color)
    }

    // TODO убираем уведомление из бд
    override fun onClickNote() {
        callback?.apply {
            startActivity(with(noteModel.noteEntity) { NoteActivity.getInstance(context, type, id) })
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
        private const val CANCEL_TIME = 15000L
        private const val VIBRATION_TIME = 1200L

        private val VIBRATION_PATTERN = longArrayOf(200, 400, 200, 400)
    }

}