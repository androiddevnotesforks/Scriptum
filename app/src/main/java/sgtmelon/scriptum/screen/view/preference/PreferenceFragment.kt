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
import sgtmelon.scriptum.screen.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.callback.IPreferenceViewModel
import sgtmelon.scriptum.screen.view.DevelopActivity
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import android.preference.PreferenceFragment as OldPreferenceFragment

/**
 * Экран настроек приложения
 *
 * @author SerjantArbuz
 */
class PreferenceFragment : OldPreferenceFragment(), IPreferenceFragment {

    private val activity: PreferenceActivity by lazy { getActivity() as PreferenceActivity }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    private val iViewModel: IPreferenceViewModel by lazy {
        PreferenceViewModel(activity, callback = this)
    }

    private val openState = OpenState()

    private val themeDialog by lazy { DialogFactory.Preference.getThemeDialog(activity, fm) }

    private val repeatDialog by lazy { DialogFactory.Preference.getRepeatDialog(activity, fm) }
    private val signalDialog by lazy { DialogFactory.Preference.getSignalDialog(activity, fm) }
    private val melodyDialog by lazy { DialogFactory.Preference.getMelodyDialog(activity, fm) }
    private val volumeDialog by lazy { DialogFactory.Preference.getVolumeDialog(activity, fm) }

    private val sortDialog by lazy { DialogFactory.Preference.getSortDialog(activity, fm) }
    private val colorDialog by lazy { DialogFactory.Preference.getColorDialog(activity, fm) }
    private val saveTimeDialog by lazy { DialogFactory.Preference.getSaveTimeDialog(activity, fm) }
    private val aboutDialog by lazy { DialogFactory.Preference.getAboutDialog(fm) }

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

        iViewModel.onSetup()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    override fun setupApp() {
        themePreference.setOnPreferenceClickListener { iViewModel.onClickTheme() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultTheme(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    // TODO #RELEASE
    override fun setupNotification(melodyTitleList: Array<String>) {
        repeatPreference.setOnPreferenceClickListener { iViewModel.onClickRepeat() }

        repeatDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultRepeat(repeatDialog.check)
        }
        repeatDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        signalPreference.setOnPreferenceClickListener { iViewModel.onClickSignal() }

        signalDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultSignal(signalDialog.check)
        }
        signalDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        melodyPreference.setOnPreferenceClickListener { iViewModel.onClickMelody() }

        melodyDialog.rows = melodyTitleList
        melodyDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultMelody(melodyDialog.check)
        }
        melodyDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        volumePreference.setOnPreferenceClickListener { iViewModel.onClickVolume() }

        volumeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultVolume(volumeDialog.progress)
        }
        volumeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupNote() {
        sortPreference.setOnPreferenceClickListener { iViewModel.onClickSort() }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultNoteSort(sortDialog.check)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference.setOnPreferenceClickListener { iViewModel.onClickNoteColor() }

        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultNoteColor(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupSave() {
        saveTimePreference.setOnPreferenceClickListener { iViewModel.onClickSaveTime() }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultSaveTime(saveTimeDialog.check)
        }
        saveTimeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        val autoSavePreference = findPreference(getString(R.string.key_save_auto)) as? CheckBoxPreference
        autoSavePreference?.setOnPreferenceChangeListener { _, newValue ->
            saveTimePreference.isEnabled = newValue as Boolean
            return@setOnPreferenceChangeListener true
        }

        saveTimePreference.isEnabled = autoSavePreference?.isChecked == true
    }

    override fun setupOther() {
        findPreference(getString(R.string.key_other_rate)).setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            try {
                intent.data = Uri.parse(BuildConfig.MARKET_URL.plus(activity.packageName))
                startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                intent.data = Uri.parse(BuildConfig.BROWSER_URL.plus(activity.packageName))
                startActivity(intent)
            }

            return@setOnPreferenceClickListener true
        }

        findPreference(getString(R.string.key_other_about)).setOnPreferenceClickListener {
            openState.tryInvoke { aboutDialog.show(fm, DialogFactory.Preference.ABOUT) }
            return@setOnPreferenceClickListener true
        }

        aboutDialog.apply {
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

    override fun updateMelodySummary(summary: String) {
        melodyPreference.summary = summary
    }

    override fun showMelodyDialog(value: Int) = openState.tryInvoke {
        melodyDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.MELODY)
    }

    override fun updateVolumeSummary(summary: String) {
        volumePreference.summary = summary
    }

    override fun showVolumeDialog(value: Int) = openState.tryInvoke {
        volumeDialog.apply { setArguments(value) }.show(fm, DialogFactory.Preference.VOLUME)
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