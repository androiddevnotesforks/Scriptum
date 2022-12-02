package sgtmelon.scriptum.infrastructure.screen.note

import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.show.ShowPlaceholderDelegator
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.TintNotePlaceholderDelegator
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.databinding.ActivityNoteBinding
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.updateMargin

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment].
 */
class NoteActivity : ThemeActivity<ActivityNoteBinding>(),
    INoteConnector,
    UnbindNoteReceiver.Callback {

    override val layoutId: Int = R.layout.activity_note

    @Inject lateinit var viewModel: NoteViewModel
    @Inject lateinit var bundleProvider: NoteBundleProvider

    private val fragments = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragments.getTextNote()
    private val rollNoteFragment get() = fragments.getRollNote()

    // TODO Add control here

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[this] }

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = savedInstanceState ?: intent.extras
        bundleProvider.getData(bundle, viewModel.defaultColor)

        val (id, type, color) = bundleProvider.values

        if (id == null || type == null || color == null) {
            finish()
            return
        }

        /** Means this activity was rotated or something like that, and need check some cache. */
        val checkCache = savedInstanceState != null

        updateHolder(color)
        showFragment(id, type, color, checkCache)
    }

    override fun inject(component: ScriptumComponent) {
        component.getNoteBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

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

    override fun registerReceivers() {
        super.registerReceivers()
        registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        unregisterReceiver(unbindNoteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleProvider.saveData(outState)
    }

    override fun onBackPressed() {
        val catchBackPress = when (bundleProvider.type) {
            NoteType.TEXT -> textNoteFragment?.onPressBack() ?: false
            NoteType.ROLL -> rollNoteFragment?.onPressBack() ?: false
            null -> false
        }

        /** If back press was caught by child fragments - don't call activity back press. */
        if (!catchBackPress) {
            super.onBackPressed()
        }
    }

    //endregion

    private fun updateHolder(color: Color) = tintPlaceholder.changeColor(color)

    /**
     * [checkCache] - find fragment by tag or create new.
     */
    private fun showFragment(id: Long, type: NoteType, color: Color, checkCache: Boolean) {
        when (type) {
            NoteType.TEXT -> showTextFragment(id, color, checkCache)
            NoteType.ROLL -> showRollFragment(id, color, checkCache)
        }
    }

    private fun showTextFragment(id: Long, color: Color, checkCache: Boolean) {
        val fragment = (if (checkCache) textNoteFragment else null) ?: TextNoteFragment[id, color]
        showFragment(fragment, FragmentFactory.Note.Tag.TEXT)
    }

    private fun showRollFragment(id: Long, color: Color, checkCache: Boolean) {
        val fragment = (if (checkCache) rollNoteFragment else null) ?: RollNoteFragment[id, color]
        showFragment(fragment, FragmentFactory.Note.Tag.ROLL)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        showPlaceholder.start()

        lifecycleScope.launchWhenResumed {
            fm.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .replace(R.id.fragment_container, fragment, tag)
                .commit()
        }
    }

    override fun updateNoteId(id: Long) = bundleProvider.updateId(id)

    override fun updateNoteColor(color: Color) {
        bundleProvider.updateColor(color)
        updateHolder(color)
    }

    override fun convertNote() = with(bundleProvider) {
        val id = id
        val convertType = type?.let { viewModel.convertType(it) }
        val color = color

        if (id == null || convertType == null || color == null) {
            finish()
            return
        }

        showFragment(id, convertType, color, checkCache = true)
    }

    //region cleanup

    private val showPlaceholder by lazy {
        ShowPlaceholderDelegator(lifecycle, resources, binding?.toolbarHolder, binding?.panelHolder)
    }
    private val tintPlaceholder by lazy {
        TintNotePlaceholderDelegator(context = this, window, binding?.toolbarHolder)
    }

    // TODO improve it, i don't think it's work correct with split screen for example
    override fun isOrientationChanging(): Boolean = isChangingConfigurations

    //endregion

    override fun onReceiveUnbindNote(noteId: Long) {
        textNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
        rollNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
    }
}