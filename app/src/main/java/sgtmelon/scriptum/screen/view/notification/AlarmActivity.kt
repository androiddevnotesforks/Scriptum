package sgtmelon.scriptum.screen.view.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.screen.callback.notification.AlarmCallback
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Экран с уведомлением запущенным по таймеру
 *
 * @author SerjantArbuz
 */
class AlarmActivity : AppActivity(), AlarmCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AlarmViewModel::class.java).apply {
            callback = this@AlarmActivity
        }
    }

    private val openState = OpenState()

    private var adapter: NoteAdapter? = null

    private val parentContainer: ViewGroup? by lazy { findViewById<ViewGroup>(R.id.alarm_parent_container) }
    private val recyclerView: RecyclerView? by lazy { findViewById<RecyclerView>(R.id.alarm_recycler) }

    private val buttonContainer: ViewGroup? by lazy { findViewById<ViewGroup>(R.id.alarm_button_container) }
    private val disableButton: Button? by lazy { findViewById<Button>(R.id.alarm_disable_button) }
    private val postponeButton: Button? by lazy { findViewById<Button>(R.id.alarm_postpone_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        viewModel.apply {
            onSetup()
            onSetupData(bundle = savedInstanceState ?: intent.extras)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { viewModel.onSaveData(bundle = this) })

    override fun setupView(theme: Int) {
        adapter = NoteAdapter(theme, ItemListener.Click { _, _ ->
            openState.tryInvoke { viewModel.onClickNote() }
        })

        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }

        disableButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickDisable() } }
        postponeButton?.setOnClickListener { openState.tryInvoke { viewModel.onClickPostpone() } }
    }

    override fun notifyDataSetChanged(noteModel: NoteModel) {
        adapter?.notifyDataSetChanged(arrayListOf(noteModel))
    }

    override fun animateControlShow() {
        parentContainer?.let { group ->
            TransitionManager.beginDelayedTransition(group, Fade().setDuration(500).apply {
                recyclerView?.let { addTarget(it) }
                buttonContainer?.let { addTarget(it) }
            })
        }

        recyclerView?.visibility = View.VISIBLE
        buttonContainer?.visibility = View.VISIBLE
    }

    companion object {
        fun Context.getAlarmIntent(id: Long, color: Int): Intent =
                Intent(this, AlarmActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.COLOR, color)
    }

}