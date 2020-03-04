package sgtmelon.scriptum.screen.ui.note

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.ShowHolderControl
import sgtmelon.scriptum.control.menu.ToolbarTintControl
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.ScriptumApplication
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import javax.inject.Inject

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment]
 */
class NoteActivity : AppActivity(), INoteActivity, INoteChild, NoteReceiver.Callback {

    @Inject internal lateinit var viewModel: INoteViewModel

    private val holderControl by lazy { ShowHolderControl(arrayOf(toolbarHolder, panelHolder)) }

    private val fragmentFactory = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragmentFactory.getTextNoteFragment()
    private val rollNoteFragment get() = fragmentFactory.getRollNoteFragment()

    private val noteReceiver by lazy { NoteReceiver(callback = this) }

    private val toolbarHolder by lazy { findViewById<View?>(R.id.note_toolbar_holder) }
    private val panelHolder by lazy { findViewById<View?>(R.id.note_panel_holder) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getNoteBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel.apply {
            onSetup(bundle = savedInstanceState ?: intent.extras)
            onSetupFragment(checkCache = savedInstanceState != null)
        }

        registerReceiver(noteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun onDestroy() {
        super.onDestroy()

        holderControl.onDestroy()

        viewModel.onDestroy()
        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply { viewModel.onSaveData(bundle = this) })
    }

    override fun onBackPressed() {
        if (!viewModel.onPressBack()) super.onBackPressed()
    }


    override fun updateHolder(theme: Int, color: Int) {
        if (theme == Theme.DARK) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ToolbarTintControl.getStatusBarColor(this, theme, color)
        }

        toolbarHolder?.setBackgroundColor(ToolbarTintControl.getToolbarColor(this, theme, color))
    }

    override fun showTextFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.TEXT, if (checkCache) {
            textNoteFragment ?: TextNoteFragment[id, color]
        } else {
            TextNoteFragment[id, color]
        })
    }

    override fun showRollFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.ROLL, if (checkCache) {
            rollNoteFragment ?: RollNoteFragment[id, color]
        } else {
            RollNoteFragment[id, color]
        })
    }

    override fun onPressBackText() = textNoteFragment?.onPressBack() ?: false

    override fun onPressBackRoll() = rollNoteFragment?.onPressBack() ?: false

    override fun onReceiveUnbindNote(id: Long) {
        textNoteFragment?.onReceiveUnbindNote(id)
        rollNoteFragment?.onReceiveUnbindNote(id)
    }

    override fun onUpdateNoteId(id: Long) = viewModel.onUpdateNoteId(id)

    override fun onUpdateNoteColor(@Color color: Int) = viewModel.onUpdateNoteColor(color)

    override fun onConvertNote() = viewModel.onConvertNote()


    private fun showFragment(key: String, fragment: Fragment) {
        holderControl.show()

        fm.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.note_fragment_container, fragment, key)
                .commit()
    }

    companion object {
        operator fun get(context: Context, item: NotificationItem) =
                get(context, item.note.type.ordinal, item.note.id, item.note.color)

        operator fun get(context: Context, item: NoteItem) =
                get(context, item.type.ordinal, item.id, item.color)

        /**
         * If [id] and [color] isDefault - it means that note will be create, not open.
         */
        operator fun get(context: Context, type: Int,
                         id: Long = NoteData.Default.ID,
                         @Color color: Int = NoteData.Default.COLOR): Intent {
            return Intent(context, NoteActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
                    .putExtra(NoteData.Intent.COLOR, color)
                    .putExtra(NoteData.Intent.TYPE, type)
        }
    }

}