package sgtmelon.scriptum.presentation.screen.ui.impl.note

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.InsetsDir
import sgtmelon.scriptum.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.updateMargin
import sgtmelon.scriptum.presentation.control.toolbar.show.HolderShowControl
import sgtmelon.scriptum.presentation.control.toolbar.tint.HolderTintControl
import sgtmelon.scriptum.presentation.factory.FragmentFactory
import sgtmelon.scriptum.presentation.receiver.screen.NoteScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel
import javax.inject.Inject

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment].
 */
class NoteActivity : AppActivity(), INoteActivity, INoteConnector, NoteScreenReceiver.Callback {

    //region Variables

    @Inject internal lateinit var viewModel: INoteViewModel

    private val holderShowControl by lazy { HolderShowControl[toolbarHolder, panelHolder] }
    private val holderTintControl by lazy { HolderTintControl[this, window, toolbarHolder] }

    private val fragmentFactory = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragmentFactory.getTextNoteFragment()
    private val rollNoteFragment get() = fragmentFactory.getRollNoteFragment()

    private val noteReceiver by lazy { NoteScreenReceiver[this] }

    private val parentContainer by lazy { findViewById<View?>(R.id.note_parent_container) }
    private val toolbarHolder by lazy { findViewById<View?>(R.id.note_toolbar_holder) }
    private val panelHolder by lazy { findViewById<View?>(R.id.note_panel_holder) }

    //endregion

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getNoteBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        holderTintControl.initLazy()

        viewModel.apply {
            onSetup(bundle = savedInstanceState ?: intent.extras)
            onSetupFragment(checkCache = savedInstanceState != null)
        }

        registerReceiver(noteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun onDestroy() {
        super.onDestroy()

        holderShowControl.onDestroy()

        viewModel.onDestroy()
        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun onBackPressed() {
        if (!viewModel.onPressBack()) super.onBackPressed()
    }

    //endregion

    override fun updateHolder(@Color color: Int) = holderTintControl.setupColor(color)

    override fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, isFirstTime, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin, !isFirstTime)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun showTextFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.Tag.TEXT, if (checkCache) {
            textNoteFragment ?: TextNoteFragment[id, color]
        } else {
            TextNoteFragment[id, color]
        })
    }

    override fun showRollFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.Tag.ROLL, if (checkCache) {
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

    //region Parent callback

    override fun onUpdateNoteId(id: Long) = viewModel.onUpdateNoteId(id)

    override fun onUpdateNoteColor(@Color color: Int) = viewModel.onUpdateNoteColor(color)

    override fun onConvertNote() = viewModel.onConvertNote()

    override fun isOrientationChanging(): Boolean = isChangingConfigurations

    //endregion

    private fun showFragment(@FragmentFactory.Note.Tag key: String, fragment: Fragment) {
        holderShowControl.show()

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
        operator fun get(
            context: Context,
            type: Int,
            id: Long = Note.Default.ID,
            @Color color: Int = Note.Default.COLOR
        ): Intent {
            return Intent(context, NoteActivity::class.java)
                .putExtra(Note.Intent.ID, id)
                .putExtra(Note.Intent.COLOR, color)
                .putExtra(Note.Intent.TYPE, type)
        }
    }

}