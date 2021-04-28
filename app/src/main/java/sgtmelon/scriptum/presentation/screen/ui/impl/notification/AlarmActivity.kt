package sgtmelon.scriptum.presentation.screen.ui.impl.notification

import android.animation.AnimatorSet
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
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
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Filter
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.presentation.control.system.*
import sgtmelon.scriptum.presentation.control.system.callback.IMelodyControl
import sgtmelon.scriptum.presentation.control.system.callback.IPowerControl
import sgtmelon.scriptum.presentation.control.system.callback.IVibratorControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.presentation.view.RippleContainer
import java.util.*
import javax.inject.Inject
import android.graphics.Color as AndroidColor

/**
 * Screen with notification opened by timer.
 */
class AlarmActivity : AppActivity(), IAlarmActivity {

    @Inject internal lateinit var viewModel: IAlarmViewModel

    private val vibratorHandler = Handler()
    private val longWaitHandler = Handler()

    /**
     * [initLazy] not require because activity configChanges under control.
     */
    private val melodyControl: IMelodyControl by lazy { MelodyControl(context = this) }
    private val vibratorControl: IVibratorControl by lazy { VibratorControl(context = this) }
    private val alarmControl by lazy { AlarmControl[this] }
    private val powerControl: IPowerControl by lazy { PowerControl(context = this) }
    private val bindControl by lazy { BindControl[null] }

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

    /**
     * Variable for detect layout is completely configure and ready for animation.
     */
    private var isLayoutConfigure = false

    @Suppress("DEPRECATION")
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
        if (!powerControl.isScreenOn) finish()
    }

    override fun onStop() {
        super.onStop()

        if (isFinishOnStop) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        longWaitHandler.removeCallbacksAndMessages(null)
        vibratorHandler.removeCallbacksAndMessages(null)

        viewModel.onDestroy()
        rippleContainer?.stopAnimation()

        melodyControl.release()

        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun setStatusBarColor() {
        window.statusBarColor = AndroidColor.TRANSPARENT
    }

    override fun setNavigationColor(@Theme theme: Int) {
        window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setNavigationDividerColor(@Theme theme: Int) {
        window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
    }


    override fun acquirePhone(timeout: Long) = powerControl.acquire(timeout)

    override fun releasePhone() = powerControl.release()


    override fun setupView() {
        parentContainer?.afterLayoutConfiguration { isLayoutConfigure = true }

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        disableButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickDisable() } }
        repeatButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickRepeat() } }
        moreButton?.setOnClickListener {
            openState.tryInvoke { repeatDialog.show(fm, DialogFactory.Alarm.REPEAT) }
        }

        repeatDialog.apply {
            itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
                dismiss()
                viewModel.onResultRepeatDialog(it.itemId)
                return@OnNavigationItemSelectedListener true
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
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

    override fun setupPlayer(stringUri: String, volume: Int, increase: Boolean){
        val uri = stringUri.toUri() ?: return

        with(melodyControl) {
            setupVolume(volume, increase)
            setupPlayer(uri, isLooping = true)
        }
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
        AppIdlingResource.getInstance().startWork(IdlingTag.Alarm.CONFIGURE)

        parentContainer?.afterLayoutConfiguration {
            viewModel.onStart()
            AppIdlingResource.getInstance().stopWork(IdlingTag.Alarm.CONFIGURE)
        }
    }

    override fun notifyList(item: NoteItem) = adapter.notifyList(arrayListOf(item))

    override fun startRippleAnimation(@Color color: Int) {
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


    override fun startLongWaitHandler(delay: Long, r: Runnable) {
        longWaitHandler.postDelayed(r, delay)
    }

    override fun startVibratorHandler(delay: Long, r: Runnable) {
        vibratorHandler.postDelayed(r, delay)
    }


    override fun melodyStart() = melodyControl.start()

    override fun melodyStop() = melodyControl.stop()

    override fun vibrateStart(pattern: LongArray) = vibratorControl.start(pattern)

    override fun vibrateCancel() = vibratorControl.cancel()


    override fun showRepeatToast(select: Int) {
        val repeatArray = resources.getStringArray(R.array.pref_text_alarm_repeat)
        val repeatText = repeatArray.getOrNull(select) ?: return

        showToast(getString(R.string.toast_alarm_repeat, repeatText))
    }

    override fun getIntArray(@ArrayRes arrayId: Int): IntArray = resources.getIntArray(arrayId)

    override fun sendUpdateBroadcast(id: Long) = sendTo(Filter.MAIN, Command.UPDATE_ALARM) {
        putExtra(Note.Intent.ID, id)
    }


    override fun setAlarm(calendar: Calendar, id: Long, showToast: Boolean) {
        alarmControl.set(calendar, id, showToast)
    }

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)

    /**
     * Function for detect when layout completely configure.
     */
    private fun ViewGroup.afterLayoutConfiguration(func: () -> Unit) {
        viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver?.removeOnGlobalLayoutListener(this)
                func()
            }
        })
    }

    companion object {
        @RunPrivate var isFinishOnStop = true

        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                .putExtra(Note.Intent.ID, id)
        }
    }

}