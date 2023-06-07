package sgtmelon.scriptum.infrastructure.screen.preference.note

import android.os.Bundle
import android.view.View
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.utils.DialogStorage
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.dialog.ColorDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener

/**
 * Fragment of notes preferences.
 */
class NotesPreferenceFragment : PreferenceFragment() {

    override val xmlId: Int = R.xml.preference_notes

    private val binding = NotesPreferenceBinding(fragment = this)

    @Inject lateinit var viewModel: NotesPreferenceViewModel

    private val dialogs by lazy { DialogFactory.Preference.Notes(resources) }
    private val sortDialog = DialogStorage(
        DialogFactory.Preference.Notes.SORT, owner = this,
        create = { dialogs.getSort() },
        setup = { setupSortDialog(it) }
    )
    private val colorDialog = DialogStorage(
        DialogFactory.Preference.Notes.COLOR, owner = this,
        create = { dialogs.getColor() },
        setup = { setupColorDialog(it) }
    )
    private val savePeriodDialog = DialogStorage(
        DialogFactory.Preference.Notes.SAVE_PERIOD, owner = this,
        create = { dialogs.getSavePeriod() },
        setup = { setupSavePeriodDialog(it) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getNotesPrefBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setup() {
        binding.sortButton?.setOnClickListener { showSortDialog(viewModel.sort) }
        binding.colorButton?.setOnClickListener { showDefaultColorDialog(viewModel.defaultColor) }
        binding.savePeriodButton?.setOnClickListener { showSavePeriodDialog(viewModel.savePeriod) }
    }

    override fun setupObservers() {
        viewModel.sortSummary.observe(this) { binding.sortButton?.summary = it }
        viewModel.defaultColorSummary.observe(this) { binding.colorButton?.summary = it }
        viewModel.savePeriodSummary.observe(this) { binding.savePeriodButton?.summary = it }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        sortDialog.restore()
        colorDialog.restore()
        savePeriodDialog.restore()
    }

    private fun setupSortDialog(dialog: SingleDialog): Unit = with(dialog) {
        onPositiveClick {
            viewModel.updateSort(check)
            system?.broadcast?.sendNotifyNotesBind()
        }
        onDismiss {
            sortDialog.release()
            open.clear()
        }
    }

    private fun setupColorDialog(dialog: ColorDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.updateDefaultColor(check) }
        onDismiss {
            colorDialog.release()
            open.clear()
        }
    }

    private fun setupSavePeriodDialog(dialog: SingleDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.updateSavePeriod(check) }
        onDismiss {
            savePeriodDialog.release()
            open.clear()
        }
    }

    private fun showSortDialog(sort: Sort) = open.attempt {
        sortDialog.show { setArguments(sort.ordinal) }
    }

    private fun showDefaultColorDialog(color: Color) = open.attempt {
        colorDialog.show { setArguments(color) }
    }

    private fun showSavePeriodDialog(savePeriod: SavePeriod) = open.attempt {
        savePeriodDialog.show { setArguments(savePeriod.ordinal) }
    }
}