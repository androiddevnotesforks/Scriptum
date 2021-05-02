package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.DialogInterface
import android.os.Bundle
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.INotePrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePrefViewModel
import javax.inject.Inject

/**
 * Fragment of note preferences.
 */
class NotePrefFragment : ParentPreferenceFragment(), INotePrefFragment {

    @Inject internal lateinit var viewModel: INotePrefViewModel

    private val openState = OpenState()

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference(context, fm) }

    private val sortDialog by lazy { dialogFactory.getSortDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }
    private val savePeriodDialog by lazy { dialogFactory.getSavePeriodDialog() }

    //endregion

    //region Preferences

    private val sortPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_sort)) }
    private val colorPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_color)) }
    private val savePeriodPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_time)) }

    //endregion

    private val broadcastControl by lazy { BroadcastControl[context] }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_note, rootKey)

        ScriptumApplication.component.getNotePrefBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        broadcastControl.initLazy()

        openState.get(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    override fun setup() {
        sortPreference?.setOnPreferenceClickListener {
            viewModel.onClickSort()
            return@setOnPreferenceClickListener true
        }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteSort(sortDialog.check)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference?.setOnPreferenceClickListener {
            viewModel.onClickNoteColor()
            return@setOnPreferenceClickListener true
        }

        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteColor(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        savePeriodPreference?.setOnPreferenceClickListener {
            viewModel.onClickSaveTime()
            return@setOnPreferenceClickListener true
        }

        savePeriodDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTime(savePeriodDialog.check)
        }
        savePeriodDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun updateSortSummary(summary: String?) {
        sortPreference?.summary = summary
    }

    override fun showSortDialog(@Sort value: Int) = openState.tryInvoke {
        sortDialog.setArguments(value).show(fm, DialogFactory.Preference.SORT)
    }

    override fun updateColorSummary(summary: String?) {
        colorPreference?.summary = summary
    }

    override fun showColorDialog(@Color color: Int) = openState.tryInvoke {
        colorDialog.setArguments(color).show(fm, DialogFactory.Preference.COLOR)
    }

    override fun updateSavePeriodSummary(summary: String?) {
        savePeriodPreference?.summary = summary
    }

    override fun showSaveTimeDialog(value: Int) = openState.tryInvoke {
        savePeriodDialog.setArguments(value).show(fm, DialogFactory.Preference.SAVE_PERIOD)
    }

    //region Broadcast functions

    override fun sendNotifyNotesBroadcast() = broadcastControl.sendNotifyNotesBind()

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