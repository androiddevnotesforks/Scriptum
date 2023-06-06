package sgtmelon.scriptum.infrastructure.screen.alarm

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import sgtmelon.safedialog.utils.DialogStorage
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.RepeatSheetDialog
import sgtmelon.scriptum.databinding.ActivityAlarmBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.BundleValueImpl
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.converter.dialog.RepeatSheetData
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Key
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.alarm.state.ScreenState
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.DelayedJob
import sgtmelon.scriptum.infrastructure.utils.extensions.afterLayoutConfigured
import sgtmelon.scriptum.infrastructure.utils.extensions.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
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

    private val animation = AlarmAnimation()

    private val noteId = BundleValueImpl<Long>(Key.ID)
    override val bundleValues: List<BundleValue> = listOf(noteId)

    private val finishTimer = DelayedJob(lifecycle)

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Alarm(fm) }
    private val repeatDialog = DialogStorage(
        create = { dialogs.createRepeat() },
        find = { dialogs.findRepeat() },
        setup = { setupRepeatDialog(it) }
    )

    private val adapter = NoteAdapter(object : NoteClickListener {
        override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
        override fun onNoteLongClick(item: NoteItem, p: Int) = Unit
    })

    /** Variable for detect layout is completely configure and ready for [animation]. */
    private var isLayoutConfigure = false

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIdling().start(IdlingTag.Alarm.START)
    }

    override fun inject(component: ScriptumComponent) {
        component.getAlarmBuilder()
            .set(owner = this)
            .set(noteId.value)
            .build()
            .inject(activity = this)
    }

    /** This activity not rotatable (don't need setup margin for left and right). */
    override fun setupInsets() {
        super.setupInsets()

        binding?.logoView?.setMarginInsets(InsetsDir.TOP)
        binding?.buttonContainer?.setMarginInsets(InsetsDir.BOTTOM)
    }

    override fun registerReceivers() {
        super.registerReceivers()
        registerReceiver(unbindNoteReceiver, IntentFilter(Filter.ALARM))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        unregisterReceiver(unbindNoteReceiver)
    }

    override fun setupView() {
        super.setupView()

        setupScreen()

        binding?.parentContainer?.afterLayoutConfigured { isLayoutConfigure = true }

        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true) /** The height of all items absolutely the same. */
            it.adapter = adapter
        }

        binding?.disableButton?.setOnClickListener { open.attempt { finish() } }
        binding?.repeatButton?.setOnClickListener { open.attempt { startPostpone() } }
        binding?.moreButton?.setOnClickListener { showRepeatDialog() }
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

    override fun setupDialogs() {
        super.setupDialogs()
        repeatDialog.restore()
    }

    private fun setupRepeatDialog(dialog: RepeatSheetDialog): Unit = with(dialog) {
        onItemSelected(owner = this@AlarmActivity) {
            startPostpone(RepeatSheetData().convert(it.itemId))
        }
        onDismiss {
            repeatDialog.release()
            open.clear()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.noteItem.observe(this) { notifyItem(it) }
        viewModel.state.observe(this) {
            when (it) {
                is ScreenState.Setup -> onSetupState(it)
                is ScreenState.Postpone -> onPostponeState(it)
                is ScreenState.Close -> {
                    getIdling().stop(IdlingTag.Alarm.START)
                    finish()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (system?.phoneAwake?.isAwake.isFalse()) {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()

        if (isFinishOnStop) {
            finish()
        }
    }

    override fun releaseBinding() {
        super.releaseBinding()
        binding?.rippleContainer?.stopAnimation()
    }

    override fun releaseSystem() {
        super.releaseSystem()
        system?.vibrator?.cancelRepeat()
        system?.phoneAwake?.release()
    }

    //endregion

    //region Setup state

    private fun onSetupState(state: ScreenState.Setup) {
        system?.phoneAwake?.wakeUp(TIMEOUT_TIME)

        if (state.melodyUri != null) {
            setupPlayer(state.melodyUri)
        }

        system?.broadcast?.sendNotifyInfoBind()
        startLogoShiftAnimation()
    }

    private fun setupPlayer(stringUri: String) {
        val uri = UriConverter().toUri(stringUri) ?: return
        val alarmState = viewModel.alarmState

        system?.alarmPlay
            ?.setupVolume(alarmState.volumePercent, alarmState.isVolumeIncrease)
            ?.setupPlayer(uri, isLooping = true)
    }

    private fun startLogoShiftAnimation() {
        animation.startLogoTransition(binding, { onLogoTransitionEnd() }) {
            binding?.recyclerView?.makeVisible()
            binding?.buttonContainer?.makeVisible()
        }
    }

    private fun notifyItem(item: NoteItem) = adapter.notifyList(arrayListOf(item))

    private fun onLogoTransitionEnd() {
        if (isLayoutConfigure) {
            onStartState()
        } else {
            binding?.parentContainer?.afterLayoutConfigured {
                isLayoutConfigure = true
                onStartState()
            }
        }
    }

    //endregion

    //region Start state

    private fun onStartState() {
        val alarmState = viewModel.alarmState

        startRippleAnimation()
        animation.startContentFade(binding)

        if (alarmState.signalState.isMelody) {
            system?.alarmPlay?.start(alarmState.isVolumeIncrease)
        }

        if (alarmState.signalState.isVibration) {
            system?.vibrator?.startRepeat()
        }

        /** Start count down for finish this screen. */
        finishTimer.start(TIMEOUT_TIME) { startPostpone() }

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

    private fun onPostponeState(state: ScreenState.Postpone) = beforeFinish {
        with(state) {
            system?.broadcast?.sendSetAlarm(noteId, calendar, showToast = false)
            system?.broadcast?.sendNotifyInfoBind()
            showRepeatToast(repeat)
        }
    }

    // TODO check how it will be working after finish \ may be send toast into systemLogic and show from there?
    private fun showRepeatToast(repeat: Repeat) {
        val repeatArray = resources.getStringArray(R.array.pref_repeat)
        val repeatText = repeatArray.getOrNull(repeat.ordinal) ?: return

        system?.toast?.show(context = this, getString(R.string.toast_alarm_repeat, repeatText))
    }

    //endregion

    private fun showRepeatDialog() = open.attempt {
        repeatDialog.show(DialogFactory.Alarm.REPEAT, owner = this)
    }

    private fun openNoteScreen(item: NoteItem) = beforeFinish {
        open.attempt { startActivity(Screens.Note.toExist(context = this, item)) }
    }

    companion object {
        @RunPrivate var isFinishOnStop = true

        const val TIMEOUT_TIME = 20000L
    }
}