package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.os.Bundle
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.INotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Fragment of note preferences.
 */
class NotePreferenceFragment : ParentPreferenceFragment(), INotePreferenceFragment {

    @Inject lateinit var viewModel: INotePreferenceViewModel

    //region Dialogs

    private val dialogs by lazy { DialogFactory.Preference.Notes(context, fm) }

    private val sortDialog by lazy { dialogs.getSortDialog() }
    private val colorDialog by lazy { dialogs.getColorDialog() }
    private val savePeriodDialog by lazy { dialogs.getSavePeriodDialog() }

    //endregion

    //region Preferences

    private val sortPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_sort)) }
    private val colorPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_color)) }
    private val savePeriodPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_time)) }

    //endregion

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_note, rootKey)

        ScriptumApplication.component.getNotePrefBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //endregion

    override fun setup() {
        sortPreference?.setOnPreferenceClickListener {
            viewModel.onClickSort()
            return@setOnPreferenceClickListener true
        }

        sortDialog.apply {
            onPositiveClick { viewModel.onResultNoteSort(sortDialog.check) }
            onDismiss { open.clear() }
        }

        colorPreference?.setOnPreferenceClickListener {
            viewModel.onClickNoteColor()
            return@setOnPreferenceClickListener true
        }

        colorDialog.apply {
            onPositiveClick { viewModel.onResultNoteColor(colorDialog.check) }
            onDismiss { open.clear() }
        }

        savePeriodPreference?.setOnPreferenceClickListener {
            viewModel.onClickSaveTime()
            return@setOnPreferenceClickListener true
        }

        savePeriodDialog.apply {
            onPositiveClick { viewModel.onResultSaveTime(savePeriodDialog.check) }
            onDismiss { open.clear() }
        }
    }

    override fun updateSortSummary(summary: String) {
        sortPreference?.summary = summary
    }

    override fun showSortDialog(sort: Sort) = open.attempt {
        sortDialog.setArguments(sort.ordinal)
            .safeShow(fm, DialogFactory.Preference.Notes.SORT, owner = this)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference?.summary = summary
    }

    override fun showColorDialog(color: Color) = open.attempt {
        colorDialog.setArguments(color)
            .safeShow(fm, DialogFactory.Preference.Notes.COLOR, owner = this)
    }

    override fun updateSavePeriodSummary(summary: String?) {
        savePeriodPreference?.summary = summary
    }

    override fun showSaveTimeDialog(savePeriod: SavePeriod) = open.attempt {
        savePeriodDialog.setArguments(savePeriod.ordinal)
            .safeShow(fm, DialogFactory.Preference.Notes.SAVE_PERIOD, owner = this)
    }

    //region Broadcast functions

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    /**
     * Not used here.
     */
    override fun sendNotifyInfoBroadcast(count: Int?) = Unit

    //endregion

}