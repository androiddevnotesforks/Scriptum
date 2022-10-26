package sgtmelon.scriptum.infrastructure.screen.alarm

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.ActivityAlarmBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.dialogs.data.RepeatSheetData
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.DelayJobDelegator
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.afterLayoutConfiguration
import sgtmelon.scriptum.infrastructure.utils.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets
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

    private val bundleProvider = AlarmBundleProvider()

    @Inject lateinit var viewModel: AlarmViewModel

    private val finishTimer = DelayJobDelegator(lifecycle)

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val repeatDialog by lazy { DialogFactory.Alarm(fm).getRepeat() }

    private val adapter = NoteAdapter(object : NoteClickListener {
        override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
        override fun onNoteLongClick(item: NoteItem, p: Int) = Unit
    })

    /** Variable for detect layout is completely configure and ready for animation. */
    private var isLayoutConfigure = false

    private val animation = AlarmAnimation()

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupScreen()

        bundleProvider.getData(bundle = savedInstanceState ?: intent.extras)
        val noteId = bundleProvider.noteId ?: run {
            finish()
            return
        }

        setupObservers(noteId)

        registerReceiver(unbindNoteReceiver, IntentFilter(Filter.ALARM))
    }

    override fun inject(component: ScriptumComponent) {
        component.getAlarmBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    /**
     * This activity not rotatable (don't need setup margin for left and right).
     */
    override fun setupInsets() {
        binding?.logoView?.setMarginInsets(InsetsDir.TOP)
        binding?.buttonContainer?.setMarginInsets(InsetsDir.BOTTOM)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleProvider.saveData(outState)
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

        if (!delegators.phoneAwake.isAwake) {
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

        delegators.vibrator.cancel()
        delegators.phoneAwake.release()
        binding?.rippleContainer?.stopAnimation()

        unregisterReceiver(unbindNoteReceiver)
    }

    //endregion

    //region Setup state

    private fun onSetupState(state: AlarmScreenState.Setup) {
        delegators.phoneAwake.wakeUp(TIMEOUT_TIME)
        setupView()

        if (state.melodyUri != null) {
            setupPlayer(state.melodyUri)
        }

        delegators.broadcast.sendNotifyInfoBind(count = null)
        startLogoShiftAnimation()
    }

    private fun setupView() {
        binding?.parentContainer?.afterLayoutConfiguration { isLayoutConfigure = true }

        binding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

        binding?.disableButton?.setOnClickListener { open.attempt { finish() } }
        binding?.repeatButton?.setOnClickListener { open.attempt { startPostpone() } }
        binding?.moreButton?.setOnClickListener {
            open.attempt { repeatDialog.safeShow(DialogFactory.Alarm.REPEAT, owner = this) }
        }

        repeatDialog.apply {
            onItemSelected(owner = this@AlarmActivity) {
                startPostpone(RepeatSheetData().convert(it.itemId))
            }
            onDismiss { open.clear() }
        }
    }

    private fun setupPlayer(stringUri: String) {
        val uri = UriConverter().toUri(stringUri) ?: return
        val alarmState = viewModel.alarmState

        delegators.alarmPlay
            .setupVolume(alarmState.volumePercent, alarmState.isVolumeIncrease)
            .setupPlayer(uri, isLooping = true)
    }

    private fun startLogoShiftAnimation() {
        animation.startLogoShiftAnimation(
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
                isLayoutConfigure = true
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
        animation.startContentAnimation(binding?.recyclerView, binding?.buttonContainer)

        if (alarmState.signalState.isMelody) {
            delegators.alarmPlay.start(alarmState.isVolumeIncrease)
        }

        if (alarmState.signalState.isVibration) {
            delegators.vibrator.startRepeat()
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
            delegators.broadcast.sendSetAlarm(noteId, calendar, showToast = false)
            delegators.broadcast.sendNotifyInfoBind(count = null)
            delegators.broadcast.sendUpdateAlarmUi(noteId)
            showRepeatToast(repeat)
        }
    }

    // TODO check how it will be working after finish \ may be send toast into systemLogic and show from there?
    private fun showRepeatToast(repeat: Repeat) {
        val repeatArray = resources.getStringArray(R.array.pref_alarm_repeat)
        val repeatText = repeatArray.getOrNull(repeat.ordinal) ?: return

        delegators.toast.show(context = this, getString(R.string.toast_alarm_repeat, repeatText))
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) = beforeFinish {
        open.attempt { startActivity(InstanceFactory.Note[this, item]) }
    }

    companion object {
        @RunPrivate var isFinishOnStop = true

        const val TIMEOUT_TIME = 20000L
    }
}