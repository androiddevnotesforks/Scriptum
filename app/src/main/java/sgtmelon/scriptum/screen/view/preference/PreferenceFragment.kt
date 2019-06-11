package sgtmelon.scriptum.screen.view.preference

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.callback.PreferenceCallback
import sgtmelon.scriptum.screen.view.DevelopActivity
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import android.preference.PreferenceFragment as OldPreferenceFragment

/**
 * Экран настроек приложения
 *
 * @author SerjantArbuz
 */
class PreferenceFragment : OldPreferenceFragment(), PreferenceCallback {

    private val activity: PreferenceActivity by lazy { getActivity() as PreferenceActivity }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    private val viewModel: PreferenceViewModel by lazy { PreferenceViewModel(activity, this) }

    private val openState = OpenState()

    private val themeDialog by lazy { DialogFactory.Preference.getThemeDialog(activity, fm) }
    private val repeatDialog by lazy { DialogFactory.Preference.getRepeatDialog(activity, fm) }
    private val signalDialog by lazy { DialogFactory.Preference.getSignalDialog(activity, fm) }
    private val sortDialog by lazy { DialogFactory.Preference.getSortDialog(activity, fm) }
    private val colorDialog by lazy { DialogFactory.Preference.getColorDialog(activity, fm) }
    private val saveTimeDialog by lazy { DialogFactory.Preference.getSaveTimeDialog(activity, fm) }
    private val infoDialog by lazy { DialogFactory.Preference.getInfoDialog(fm) }

    private val themePreference: Preference by lazy { findPreference(getString(R.string.key_app_theme)) }

    private val repeatPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_repeat)) }
    private val signalPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_signal)) }
    private val melodyPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_melody)) }
    private val increasePreference: Preference by lazy { findPreference(getString(R.string.key_alarm_increase)) }
    private val volumePreference: Preference by lazy { findPreference(getString(R.string.key_alarm_volume)) }

    private val sortPreference: Preference by lazy { findPreference(getString(R.string.key_note_sort)) }
    private val colorPreference: Preference by lazy { findPreference(getString(R.string.key_note_color)) }
    private val saveTimePreference: Preference by lazy { findPreference(getString(R.string.key_save_time)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            super.onCreateView(inflater, container, savedInstanceState)?.apply {
                findViewById<View>(android.R.id.list).setPadding(0, 0, 0, 0)
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        openState.get(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.updateSummary()

        setupApp()
        setupNotification()
        setupNote()
        setupSave()
        setupOther()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    private fun setupApp() {
        themePreference.setOnPreferenceClickListener { viewModel.onClickTheme() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultTheme(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    // TODO #RELEASE
    private fun setupNotification() {
        repeatPreference.setOnPreferenceClickListener { viewModel.onClickRepeat() }

        repeatDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultRepeat(repeatDialog.check)
        }
        repeatDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        signalPreference.setOnPreferenceClickListener { viewModel.onClickSignal() }

        signalDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSignal(signalDialog.check)
        }
        signalDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupNote() {
        sortPreference.setOnPreferenceClickListener { viewModel.onClickSort() }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteSort(sortDialog.check)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference.setOnPreferenceClickListener { viewModel.onClickNoteColor() }

        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteColor(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupSave() {
        saveTimePreference.setOnPreferenceClickListener { viewModel.onClickSaveTime() }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTime(saveTimeDialog.check)
        }
        saveTimeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        val autoSavePreference = findPreference(getString(R.string.key_save_auto)) as? CheckBoxPreference
        autoSavePreference?.setOnPreferenceChangeListener { _, newValue ->
            saveTimePreference.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        saveTimePreference.isEnabled = autoSavePreference?.isChecked == true
    }

    private fun setupOther() {
        findPreference(getString(R.string.key_other_rate)).setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            try {
                intent.data = Uri.parse(BuildConfig.MARKET_URL + activity.packageName)
                startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                intent.data = Uri.parse(BuildConfig.BROWSER_URL + activity.packageName)
                startActivity(intent)
            }

            return@setOnPreferenceClickListener true
        }

        findPreference(getString(R.string.key_other_about)).setOnPreferenceClickListener {
            openState.tryInvoke { infoDialog.show(fm, DialogFactory.Preference.INFO) }
            return@setOnPreferenceClickListener true
        }

        infoDialog.apply {
            logoClick = View.OnClickListener {
                startActivity(Intent(activity, DevelopActivity::class.java))
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun updateThemeSummary(summary: String) {
        themePreference.summary = summary
    }

    override fun showThemeDialog(@Theme value: Int) = openState.tryInvoke {
        themeDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.THEME)
    }

    override fun updateRepeatSummary(summary: String) {
        repeatPreference.summary = summary
    }

    override fun showRepeatDialog(value: Int) = openState.tryInvoke {
        repeatDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.REPEAT)
    }

    override fun updateSignalSummary(summary: String) {
        signalPreference.summary = summary
    }

    override fun showSignalDialog(value: BooleanArray) = openState.tryInvoke {
        signalDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.SIGNAL)
    }

    override fun updateMelodyGroupEnabled(enabled: Boolean) {
        melodyPreference.isEnabled = enabled
        increasePreference.isEnabled = enabled
        volumePreference.isEnabled = enabled
    }

    override fun updateSortSummary(summary: String) {
        sortPreference.summary = summary
    }

    override fun showSortDialog(value: Int) = openState.tryInvoke {
        sortDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.SORT)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference.summary = summary
    }

    override fun showColorDialog(@Color value: Int) = openState.tryInvoke {
        colorDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.COLOR)
    }

    override fun updateSaveTimeSummary(summary: String) {
        saveTimePreference.summary = summary
    }

    override fun showSaveTimeDialog(value: Int) = openState.tryInvoke {
        saveTimeDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.SAVE_TIME)
    }

}