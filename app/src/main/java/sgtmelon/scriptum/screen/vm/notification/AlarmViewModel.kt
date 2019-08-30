package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import android.os.Handler
import sgtmelon.extension.getDateFormat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import java.util.*

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

    private val vibratorHandler = Handler()
    private val vibratorRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibratorPattern)
            vibratorHandler.postDelayed(this, vibratorPattern.sum())
        }
    }

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { callback?.finish() }

    /**
     * Control setup alarm repeat in [onDestroy]
     */
    private var needRepeat = true

    // TODO #RELEASE2 Обработка id = -1
    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            acquirePhone(CANCEL_DELAY)
            setupView(iPreferenceRepo.theme)
            setupMelody(iPreferenceRepo.melodyUri.toUri())
        }

        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
            color = bundle.getInt(NoteData.Intent.COLOR, iPreferenceRepo.defaultColor)
        }

        if (!::noteModel.isInitialized) {
            /**
             * Delete before get [noteModel] for hide alarm icon
             */
            iAlarmRepo.delete(id)

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
            callback?.melodyStart()
        }

        if (signalState.isVibration) {
            vibratorHandler.postDelayed(vibratorRunnable, START_DELAY)
        }

        longWaitHandler.postDelayed(longWaitRunnable, CANCEL_DELAY)
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        if (signalState.isMelody) {
            callback?.melodyStop()
        }

        if (signalState.isVibration) {
            callback?.vibrateCancel()
            vibratorHandler.removeCallbacks(vibratorRunnable)
        }

        longWaitHandler.removeCallbacks(longWaitRunnable)

        if (needRepeat) {
            val valueArray = context.resources.getIntArray(R.array.value_alarm_repeat_array)
            val repeat = iPreferenceRepo.repeat

            val calendar = Calendar.getInstance().apply {
                add(Calendar.MINUTE, valueArray[repeat])
            }

            iAlarmRepo.insertOrUpdate(noteModel.alarmEntity.apply {
                date = getDateFormat().format(calendar.time)
            })

            callback?.setAlarm(calendar, AlarmReceiver.getInstance(context, noteModel.noteEntity))
            callback?.showPostponeToast(repeat)
        }

        callback?.releasePhone()
    }

    override fun onClickNote() {
        needRepeat = false

        callback?.apply {
            startActivity(NoteActivity.getInstance(context, noteModel.noteEntity))
            finish()
        }
    }

    override fun onClickDisable() {
        needRepeat = false
        callback?.finish()
    }

    override fun onClickPostpone() {
        callback?.finish()
    }

    companion object {
        private const val START_DELAY = 0L
        private const val CANCEL_DELAY = 15000L

        private val vibratorPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }

}