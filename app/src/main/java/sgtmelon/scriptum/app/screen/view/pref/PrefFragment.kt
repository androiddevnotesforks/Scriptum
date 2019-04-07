package sgtmelon.scriptum.app.screen.view.pref

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.state.OpenState
import sgtmelon.scriptum.app.screen.callback.PrefCallback
import sgtmelon.scriptum.app.screen.view.DevelopActivity
import sgtmelon.scriptum.app.screen.vm.PrefViewModel
import sgtmelon.scriptum.office.annot.def.DialogDef

/**
 * Экран настроек приложения
 *
 * @author SerjantArbuz
 */
class PrefFragment : PreferenceFragment(), PrefCallback {

    // TODO https://www.youtube.com/watch?v=PS9jhuHECEQ
    // TODO перенести theme на самый верх

    private val openState = OpenState()

    private val activity: PrefActivity by lazy { getActivity() as PrefActivity }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    private val viewModel: PrefViewModel by lazy { PrefViewModel(activity, this) }

    private val sortDialog by lazy { DialogFactory.getSortDialog(fm) }
    private val colorDialog by lazy { DialogFactory.getColorDialog(fm) }
    private val infoDialog by lazy { DialogFactory.getInfoDialog(fm) }
    private val saveTimeDialog by lazy { DialogFactory.getSaveTimeDialog(activity, fm) }
    private val themeDialog by lazy { DialogFactory.getThemeDialog(activity, fm) }

    private val sortPreference: Preference by lazy { findPreference(getString(R.string.pref_key_sort)) }
    private val colorPreference: Preference by lazy { findPreference(getString(R.string.pref_key_color)) }
    private val saveTimePreference: Preference by lazy { findPreference(getString(R.string.pref_key_save_time)) }
    private val themePreference: Preference by lazy { findPreference(getString(R.string.pref_key_theme)) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (view != null) {
            val list = view.findViewById<View>(android.R.id.list)
            list.setPadding(0, 0, 0, 0)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.updateSummary()

        setupNotePref()
        setupSavePref()
        setupAppPref()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { putBoolean(OpenState.KEY, openState.value) })

    private fun setupNotePref() {
        sortPreference.setOnPreferenceClickListener { viewModel.onClickSortPreference() }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSortDialog(sortDialog.keys)
        }
        sortDialog.neutralListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSortDialog()
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference.setOnPreferenceClickListener { viewModel.onClickColorPreference() }

        colorDialog.title = getString(R.string.pref_color_title)
        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultColorDialog(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupSavePref() {
        saveTimePreference.setOnPreferenceClickListener { viewModel.onClickSaveTimePreference() }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTimeDialog(saveTimeDialog.check)
        }
        saveTimeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        val autoSavePreference = findPreference(getString(R.string.pref_key_auto_save)) as? CheckBoxPreference
        autoSavePreference?.setOnPreferenceChangeListener { _, newValue ->
            saveTimePreference.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        saveTimePreference.isEnabled = autoSavePreference?.isChecked == true
    }

    private fun setupAppPref() {
        themePreference.setOnPreferenceClickListener { viewModel.onClickThemePreference() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultThemeDialog(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        findPreference(getString(R.string.pref_key_rate)).setOnPreferenceClickListener {
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

        findPreference(getString(R.string.pref_key_about)).setOnPreferenceClickListener {
            openState.tryInvoke { infoDialog.show(fm, DialogDef.INFO) }
            return@setOnPreferenceClickListener true
        }

        infoDialog.apply {
            logoClick = View.OnClickListener {
                startActivity(Intent(activity, DevelopActivity::class.java))
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun updateSortSummary(summary: String) {
        sortPreference.summary = summary
    }

    override fun showSortDialog(sortKeys: String) = openState.tryInvoke {
        sortDialog.apply { setArguments(sortKeys) }.show(fm, DialogDef.SORT)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference.summary = summary
    }

    override fun showColorDialog(check: Int) = openState.tryInvoke {
        colorDialog.apply { setArguments(check) }.show(fm, DialogDef.COLOR)
    }

    override fun updateSaveTimeSummary(summary: String) {
        saveTimePreference.summary = summary
    }

    override fun showSaveTimeDialog(check: Int) = openState.tryInvoke {
        saveTimeDialog.apply { setArguments(check) }.show(fm, DialogDef.SAVE_TIME)
    }

    override fun updateThemePrefSummary(summary: String) {
        themePreference.summary = summary
    }

    override fun showThemeDialog(check: Int) = openState.tryInvoke {
        themeDialog.apply { setArguments(check) }.show(fm, DialogDef.THEME)
    }

}