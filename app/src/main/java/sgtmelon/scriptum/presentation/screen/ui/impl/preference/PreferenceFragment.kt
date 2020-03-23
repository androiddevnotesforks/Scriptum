package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.MelodyControl
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.PermissionResult
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.model.state.PermissionState
import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel
import javax.inject.Inject

/**
 * Fragment of preference
 */
class PreferenceFragment : PreferenceFragmentCompat(), IPreferenceFragment {

    /**
     * TODO nullability.
     */
    private val activity: PreferenceActivity by lazy { getActivity() as PreferenceActivity }
    private val fm: FragmentManager by lazy { activity.supportFragmentManager }

    @Inject internal lateinit var viewModel: IPreferenceViewModel

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

    private val themePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_app_theme)) }

    private val sortPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_sort)) }
    private val colorPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_color)) }

    private val repeatPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_repeat)) }
    private val signalPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_signal)) }
    private val melodyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_melody)) }
    private val increasePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_increase)) }
    private val volumePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_volume)) }

    private val saveTimePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_time)) }

    //endregion

    private val melodyControl: IMelodyControl by lazy { MelodyControl(activity) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        ScriptumApplication.component.getPreferenceBuilder().set(fragment = this).build()
                .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        melodyControl.initLazy()
        openState.get(savedInstanceState)

        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
        melodyControl.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MELODY_REQUEST -> {
                viewModel.onClickMelody(if (grantResults.first().isGranted()) {
                    PermissionResult.GRANTED
                } else {
                    PermissionResult.FORBIDDEN
                })
            }
        }
    }


    override fun setupApp() {
        themePreference?.setOnPreferenceClickListener { viewModel.onClickTheme() }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultTheme(themeDialog.check)
            activity.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupNote() {
        sortPreference?.setOnPreferenceClickListener { viewModel.onClickSort() }

        sortDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteSort(sortDialog.check)
        }
        sortDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        colorPreference?.setOnPreferenceClickListener { viewModel.onClickNoteColor() }

        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultNoteColor(colorDialog.check)
        }
        colorDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupNotification(melodyTitleArray: Array<String>) {
        repeatPreference?.setOnPreferenceClickListener { viewModel.onClickRepeat() }

        repeatDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultRepeat(repeatDialog.check)
        }
        repeatDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        signalPreference?.setOnPreferenceClickListener { viewModel.onClickSignal() }

        signalDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSignal(signalDialog.check)
        }
        signalDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        melodyPreference?.setOnPreferenceClickListener {
            viewModel.onClickMelody(externalPermissionState.getResult())
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
            viewModel.onSelectMelody(i)
        }
        melodyDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultMelody(melodyDialog.check)
        }
        melodyDialog.dismissListener = DialogInterface.OnDismissListener {
            melodyControl.stop()
            openState.clear()
        }

        volumePreference?.setOnPreferenceClickListener { viewModel.onClickVolume() }

        volumeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultVolume(volumeDialog.progress)
        }
        volumeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupSave() {
        saveTimePreference?.setOnPreferenceClickListener { viewModel.onClickSaveTime() }

        saveTimeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTime(saveTimeDialog.check)
        }
        saveTimeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupOther() {
        findPreference<Preference>(getString(R.string.pref_key_other_rate))?.setOnPreferenceClickListener {
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

        findPreference<Preference>(getString(R.string.pref_key_other_privacy_policy))?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = BuildConfig.PRIVACY_POLICY_URL.toUri()
            })

            return@setOnPreferenceClickListener true
        }

        findPreference<Preference>(getString(R.string.pref_key_other_about))?.setOnPreferenceClickListener {
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
        themePreference?.summary = summary
    }

    override fun showThemeDialog(@Theme value: Int) = openState.tryInvoke {
        themeDialog.setArguments(value).show(fm, DialogFactory.Preference.THEME)
    }


    override fun updateSortSummary(summary: String) {
        sortPreference?.summary = summary
    }

    override fun showSortDialog(value: Int) = openState.tryInvoke {
        sortDialog.setArguments(value).show(fm, DialogFactory.Preference.SORT)
    }

    override fun updateColorSummary(summary: String) {
        colorPreference?.summary = summary
    }

    override fun showColorDialog(@Color color: Int, @Theme theme: Int) = openState.tryInvoke {
        colorDialog.setArguments(color, theme).show(fm, DialogFactory.Preference.COLOR)
    }


    override fun updateRepeatSummary(summary: String) {
        repeatPreference?.summary = summary
    }

    override fun showRepeatDialog(value: Int) = openState.tryInvoke {
        repeatDialog.setArguments(value).show(fm, DialogFactory.Preference.REPEAT)
    }

    override fun updateSignalSummary(summary: String) {
        signalPreference?.summary = summary
    }

    override fun showSignalDialog(value: BooleanArray) = openState.tryInvoke {
        signalDialog.setArguments(value).show(fm, DialogFactory.Preference.SIGNAL)
    }

    override fun showMelodyPermissionDialog() = openState.tryInvoke {
        melodyPermissionDialog.show(fm, DialogFactory.Preference.MELODY_PERMISSION)
    }

    override fun updateMelodyGroupEnabled(enabled: Boolean) {
        melodyPreference?.isEnabled = enabled
        increasePreference?.isEnabled = enabled
        volumePreference?.isEnabled = enabled
    }

    override fun updateMelodySummary(summary: String) {
        melodyPreference?.summary = summary
    }

    override fun showMelodyDialog(value: Int) = openState.tryInvoke {
        melodyDialog.setArguments(value).show(fm, DialogFactory.Preference.MELODY)
    }

    override fun playMelody(stringUri: String) {
        val uri = stringUri.toUri() ?: return

        with(melodyControl) {
            stop()
            setupPlayer(uri, isLooping = false)
            start()
        }
    }

    override fun updateVolumeSummary(summary: String) {
        volumePreference?.summary = summary
    }

    override fun showVolumeDialog(value: Int) = openState.tryInvoke {
        volumeDialog.setArguments(value).show(fm, DialogFactory.Preference.VOLUME)
    }


    override fun updateSaveTimeSummary(summary: String) {
        saveTimePreference?.summary = summary
    }

    override fun showSaveTimeDialog(value: Int) = openState.tryInvoke {
        saveTimeDialog.setArguments(value).show(fm, DialogFactory.Preference.SAVE_TIME)
    }

    companion object {
        private const val MELODY_REQUEST = 0
    }

}