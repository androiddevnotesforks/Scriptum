package sgtmelon.scriptum.infrastructure.screen.note

import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.show.HolderShowControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.HolderTintControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.databinding.ActivityNoteBinding
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.updateMargin

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment].
 */
class NoteActivity : ThemeActivity<ActivityNoteBinding>(),
    INoteActivity,
    INoteConnector,
    UnbindNoteReceiver.Callback {

    override val layoutId: Int = R.layout.activity_note

    //region Variables

    @Inject lateinit var viewModel: NoteViewModel

    private val holderShowControl by lazy {
        HolderShowControl[binding?.toolbarHolder, binding?.panelHolder]
    }
    private val holderTintControl by lazy {
        HolderTintControl[this, window, binding?.toolbarHolder]
    }

    private val fragments = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragments.getTextNote()
    private val rollNoteFragment get() = fragments.getRollNote()

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[this] }

    //endregion

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        holderTintControl.initLazy()

        viewModel.apply {
            onSetup(bundle = savedInstanceState ?: intent.extras)
            onSetupFragment(checkCache = savedInstanceState != null)
        }
    }

    override fun inject(component: ScriptumComponent) {
        component.getNoteBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun registerReceivers() {
        super.registerReceivers()
        registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        unregisterReceiver(unbindNoteReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()

        holderShowControl.onDestroy()

        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    override fun onBackPressed() {
        if (!viewModel.onPressBack()) super.onBackPressed()
    }

    //endregion

    override fun updateHolder(color: Color) = holderTintControl.setupColor(color)

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.doOnApplyWindowInsets { view, insets, isFirstTime, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            view.updateMargin(InsetsDir.BOTTOM, insets, margin, !isFirstTime)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun showTextFragment(id: Long, color: Color, checkCache: Boolean) {
        val fragment = (if (checkCache) textNoteFragment else null) ?: TextNoteFragment[id, color]
        showFragment(fragment, FragmentFactory.Note.Tag.TEXT)
    }

    override fun showRollFragment(id: Long, color: Color, checkCache: Boolean) {
        val fragment = (if (checkCache) rollNoteFragment else null) ?: RollNoteFragment[id, color]
        showFragment(fragment, FragmentFactory.Note.Tag.ROLL)
    }

    override fun onPressBackText() = textNoteFragment?.onPressBack() ?: false

    override fun onPressBackRoll() = rollNoteFragment?.onPressBack() ?: false

    override fun onReceiveUnbindNote(noteId: Long) {
        textNoteFragment?.onReceiveUnbindNote(noteId)
        rollNoteFragment?.onReceiveUnbindNote(noteId)
    }

    //region Parent callback

    override fun onUpdateNoteId(id: Long) = viewModel.onUpdateNoteId(id)

    override fun onUpdateNoteColor(color: Color) = viewModel.onUpdateNoteColor(color)

    override fun onConvertNote() = viewModel.onConvertNote()

    override fun isOrientationChanging(): Boolean = isChangingConfigurations

    //endregion

    private fun showFragment(fragment: Fragment, tag: String) {
        holderShowControl.display()

        lifecycleScope.launchWhenResumed {
            fm.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .replace(R.id.fragment_container, fragment, tag)
                .commit()
        }
    }
}