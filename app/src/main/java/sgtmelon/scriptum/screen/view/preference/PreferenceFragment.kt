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

    private val sortDialog by lazy { DialogFactory.getSortDialog(activity, fm) }
    private val colorDialog by lazy { DialogFactory.getColorDialog(fm) }
    private val infoDialog by lazy { DialogFactory.getInfoDialog(fm) }
    private val saveTimeDialog by lazy { DialogFactory.getSaveTimeDialog(activity, fm) }
    private val themeDialog by lazy { DialogFactory.getThemeDialog(activity, fm) }

    private val sortPreference: Preference by lazy { findPreference(getString(R.string.key_note_sort)) }
    private val colorPreference: Preference by lazy { findPreference(getString(R.string.key_note_color)) }
    private val saveTimePreference: Preference by lazy { findPreference(getString(R.string.key_save_time)) }
    private val themePreference: Preference by lazy { findPreference(getString(R.string.key_app_theme)) }

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
        themePreference.setOnPreferenceClickListener { viewModel.onClickThemePreference() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultThemeDialog(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupNotification() {
        // TODO #RELEASE
    }

    private fun setupNote() {
        sortPreference.setOnPreferenceClickListener { viewModel.onClickSortPreference() }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSortDialog(sortDialog.check)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference.setOnPreferenceClickListener { viewModel.onClickColorPreference() }

        colorDialog.title = getString(R.string.title_note_color)
        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultColorDialog(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupSave() {
        saveTimePreference.setOnPreferenceClickListener { viewModel.onClickSaveTimePreference() }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTimeDialog(saveTimeDialog.check)
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
            openState.tryInvoke { infoDialog.show(fm, DialogFactory.Key.INFO) }
            return@setOnPreferenceClickListener true
        }

        infoDialog.apply {
            logoClick = View.OnClickListener {
                startActivity(Intent(activity, DevelopActivity::class.java))
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun updateThemePrefSummary(summary: String) {
        themePreference.summary = summary
    }

    override fun showThemeDialog(@Theme value: Int) = openState.tryInvoke {
        themeDialog.apply { setArguments(value) }.show(fm, DialogFactory.Key.THEME)
    }

    override fun updateSortSummary(summary: String) {
        sortPreference.summary = summary
    }

    override fun showSortDialog(value: Int) = openState.tryInvoke {
        sortDialog.apply { setArguments(value) }.show(fm, DialogFactory.Key.SORT)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference.summary = summary
    }

    override fun showColorDialog(@Color value: Int) = openState.tryInvoke {
        colorDialog.apply { setArguments(value) }.show(fm, DialogFactory.Key.COLOR)
    }

    override fun updateSaveTimeSummary(summary: String) {
        saveTimePreference.summary = summary
    }

    override fun showSaveTimeDialog(value: Int) = openState.tryInvoke {
        saveTimeDialog.apply { setArguments(value) }.show(fm, DialogFactory.Key.SAVE_TIME)
    }

}