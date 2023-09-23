package sgtmelon.scriptum.infrastructure.screen.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.ActivityNoteBinding
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.json.BundleNoteValue
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.ShowPlaceholder
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.updateMargin
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.infrastructure.utils.tint.TintNotePlaceholder

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment].
 */
class NoteActivity : ThemeActivity<ActivityNoteBinding>(),
    NoteConnector,
    UnbindNoteReceiver.Callback {

    override val layoutId: Int = R.layout.activity_note

    private val initBundle = BundleNoteValue()
    override val bundleValues: List<BundleValue> = listOf(initBundle)

    override val init: NoteInit get() = initBundle.value

    private val fragments = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragments.getTextNote()
    private val rollNoteFragment get() = fragments.getRollNote()

    private val showPlaceholder = ShowPlaceholder(lifecycle, context = this)
    private val tintPlaceholder = TintNotePlaceholder(context = this)

    private val unbindNoteReceiver = UnbindNoteReceiver[this]
    override val receiverFilter = ReceiverFilter.NOTE
    override val receiverList get() = listOf(unbindNoteReceiver)

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Means this activity was rotated or something like that, and need to check cache. */
        val checkCache = savedInstanceState != null

        updateHolder(init.noteItem.color)
        showFragment(checkCache)
    }

    override fun inject(component: ScriptumComponent) {
        component.getNoteBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun onBackPressed() {
        val catchBackPress = when (init.noteItem.type) {
            NoteType.TEXT -> textNoteFragment?.onPressBack() ?: false
            NoteType.ROLL -> rollNoteFragment?.onPressBack() ?: false
        }

        /** If back press was caught by child fragments - don't call activity back press. */
        if (!catchBackPress) {
            super.onBackPressed()
        }
    }

    //endregion

    override fun updateHolder(color: Color) {
        tintPlaceholder.changeColor(color, window, binding?.toolbarHolder)
    }

    /** [checkCache] - find fragment by tag or create new. */
    private fun showFragment(checkCache: Boolean) {
        when (init.noteItem.type) {
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

    override fun convertNote(item: NoteItem) {
        init.noteItem = item
        showFragment(checkCache = true)
    }

    override fun isOrientationChanging(): Boolean = isChangingConfigurations

    override fun onReceiveUnbindNote(noteId: Long) {
        textNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
        rollNoteFragment?.viewModel?.onReceiveUnbindNote(noteId)
    }

}