package sgtmelon.scriptum.app.view.fragment

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
import sgtmelon.safedialog.library.SingleDialog
import sgtmelon.safedialog.library.color.ColorDialog
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.view.activity.DevelopActivity
import sgtmelon.scriptum.app.view.activity.PreferenceActivity
import sgtmelon.scriptum.element.InfoDialog
import sgtmelon.scriptum.element.SortDialog
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.PrefUtils

class PreferenceFragment : android.preference.PreferenceFragment() {

    // TODO https://www.youtube.com/watch?v=PS9jhuHECEQ
    // TODO перенести theme на самый верх

    private val openState = OpenState()

    private val activity: PreferenceActivity by lazy { getActivity() as PreferenceActivity }

    private val prefUtils: PrefUtils by lazy { PrefUtils(activity) }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    private val sortDialog: SortDialog by lazy {
        DialogFactory.getSortDialog(fm)
    }
    private val colorDialog: ColorDialog by lazy {
        DialogFactory.getColorDialog(activity, fm)
    }
    private val infoDialog: InfoDialog by lazy {
        DialogFactory.getInfoDialog(fm)
    }
    private val saveTimeDialog: SingleDialog by lazy {
        DialogFactory.getSaveTimeDialog(activity, fm)
    }
    private val themeDialog: SingleDialog by lazy {
        DialogFactory.getThemeDialog(activity, fm)
    }

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
            openState.value = savedInstanceState.getBoolean(IntentDef.STATE_OPEN)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupNotePref()
        setupSavePref()
        setupAppPref()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IntentDef.STATE_OPEN, openState.value)
    }

    private fun setupNotePref() {
        sortPreference.summary = prefUtils.getSortSummary(prefUtils.sort)
        sortPreference.setOnPreferenceClickListener {
            openState.tryInvoke {
                sortDialog.setArguments(prefUtils.sort)
                sortDialog.show(fm, DialogDef.SORT)
            }
            return@setOnPreferenceClickListener true
        }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            prefUtils.sort = sortDialog.keys
            sortPreference.summary = prefUtils.getSortSummary(sortDialog.keys)
        }
        sortDialog.neutralListener = DialogInterface.OnClickListener { _, _ ->
            prefUtils.sort = SortDef.def
            sortPreference.summary = prefUtils.getSortSummary(SortDef.def)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference.summary = resources.getStringArray(R.array.pref_color_text)[prefUtils.defaultColor]
        colorPreference.setOnPreferenceClickListener {
            openState.tryInvoke {
                colorDialog.setArguments(prefUtils.defaultColor)
                colorDialog.show(fm, DialogDef.COLOR)
            }
            return@setOnPreferenceClickListener true
        }

        colorDialog.title = getString(R.string.pref_color_title)
        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            prefUtils.defaultColor = colorDialog.check
            colorPreference.summary = resources.getStringArray(R.array.pref_color_text)[colorDialog.check]
        }

        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupSavePref() {
        saveTimePreference.summary = resources.getStringArray(R.array.pref_save_time_text)[prefUtils.saveTime]
        saveTimePreference.setOnPreferenceClickListener {
            openState.tryInvoke {
                saveTimeDialog.setArguments(prefUtils.saveTime)
                saveTimeDialog.show(fm, DialogDef.SAVE_TIME)
            }
            return@setOnPreferenceClickListener true
        }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            prefUtils.saveTime = saveTimeDialog.check
            saveTimePreference.summary = saveTimeDialog.rows[saveTimeDialog.check]
        }
        saveTimeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        val autoSavePreference = findPreference(getString(R.string.pref_key_auto_save)) as CheckBoxPreference
        autoSavePreference.setOnPreferenceChangeListener { _, newValue ->
            saveTimePreference.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        saveTimePreference.isEnabled = autoSavePreference.isChecked
    }

    private fun setupAppPref() {
        themePreference.summary = resources.getStringArray(R.array.pref_theme_text)[prefUtils.theme]
        themePreference.setOnPreferenceClickListener {
            openState.tryInvoke {
                themeDialog.setArguments(prefUtils.theme)
                themeDialog.show(fm, DialogDef.THEME)
            }
            return@setOnPreferenceClickListener true
        }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            prefUtils.theme = themeDialog.check
            themePreference.summary = themeDialog.rows[themeDialog.check]

            activity.isThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        val ratePreference = findPreference(getString(R.string.pref_key_rate))
        ratePreference.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            try {
                intent.data = Uri.parse(BuildConfig.MARKET_URL + activity.packageName)
            } catch (exception: ActivityNotFoundException) {
                intent.data = Uri.parse(BuildConfig.BROWSER_URL + activity.packageName)
            }

            startActivity(intent)
            return@setOnPreferenceClickListener true
        }

        val aboutPreference = findPreference(getString(R.string.pref_key_about))
        aboutPreference.setOnPreferenceClickListener {
            openState.tryInvoke { infoDialog.show(fm, DialogDef.INFO) }
            return@setOnPreferenceClickListener true
        }

        infoDialog.logoClick = View.OnClickListener {
            startActivity(Intent(activity, DevelopActivity::class.java))
        }
        infoDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

}