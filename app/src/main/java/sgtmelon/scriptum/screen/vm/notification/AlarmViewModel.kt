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

    private var noteModel: NoteModel? = null

    private var melodyPlayer: MediaPlayer? = null

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { callback?.finish() }

    private var vibrationHandler: Handler? = null
    private val vibrationRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibrationPattern)
            vibrationHandler?.postDelayed(this, vibrationPattern.sum())
        }
    }

    private var lightHandler: Handler? = null
    private val lightRunnable = object : Runnable {
        override fun run() {
            // TODO #RELEASE
            context.showToast("Light on/off")
            lightHandler?.postDelayed(this, LIGHT_DELAY)
        }
    }

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

        if (noteModel == null) noteModel = iRoomRepo.getNoteModel(id)

        iPreferenceRepo.signalCheck.forEachIndexed { i, bool ->
            if (!bool) return@forEachIndexed

            when (i) {
                PreferenceRepo.SIGNAL_MELODY -> {
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
                PreferenceRepo.SIGNAL_VIBRATION -> vibrationHandler = Handler()
                PreferenceRepo.SIGNAL_LIGHT -> lightHandler = Handler()
            }
        }

        noteModel?.let { callback?.notifyDataSetChanged(it) }
    }

    override fun onStart() {
        melodyPlayer?.start()

        longWaitHandler.postDelayed(longWaitRunnable, CANCEL_DELAY)
        vibrationHandler?.postDelayed(vibrationRunnable, START_DELAY)
        lightHandler?.postDelayed(lightRunnable, START_DELAY)

        callback?.let {
            val theme = iPreferenceRepo.theme
            it.startRippleAnimation(theme, context.getAppSimpleColor(color,
                    if (theme == Theme.LIGHT) ColorShade.ACCENT else ColorShade.DARK
            ))

            it.startButtonFadeInAnimation()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        callback?.vibrateStop()

        melodyPlayer?.stop()

        longWaitHandler.removeCallbacks(longWaitRunnable)
        vibrationHandler?.removeCallbacks(vibrationRunnable)
        lightHandler?.removeCallbacks(lightRunnable)
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
        putInt(NoteData.Intent.COLOR, color)
    }

    // TODO убираем уведомление из бд
    override fun onClickNote() {
        val noteEntity = noteModel?.noteEntity ?: return

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
        private const val START_DELAY = 0L
        private const val CANCEL_DELAY = 15000L

        // TODO #RELEASE
        private const val LIGHT_DELAY = 1000L

        private val vibrationPattern = longArrayOf(600, 400, 800, 600)
    }

}