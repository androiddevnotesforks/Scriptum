package sgtmelon.scriptum.infrastructure.screen.note

import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.databinding.ActivityNoteBinding
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.ShowPlaceholder
import sgtmelon.scriptum.infrastructure.utils.extensions.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.scriptum.infrastructure.utils.extensions.updateMargin
import sgtmelon.scriptum.infrastructure.utils.tint.TintNotePlaceholder

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

    private val showPlaceholder = ShowPlaceholder(lifecycle, context = this)
    private val tintPlaceholder = TintNotePlaceholder(context = this)

    private val unbindNoteReceiver = UnbindNoteReceiver[this]

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Call it after super function because it must be injected */
        bundleProvider.getData(bundle = savedInstanceState ?: intent.extras)

        val (id, type, color) = bundleProvider.data

        /**
         * Checkout all needed data for display note screen. If something goes wrong - report
         * and close screen.
         */
        if (id == null || type == null || color == null) {
            recordDataException(id, type, color)
            finish()
            return
        }

        /** Means this activity was rotated or something like that, and need to check cache. */
        val checkCache = savedInstanceState != null

        updateHolder(color)
        showFragment(type, checkCache)
    }

    private fun recordDataException(id: Long?, type: NoteType?, color: Color?) {
        val description = "Null values on create: " +
                "id=${id == null}, type=${type == null}, color=${color == null}"

        NullPointerException(description).record()
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
        val catchBackPress = when (bundleProvider.data.second) {
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

    private fun updateHolder(color: Color) {
        tintPlaceholder.changeColor(color, window, binding?.toolbarHolder)
    }

    /**
     * [checkCache] - find fragment by tag or create new.
     */
    private fun showFragment(type: NoteType, checkCache: Boolean) {
        when (type) {
            NoteType.TEXT -> showTextFragment(checkCache)
            NoteType.ROLL -> showRollFragment(checkCache)
        }
    }

    private fun showTextFragment(checkCache: Boolean) {
        val fragment = (if (checkCache) textNoteFragment else null) ?: TextNoteFragment()
        showFragment(fragment, FragmentFactory.Note.Tag.TEXT)
    }

    private fun showRollFragment(checkCache: Boolean) {
        val fragment = (if (checkCache) rollNoteFragment else null) ?: RollNoteFragment()
        showFragment(fragment, FragmentFactory.Note.Tag.ROLL)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        showPlaceholder.start(binding?.toolbarHolder, binding?.panelHolder)

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

    override fun convertNote() {
        /** We already checkout type value inside [onCreate] func (just skip it). */
        val type = bundleProvider.data.second ?: return
        val newType = viewModel.convertType(type)

        bundleProvider.updateType(newType)
        showFragment(newType, checkCache = true)
    }

    override fun isOrientationChanging(): Boolean = isChangingConfigurations

    override fun onReceiveUnbindNote(noteId: Long) {
        textNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
        rollNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
    }
}