package sgtmelon.scriptum.screen.ui.notification

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.alarm.MelodyControl
import sgtmelon.scriptum.control.alarm.PowerControl
import sgtmelon.scriptum.control.alarm.VibratorControl
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl
import sgtmelon.scriptum.control.alarm.callback.IPowerControl
import sgtmelon.scriptum.control.alarm.callback.IVibratorControl
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.view.RippleContainer
import java.util.*

/**
 * Screen with notification opened by timer
 */
class AlarmActivity : AppActivity(), IAlarmActivity {

    private val iViewModel by lazy { ViewModelFactory.getAlarmViewModel(activity = this) }

    private val iMelodyControl: IMelodyControl by lazy { MelodyControl(context = this) }
    private val iVibratorControl: IVibratorControl by lazy { VibratorControl(context = this) }
    private val iAlarmControl by lazy { AlarmControl[this] }
    private val iPowerControl: IPowerControl by lazy { PowerControl(context = this) }

    private val openState = OpenState()

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = openState.tryInvoke {
                iViewModel.onClickNote()
            }
        })
    }

    //region Views

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.alarm_parent_container) }
    private val rippleContainer by lazy { findViewById<RippleContainer?>(R.id.alarm_ripple_container) }

    private val logoView by lazy { findViewById<View?>(R.id.alarm_logo_view) }
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.alarm_recycler) }

    private val buttonContainer by lazy { findViewById<ViewGroup?>(R.id.alarm_button_container) }
    private val disableButton by lazy { findViewById<Button?>(R.id.alarm_disable_button) }
    private val postponeButton by lazy { findViewById<Button?>(R.id.alarm_postpone_button) }

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        iViewModel.onSetup(bundle = savedInstanceState ?: intent.extras)

        parentContainer?.afterLayoutConfiguration { iViewModel.onStart() }

        /**
         * If keyboard was open in another app
         */
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        rippleContainer?.stopAnimation()

        iMelodyControl.release()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { iViewModel.onSaveData(bundle = this) })

    /**
     * It calls when orientation changes
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        parentContainer?.afterLayoutConfiguration { rippleContainer?.invalidate(logoView) }
    }

    override fun acquirePhone(timeout: Long) = iPowerControl.acquire(timeout)

    override fun releasePhone() = iPowerControl.release()


    override fun setupView(@Theme theme: Int) {
        adapter.theme = theme

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        disableButton?.setOnClickListener { openState.tryInvoke { iViewModel.onClickDisable() } }
        postponeButton?.setOnClickListener { openState.tryInvoke { iViewModel.onClickPostpone() } }
    }

    override fun setupPlayer(volume: Int, increase: Boolean, uri: Uri) = with(iMelodyControl) {
        setupVolume(volume, increase)
        setupPlayer(uri, isLooping = true)
    }

    override fun notifyDataSetChanged(noteModel: NoteModel) =
            adapter.notifyDataSetChanged(arrayListOf(noteModel))


    override fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int) {
        logoView?.let { rippleContainer?.setupAnimation(theme, fillColor, it)?.startAnimation() }
    }

    override fun startButtonFadeInAnimation() {
        parentContainer?.let { group ->
            TransitionManager.beginDelayedTransition(group, Fade().apply {
                startDelay = 500
                duration = 500

                recyclerView?.let { addTarget(it) }
                buttonContainer?.let { addTarget(it) }
            })
        }

        recyclerView?.visibility = View.VISIBLE
        buttonContainer?.visibility = View.VISIBLE
    }

    override fun startNoteActivity(noteEntity: NoteEntity) {
        startActivity(NoteActivity[this, noteEntity])
    }


    override fun melodyStart() = iMelodyControl.start()

    override fun melodyStop() = iMelodyControl.stop()

    override fun vibrateStart(pattern: LongArray) = iVibratorControl.start(pattern)

    override fun vibrateCancel() = iVibratorControl.cancel()

    override fun setAlarm(calendar: Calendar, model: AlarmReceiver.Model) {
        iAlarmControl.set(calendar, model)
    }

    override fun showPostponeToast(select: Int) {
        showToast(getString(R.string.toast_alarm_postpone, resources.getStringArray(R.array.text_alarm_repeat)[select]))
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
        operator fun get(context: Context, id: Long, @Color color: Int): Intent =
                Intent(context, AlarmActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.COLOR, color)
    }

}