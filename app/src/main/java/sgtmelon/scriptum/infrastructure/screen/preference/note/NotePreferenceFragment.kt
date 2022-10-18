package sgtmelon.scriptum.infrastructure.screen.preference.note

import android.os.Bundle
import android.view.View
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener

/**
 * Fragment of note preferences.
 */
class NotePreferenceFragment : ParentPreferenceFragment() {

    override val xmlId: Int = R.xml.preference_note

    private val binding = NotePreferenceDataBinding(lifecycle, fragment = this)

    @Inject lateinit var viewModel: NotePreferenceViewModel

    private val dialogs by lazy { DialogFactory.Preference.Notes(context, fm) }
    private val sortDialog by lazy { dialogs.getSort() }
    private val colorDialog by lazy { dialogs.getColor() }
    private val savePeriodDialog by lazy { dialogs.getSavePeriod() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getNotePrefBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setup() {
        super.setup()
        binding.sortButton?.setOnClickListener { showSortDialog(viewModel.sort) }
        binding.colorButton?.setOnClickListener { showDefaultColorDialog(viewModel.defaultColor) }
        binding.savePeriodButton?.setOnClickListener { showSavePeriodDialog(viewModel.savePeriod) }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.sortSummary.observe(this) { binding.sortButton?.summary = it }
        viewModel.defaultColorSummary.observe(this) { binding.colorButton?.summary = it }
        viewModel.savePeriodSummary.observe(this) { binding.savePeriodButton?.summary = it }
    }

    private fun setupDialogs() {
        sortDialog.apply {
            onPositiveClick {
                viewModel.updateSort(sortDialog.check)
                delegators.broadcast.sendNotifyNotesBind()
            }
            onDismiss { open.clear() }
        }

        colorDialog.apply {
            onPositiveClick { viewModel.updateDefaultColor(colorDialog.check) }
            onDismiss { open.clear() }
        }

        savePeriodDialog.apply {
            onPositiveClick { viewModel.updateSavePeriod(savePeriodDialog.check) }
            onDismiss { open.clear() }
        }
    }

    private fun showSortDialog(sort: Sort) = open.attempt {
        sortDialog.setArguments(sort.ordinal)
            .safeShow(fm, DialogFactory.Preference.Notes.SORT, owner = this)
    }

    private fun showDefaultColorDialog(color: Color) = open.attempt {
        colorDialog.setArguments(color)
            .safeShow(fm, DialogFactory.Preference.Notes.COLOR, owner = this)
    }

    private fun showSavePeriodDialog(savePeriod: SavePeriod) = open.attempt {
        savePeriodDialog.setArguments(savePeriod.ordinal)
            .safeShow(fm, DialogFactory.Preference.Notes.SAVE_PERIOD, owner = this)
    }
}