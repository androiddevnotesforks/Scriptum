package sgtmelon.scriptum.screen.ui.preference

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.MelodyControl
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.PermissionResult
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.model.state.PermissionState
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel
import android.preference.PreferenceFragment as OldPreferenceFragment

/**
 * Экран настроек приложения
 */
class PreferenceFragment : OldPreferenceFragment(), IPreferenceFragment {

    private val activity: PreferenceActivity by lazy { getActivity() as PreferenceActivity }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    private val iViewModel: IPreferenceViewModel by lazy { PreferenceViewModel(activity, callback = this) }

    private val openState = OpenState()
    private val externalPermissionState by lazy {
        PermissionState(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference(activity, fm) }

    private val themeDialog by lazy { dialogFactory.getThemeDialog() }

    private val sortDialog by lazy { dialogFactory.getSortDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }

    private val repeatDialog by lazy { dialogFactory.getRepeatDialog() }
    private val signalDialog by lazy { dialogFactory.getSignalDialog() }
    private val melodyPermissionDialog by lazy { dialogFactory.getMelodyPermissionDialog() }
    private val melodyDialog by lazy { dialogFactory.getMelodyDialog() }
    private val volumeDialog by lazy { dialogFactory.getVolumeDialog() }

    private val saveTimeDialog by lazy { dialogFactory.getSaveTimeDialog() }
    private val aboutDialog by lazy { dialogFactory.getAboutDialog() }

    //endregion

    //region Preferences

    private val themePreference: Preference by lazy { findPreference(getString(R.string.key_app_theme)) }

    private val sortPreference: Preference by lazy { findPreference(getString(R.string.key_note_sort)) }
    private val colorPreference: Preference by lazy { findPreference(getString(R.string.key_note_color)) }

    private val repeatPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_repeat)) }
    private val signalPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_signal)) }
    private val melodyPreference: Preference by lazy { findPreference(getString(R.string.key_alarm_melody)) }
    private val increasePreference: Preference by lazy { findPreference(getString(R.string.key_alarm_increase)) }
    private val volumePreference: Preference by lazy { findPreference(getString(R.string.key_alarm_volume)) }

    private val saveTimePreference: Preference by lazy { findPreference(getString(R.string.key_save_time)) }

    //endregion

    private val iMelodyControl: IMelodyControl by lazy { MelodyControl(activity) }

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

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        iMelodyControl.release()
    }

    // TODO #RELEASE2 check permission update
    override fun onResume() = super.onResume()

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MELODY_REQUEST -> {
                iViewModel.onClickMelody(if (grantResults.first().isGranted()) {
                    PermissionResult.GRANTED
                } else {
                    PermissionResult.FORBIDDEN
                })
            }
        }
    }


    override fun setupApp() {
        themePreference.setOnPreferenceClickListener { iViewModel.onClickTheme() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultTheme(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
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

    override fun setupNotification(melodyTitleArray: Array<String>) {
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

        melodyPreference.setOnPreferenceClickListener {
            iViewModel.onClickMelody(externalPermissionState.getResult())
        }

        melodyPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(externalPermissionState.permission), MELODY_REQUEST)
        }
        melodyPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        melodyDialog.itemArray = melodyTitleArray
        melodyDialog.itemListener = DialogInterface.OnClickListener { _, i ->
            iViewModel.onSelectMelody(i)
        }
        melodyDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultMelody(melodyDialog.check)
        }
        melodyDialog.dismissListener = DialogInterface.OnDismissListener {
            iMelodyControl.stop()
            openState.clear()
        }

        volumePreference.setOnPreferenceClickListener { iViewModel.onClickVolume() }

        volumeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            iViewModel.onResultVolume(volumeDialog.progress)
        }
        volumeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
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
                startActivity(intent.apply {
                    data = BuildConfig.MARKET_URL.plus(activity.packageName).toUri()
                })
            } catch (exception: ActivityNotFoundException) {
                startActivity(intent.apply {
                    data = BuildConfig.BROWSER_URL.plus(activity.packageName).toUri()
                })
            }

            return@setOnPreferenceClickListener true
        }

        findPreference(getString(R.string.key_other_about)).setOnPreferenceClickListener {
            openState.tryInvoke { aboutDialog.show(fm, DialogFactory.Preference.ABOUT) }
            return@setOnPreferenceClickListener true
        }

        aboutDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()

            if (aboutDialog.hideOpen) startActivity(Intent(activity, DevelopActivity::class.java))

            aboutDialog.clear()
        }
    }


    override fun updateThemeSummary(summary: String) {
        themePreference.summary = summary
    }

    override fun showThemeDialog(@Theme value: Int) = openState.tryInvoke {
        themeDialog.setArguments(value).show(fm, DialogFactory.Preference.THEME)
    }


    override fun updateSortSummary(summary: String) {
        sortPreference.summary = summary
    }

    override fun showSortDialog(value: Int) = openState.tryInvoke {
        sortDialog.setArguments(value).show(fm, DialogFactory.Preference.SORT)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference.summary = summary
    }

    override fun showColorDialog(@Color value: Int) = openState.tryInvoke {
        colorDialog.setArguments(value).show(fm, DialogFactory.Preference.COLOR)
    }


    override fun updateRepeatSummary(summary: String) {
        repeatPreference.summary = summary
    }

    override fun showRepeatDialog(value: Int) = openState.tryInvoke {
        repeatDialog.setArguments(value).show(fm, DialogFactory.Preference.REPEAT)
    }

    override fun updateSignalSummary(summary: String) {
        signalPreference.summary = summary
    }

    override fun showSignalDialog(value: BooleanArray) = openState.tryInvoke {
        signalDialog.setArguments(value).show(fm, DialogFactory.Preference.SIGNAL)
    }

    override fun showMelodyPermissionDialog() = openState.tryInvoke {
        melodyPermissionDialog.show(fm, DialogFactory.Preference.MELODY_PERMISSION)
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
        melodyDialog.setArguments(value).show(fm, DialogFactory.Preference.MELODY)
    }

    override fun playMelody(uri: Uri) = with(iMelodyControl) {
        stop()
        setupPlayer(uri, isLooping = false)
        start()
    }

    override fun updateVolumeSummary(summary: String) {
        volumePreference.summary = summary
    }

    override fun showVolumeDialog(value: Int) = openState.tryInvoke {
        volumeDialog.setArguments(value).show(fm, DialogFactory.Preference.VOLUME)
    }


    override fun updateSaveTimeSummary(summary: String) {
        saveTimePreference.summary = summary
    }

    override fun showSaveTimeDialog(value: Int) = openState.tryInvoke {
        saveTimeDialog.setArguments(value).show(fm, DialogFactory.Preference.SAVE_TIME)
    }

    companion object {
        private const val MELODY_REQUEST = 0
    }

}