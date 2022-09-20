package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.afterLayoutConfiguration
import sgtmelon.scriptum.cleanup.extension.beforeFinish
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.setMarginInsets
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.cleanup.presentation.adapter.callback.NoteItemClickCallback
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.databinding.ActivityAlarmBinding
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.dialogs.data.RepeatSheetData
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmAnimations
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmBundleProvider
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmScreenState
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModel
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.PhoneAwakeDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.VibratorDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.melody.MelodyPlayDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.DelayJobDelegator
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

/**
 * Screen with notification opened by timer.
 *
 * Scenario of this screen:
 * - This screen not support rotation changes - so we don't care about activity lifecycle.
 * - Ways to disable alarm:
 *      - Open note
 *      - Click disable button
 *      - Close app / disable phone screen
 * - Ways to postpone alarm:
 *      - Click postpone
 *      - Select custom postpone
 *      - Timer is over
 *
 * TODO описать полностью функционал экрана после проверки его работы
 */
class AlarmActivity : ThemeActivity<ActivityAlarmBinding>() {

    override val layoutId: Int = R.layout.activity_alarm

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: AlarmViewModel

    /**
     * [initLazy] not require because activity configChanges under control.
     */
    // TODO init on UI
    private val phoneAwake by lazy { PhoneAwakeDelegator(context = this) }
    private val finishTimer = DelayJobDelegator(lifecycle)
    private val melodyPlay by lazy {
        MelodyPlayDelegator(context = this, lifecycle, AudioManager.STREAM_ALARM)
    }
    private val vibrator by lazy { VibratorDelegator(context = this) }
    private val broadcast by lazy { BroadcastDelegator(context = this) }

    // TODO create separate receiver for this functional
    private val noteReceiver by lazy { NoteScreenReceiver[viewModel] }

    private val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Alarm(fm = fm) }
    private val repeatDialog by lazy { dialogFactory.getRepeatDialog() }

    private val adapter = NoteAdapter(object : NoteItemClickCallback {
        override fun onItemClick(item: NoteItem) = openNoteScreen(item)
        override fun onItemLongClick(item: NoteItem, p: Int) = Unit
    })

    /** Variable for detect layout is completely configure and ready for animation. */
    private var isLayoutConfigure = false

    private val animations = AlarmAnimations()

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupScreen()

        val noteId = AlarmBundleProvider().getNoteId(intent.extras) ?: run {
            finish()
            return
        }

        setupObservers(noteId)

        registerReceiver(noteReceiver, IntentFilter(Filter.NOTE))
    }

    override fun inject(component: ScriptumComponent) {
        component.getAlarmBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    private fun setupScreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }

    private fun setupObservers(noteId: Long) {
        viewModel.setup(noteId)
        viewModel.noteItem.observe(this) { notifyItem(it) }
        viewModel.state.observe(this) {
            when (it) {
                is AlarmScreenState.Setup -> onSetupState(it)
                is AlarmScreenState.Postpone -> onPostponeState(it)
                is AlarmScreenState.Close -> finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (!phoneAwake.isAwake) {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()

        if (isFinishOnStop) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        vibrator.cancel()
        phoneAwake.release()
        binding?.rippleContainer?.stopAnimation()

        unregisterReceiver(noteReceiver)
    }

    //endregion

    //region Setup state

    private fun onSetupState(state: AlarmScreenState.Setup) {
        phoneAwake.wakeUp(TIMEOUT_TIME)
        setupView()
        setupInsets()

        if (state.melodyUri != null) {
            setupPlayer(state.melodyUri)
        }

        broadcast.sendNotifyInfoBind(count = null)
        startLogoShiftAnimation()
    }

    private fun setupView() {
        binding?.parentContainer?.afterLayoutConfiguration { isLayoutConfigure = true }

        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        binding?.disableButton?.setOnClickListener { openState.tryInvoke { finish() } }
        binding?.repeatButton?.setOnClickListener { openState.tryInvoke { startPostpone() } }
        binding?.moreButton?.setOnClickListener {
            openState.tryInvoke {
                repeatDialog.safeShow(fm, DialogFactory.Alarm.REPEAT, owner = this)
            }
        }

        repeatDialog.apply {
            onItemSelected(owner = this@AlarmActivity) {
                startPostpone(RepeatSheetData().convert(it.itemId))
            }
            onDismiss { openState.clear() }
        }
    }

    /**
     * This activity not rotatable (don't need setup margin for left and right).
     */
    private fun setupInsets() {
        binding?.logoView?.setMarginInsets(InsetsDir.TOP)
        binding?.buttonContainer?.setMarginInsets(InsetsDir.BOTTOM)
    }

    private fun setupPlayer(stringUri: String) {
        val uri = UriConverter().toUri(stringUri) ?: return
        val alarmState = viewModel.alarmState

        melodyPlay.setupVolume(alarmState.volumePercent, alarmState.isVolumeIncrease)
        melodyPlay.setupPlayer(context = this, uri, isLooping = true)
    }

    private fun startLogoShiftAnimation() {
        animations.startLogoShiftAnimation(
            binding?.parentContainer, binding?.logoView, { onLogoTransitionEnd() }
        ) {
            binding?.recyclerView?.visibility = View.VISIBLE
            binding?.buttonContainer?.visibility = View.VISIBLE
        }
    }

    private fun notifyItem(item: NoteItem) = adapter.notifyList(arrayListOf(item))

    private fun onLogoTransitionEnd() {
        if (isLayoutConfigure) {
            onStartState()
        } else {
            getIdling().start(IdlingTag.Alarm.CONFIGURE)
            binding?.parentContainer?.afterLayoutConfiguration {
                onStartState()
                getIdling().stop(IdlingTag.Alarm.CONFIGURE)
            }
        }
    }

    //endregion

    //region Start state

    private fun onStartState() {
        val alarmState = viewModel.alarmState

        getIdling().start(IdlingTag.Alarm.START)

        startRippleAnimation()
        animations.startContentAnimation(binding?.recyclerView, binding?.buttonContainer)

        if (alarmState.signalState.isMelody) {
            melodyPlay.start(alarmState.isVolumeIncrease)
        }

        if (alarmState.signalState.isVibration) {
            vibrator.startRepeat()
        }

        /** Start count down for finish this screen. */
        finishTimer.run(TIMEOUT_TIME) { startPostpone() }

        getIdling().stop(IdlingTag.Alarm.START)
    }

    private fun startRippleAnimation() {
        val noteItem = viewModel.noteItem.value ?: return
        val logoView = binding?.logoView ?: return

        binding?.rippleContainer?.setup(noteItem.color, logoView)?.startAnimation()
    }

    //endregion

    //region Postpone state

    /**
     * Call this when need set alarm repeat with screen finish. If [repeat] is null when will
     * be used value from preferences.
     */
    private fun startPostpone(repeat: Repeat? = null) {
        val timeArray = resources.getIntArray(R.array.pref_alarm_repeat_array)
        viewModel.postpone(repeat, timeArray)
    }

    private fun onPostponeState(state: AlarmScreenState.Postpone) = beforeFinish {
        with(state) {
            broadcast.sendSetAlarm(noteId, calendar, showToast = false)
            broadcast.sendNotifyInfoBind(count = null)
            broadcast.sendUpdateAlarmUi(noteId)
            showRepeatToast(repeat)
        }
    }

    // TODO check how it will be working after finish \ may be send toast into systemLogic and show from there?
    private fun showRepeatToast(repeat: Repeat) {
        val repeatArray = resources.getStringArray(R.array.pref_alarm_repeat)
        val repeatText = repeatArray.getOrNull(repeat.ordinal) ?: return

        toast.show(context = this, getString(R.string.toast_alarm_repeat, repeatText))
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) = beforeFinish {
        openState.tryInvoke { startActivity(NoteActivity[this, item]) }
    }

    companion object {
        @RunPrivate var isFinishOnStop = true

        const val TIMEOUT_TIME = 20000L

        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                .putExtra(Note.Intent.ID, id)
        }
    }
}