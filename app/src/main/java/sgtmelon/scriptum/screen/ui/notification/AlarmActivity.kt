package sgtmelon.scriptum.screen.ui.notification

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.alarm.MelodyControl
import sgtmelon.scriptum.control.alarm.PowerControl
import sgtmelon.scriptum.control.alarm.VibratorControl
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl
import sgtmelon.scriptum.control.alarm.callback.IPowerControl
import sgtmelon.scriptum.control.alarm.callback.IVibratorControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.ScriptumApplication
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.view.RippleContainer
import java.util.*
import javax.inject.Inject

/**
 * Screen with notification opened by timer
 */
class AlarmActivity : AppActivity(), IAlarmActivity {

    /**
     * TODO Убрать логику для смены ориентации, её тут нет.
     */

    @Inject internal lateinit var viewModel: IAlarmViewModel

    /**
     * [initLazy] not require because activity configChanges under control.
     */
    private val melodyControl: IMelodyControl by lazy { MelodyControl(context = this) }
    private val vibratorControl: IVibratorControl by lazy { VibratorControl(context = this) }
    private val alarmControl by lazy { AlarmControl[this] }
    private val powerControl: IPowerControl by lazy { PowerControl(context = this) }
    private val bindControl by lazy { BindControl[this] }

    private val noteReceiver by lazy { NoteReceiver(viewModel) }

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

        registerReceiver(noteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun onPause() {
        super.onPause()
        if (!powerControl.isScreenOn) finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
        rippleContainer?.stopAnimation()

        melodyControl.release()

        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply { viewModel.onSaveData(bundle = this) })
    }

    /**
     * It calls when orientation changes
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        parentContainer?.afterLayoutConfiguration { rippleContainer?.invalidate(logoView) }
    }

    override fun acquirePhone(timeout: Long) = powerControl.acquire(timeout)

    override fun releasePhone() = powerControl.release()


    override fun setupView(@Theme theme: Int) {
        adapter.theme = theme

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
                viewModel.onResultRepeatDialog(it)
                return@OnNavigationItemSelectedListener true
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupPlayer(volume: Int, increase: Boolean, uri: Uri) = with(melodyControl) {
        setupVolume(volume, increase)
        setupPlayer(uri, isLooping = true)
    }

    override fun notifyList(item: NoteItem) = adapter.notifyList(arrayListOf(item))


    /**
     * Need call on coroutine end, because layout may be configure before coroutine end.
     */
    override fun waitLayoutConfigure() {
        parentContainer?.afterLayoutConfiguration { viewModel.onStart() }
    }

    override fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int) {
        logoView?.let { rippleContainer?.setupAnimation(theme, fillColor, it)?.startAnimation() }
    }

    override fun startButtonFadeInAnimation() {
        parentContainer?.let { group ->
            TransitionManager.beginDelayedTransition(group, Fade().apply {
                startDelay = resources.getInteger(R.integer.alarm_show_delay).toLong()
                duration = resources.getInteger(R.integer.alarm_show_time).toLong()

                recyclerView?.let { addTarget(it) }
                buttonContainer?.let { addTarget(it) }
            })
        }

        recyclerView?.visibility = View.VISIBLE
        buttonContainer?.visibility = View.VISIBLE
    }

    override fun startNoteActivity(item: NoteItem) {
        startActivity(NoteActivity[this, item])
    }


    override fun melodyStart() = melodyControl.start()

    override fun melodyStop() = melodyControl.stop()

    override fun vibrateStart(pattern: LongArray) = vibratorControl.start(pattern)

    override fun vibrateCancel() = vibratorControl.cancel()

    override fun showRepeatToast(select: Int) {
        showToast(getString(R.string.toast_alarm_repeat, resources.getStringArray(R.array.pref_text_alarm_repeat)[select]))
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        alarmControl.set(calendar, id, showToast = false)
    }

    override fun notifyInfoBind(count: Int) {
        bindControl.notifyInfo(count)
    }

    /**
     * Function for detect when layout completely configure
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
        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
        }
    }

}