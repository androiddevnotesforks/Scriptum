package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import androidx.annotation.ArrayRes
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.extensions.getColorAttr
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.afterLayoutConfiguration
import sgtmelon.scriptum.cleanup.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.cleanup.extension.getAlphaAnimator
import sgtmelon.scriptum.cleanup.extension.getAlphaInterpolator
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.toUriOrNull
import sgtmelon.scriptum.cleanup.extension.updateMargin
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.cleanup.presentation.control.system.MelodyPlayDelegator
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.screen.DelayJobDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.PhoneAwakeDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.VibratorDelegator
import sgtmelon.scriptum.infrastructure.widgets.ripple.RippleContainer
import sgtmelon.test.idling.addIdlingListener
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate
import android.graphics.Color as AndroidColor

/**
 * Screen with notification opened by timer.
 */
class AlarmActivity : AppActivity(), IAlarmActivity {

    @Inject internal lateinit var viewModel: IAlarmViewModel

    /**
     * [initLazy] not require because activity configChanges under control.
     */
    private val phoneAwake by lazy { PhoneAwakeDelegator(context = this) }
    private val finishTimer = DelayJobDelegator(lifecycle)
    private val melodyPlay by lazy {
        MelodyPlayDelegator(context = this, lifecycle, AudioManager.STREAM_ALARM)
    }
    private val vibrator by lazy { VibratorDelegator(context = this) }
    private val broadcast by lazy { BroadcastDelegator(context = this) }

    private val noteReceiver by lazy { NoteScreenReceiver[viewModel] }

    private val openState = OpenState()

    private val dialogFactory by lazy { DialogFactory.Alarm(fm = fm) }
    private val repeatDialog by lazy { dialogFactory.getRepeatDialog() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = openState.tryInvoke {
                viewModel.onClickNote()
            }
        })
    }

    //region Views

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.alarm_parent_container) }
    private val rippleContainer by lazy { findViewById<RippleContainer?>(R.id.alarm_ripple_container) }

    private val logoView by lazy { findViewById<View?>(R.id.alarm_logo_view) }
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.alarm_recycler) }

    private val buttonContainer by lazy { findViewById<ViewGroup?>(R.id.alarm_button_container) }
    private val disableButton by lazy { findViewById<View?>(R.id.alarm_disable_button) }
    private val repeatButton by lazy { findViewById<View?>(R.id.alarm_repeat_button) }
    private val moreButton by lazy { findViewById<View?>(R.id.alarm_more_button) }

    //endregion

    /** Variable for detect layout is completely configure and ready for animation. */
    private var isLayoutConfigure = false

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getAlarmBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        setContentView(R.layout.activity_alarm)

        viewModel.onSetup(bundle = savedInstanceState ?: intent.extras)

        registerReceiver(noteReceiver, IntentFilter(Filter.NOTE))
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

        viewModel.onDestroy()
        rippleContainer?.stopAnimation()

        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun setStatusBarColor() {
        window.statusBarColor = AndroidColor.TRANSPARENT
    }

    override fun setNavigationColor(theme: ThemeDisplayed) {
        window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setNavigationDividerColor(theme: ThemeDisplayed) {
        window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
    }


    override fun setupView() {
        parentContainer?.afterLayoutConfiguration { isLayoutConfigure = true }

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        disableButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickDisable() } }
        repeatButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickRepeat() } }
        moreButton?.setOnClickListener {
            openState.tryInvoke {
                repeatDialog.safeShow(fm, DialogFactory.Alarm.REPEAT, owner = this)
            }
        }

        repeatDialog.apply {
            onItemSelected(owner = this@AlarmActivity) { viewModel.onResultRepeatDialog(it.itemId) }
            onDismiss { openState.clear() }
        }
    }

    /**
     * This activity not rotatable (don't need setup margin for left and right).
     */
    override fun setupInsets() {
        logoView?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.TOP, insets, margin)
            return@doOnApplyWindowInsets insets
        }

        buttonContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.BOTTOM, insets, margin)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun setupPlayer(stringUri: String, volumePercent: Int, isIncrease: Boolean) {
        val uri = stringUri.toUriOrNull() ?: return

        melodyPlay.setupVolume(volumePercent, isIncrease)
        melodyPlay.setupPlayer(context = this@AlarmActivity, uri, isLooping = true)
    }

    override fun prepareLogoAnimation() {
        val parentContainer = parentContainer ?: return
        val logoView = logoView ?: return

        val transition = AutoTransition()
            .setInterpolator(AccelerateInterpolator())
            .addTarget(logoView)
            .addIdlingListener()
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) = onLogoTransitionEnd()
            })

        TransitionManager.beginDelayedTransition(parentContainer, transition)

        recyclerView?.visibility = View.VISIBLE
        buttonContainer?.visibility = View.VISIBLE
    }

    private fun onLogoTransitionEnd() {
        if (isLayoutConfigure) {
            viewModel.onStart()
        } else {
            waitLayoutConfigure()
        }
    }

    private fun waitLayoutConfigure() {
        getIdling().start(IdlingTag.Alarm.CONFIGURE)

        parentContainer?.afterLayoutConfiguration {
            viewModel.onStart()
            getIdling().stop(IdlingTag.Alarm.CONFIGURE)
        }
    }

    override fun notifyList(item: NoteItem) = adapter.notifyList(arrayListOf(item))

    override fun startRippleAnimation(color: Color) {
        val logoView = logoView ?: return

        rippleContainer?.setupAnimation(color, logoView)?.startAnimation()
    }

    override fun startButtonFadeInAnimation() {
        val recyclerView = recyclerView ?: return
        val buttonContainer = buttonContainer ?: return

        AnimatorSet().apply {
            interpolator = getAlphaInterpolator(isVisible = true)
            startDelay = resources.getInteger(R.integer.alarm_show_delay).toLong()
            duration = resources.getInteger(R.integer.alarm_show_time).toLong()

            playTogether(
                getAlphaAnimator(recyclerView, alphaTo = 1f),
                getAlphaAnimator(buttonContainer, alphaTo = 1f)
            )

            addIdlingListener()
        }.start()
    }

    override fun openNoteScreen(item: NoteItem) = startActivity(NoteActivity[this, item])


    override fun wakePhone(timeout: Long) = phoneAwake.wakeUp(timeout)

    override fun releasePhone() = phoneAwake.release()

    override fun startFinishTimer(time: Long) {
        finishTimer.run(time) { viewModel.finishWithRepeat() }
    }

    override fun startMelody(isIncrease: Boolean) = melodyPlay.start(isIncrease)

    override fun stopMelody() = melodyPlay.stop()

    override fun startVibrator() = vibrator.startRepeat()

    override fun cancelVibrator() = vibrator.cancel()


    override fun showRepeatToast(repeat: Repeat) {
        val repeatArray = resources.getStringArray(R.array.pref_alarm_repeat)
        val repeatText = repeatArray.getOrNull(repeat.ordinal) ?: return

        toastControl.show(getString(R.string.toast_alarm_repeat, repeatText))
    }

    override fun getIntArray(@ArrayRes arrayId: Int): IntArray = resources.getIntArray(arrayId)

    //region Broadcast functions

    override fun sendUpdateBroadcast(id: Long) = broadcast.sendUpdateAlarmUi(id)

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        broadcast.sendSetAlarm(id, calendar, showToast)
    }

    /**
     * Not used here.
     */
    override fun sendCancelAlarmBroadcast(id: Long) = Unit

    /**
     * Not used here.
     */
    override fun sendNotifyNotesBroadcast() = Unit

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcast.sendNotifyInfoBind(count)

    //endregion

    companion object {
        @RunPrivate var isFinishOnStop = true

        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                .putExtra(Note.Intent.ID, id)
        }
    }
}