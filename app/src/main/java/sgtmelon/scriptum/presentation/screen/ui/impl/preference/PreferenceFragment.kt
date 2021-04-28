package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.Preference
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.*
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.domain.model.state.PermissionState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.presentation.control.system.MelodyControl
import sgtmelon.scriptum.presentation.control.system.callback.IMelodyControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel
import javax.inject.Inject

/**
 * Fragment of preference.
 */
class PreferenceFragment : ParentPreferenceFragment(), IPreferenceFragment {

    @Inject internal lateinit var viewModel: IPreferenceViewModel

    private val openState = OpenState()
    private val readPermissionState by lazy {
        PermissionState(Manifest.permission.READ_EXTERNAL_STORAGE, activity)
    }
    private val writePermissionState by lazy {
        PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity)
    }

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference(context, fm) }

    private val themeDialog by lazy { dialogFactory.getThemeDialog() }

    private val exportPermissionDialog by lazy { dialogFactory.getExportPermissionDialog() }
    private val exportDenyDialog by lazy { dialogFactory.getExportDenyDialog() }
    private val importPermissionDialog by lazy { dialogFactory.getImportPermissionDialog() }
    private val importDialog by lazy { dialogFactory.getImportDialog() }
    private val loadingDialog by lazy { dialogFactory.getLoadingDialog() }

    private val sortDialog by lazy { dialogFactory.getSortDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }
    private val savePeriodDialog by lazy { dialogFactory.getSavePeriodDialog() }

    private val repeatDialog by lazy { dialogFactory.getRepeatDialog() }
    private val signalDialog by lazy { dialogFactory.getSignalDialog() }
    private val melodyPermissionDialog by lazy { dialogFactory.getMelodyPermissionDialog() }
    private val melodyDialog by lazy { dialogFactory.getMelodyDialog() }
    private val volumeDialog by lazy { dialogFactory.getVolumeDialog() }

    private val aboutDialog by lazy { dialogFactory.getAboutDialog() }

    //endregion

    //region Preferences

    private val themePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_app_theme)) }

    private val exportPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_export)) }
    private val importPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_import)) }
    private val importSkipPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_skip)) }

    private val sortPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_sort)) }
    private val colorPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_color)) }

    private val repeatPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_repeat)) }
    private val signalPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_signal)) }
    private val melodyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_melody)) }
    private val increasePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_increase)) }
    private val volumePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_volume)) }

    private val savePeriodPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_note_time)) }

    private val developerPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_other_develop)) }

    //endregion

    private val broadcastControl by lazy { BroadcastControl[context] }
    private val melodyControl: IMelodyControl by lazy { MelodyControl(context) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_main, rootKey)

        ScriptumApplication.component.getPreferenceBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        broadcastControl.initLazy()
        melodyControl.initLazy()

        openState.get(savedInstanceState)

        /**
         * Need reset default summary for [melodyPreference] in [onActivityCreated], because
         * [viewModel] will update summary inside coroutine.
         *
         * It's unnecessary doing inside [onResume], because after first start summary will be set.
         */
        updateMelodySummary(summary = "")
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onPause() {
        super.onPause()

        viewModel.onPause()

        /**
         * After call [IPreferenceViewModel.onPause] this dialog's will not have any items.
         */
        melodyDialog.safeDismiss()
        importDialog.safeDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
        melodyControl.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = grantResults.firstOrNull()?.isGranted() ?: return
        val result = if (isGranted) PermissionResult.GRANTED else PermissionResult.FORBIDDEN

        when (requestCode) {
            PermissionRequest.MELODY -> viewModel.onClickMelody(result)
            PermissionRequest.EXPORT -> viewModel.onClickExport(result)
            PermissionRequest.IMPORT -> viewModel.onClickImport(result)
        }
    }


    override fun showToast(@StringRes stringId: Int) {
        context?.showToast(stringId)
    }

    override fun showExportPathToast(path: String) {
        val text = getString(R.string.pref_toast_export_result, path)

        context?.showToast(text, Toast.LENGTH_LONG)
    }

    override fun showImportSkipToast(count: Int) {
        val text = getString(R.string.pref_toast_import_result_skip, count)

        context?.showToast(text, Toast.LENGTH_LONG)
    }

    override fun setupApp() {
        themePreference?.setOnPreferenceClickListener {
            viewModel.onClickTheme()
            return@setOnPreferenceClickListener true
        }

        themeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultTheme(themeDialog.check)
            activity?.checkThemeChange()
        }
        themeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupBackup() {
        exportPreference?.setOnPreferenceClickListener {
            viewModel.onClickExport(writePermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        importPreference?.setOnPreferenceClickListener {
            viewModel.onClickImport(readPermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        exportPermissionDialog.isCancelable = false
        exportPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(writePermissionState.permission), PermissionRequest.EXPORT)
        }
        exportPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        exportDenyDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        importPermissionDialog.isCancelable = false
        importPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(readPermissionState.permission), PermissionRequest.IMPORT)
        }
        importPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        importDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            val name = with(importDialog) { itemArray.getOrNull(check) } ?: return@OnClickListener

            openState.skipClear = true
            viewModel.onResultImport(name)
        }
        importDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        loadingDialog.isCancelable = false
        loadingDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupNote() {
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
    }

    override fun setupSave() {
        savePeriodPreference?.setOnPreferenceClickListener {
            viewModel.onClickSaveTime()
            return@setOnPreferenceClickListener true
        }

        savePeriodDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSaveTime(savePeriodDialog.check)
        }
        savePeriodDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupNotification() {
        repeatPreference?.setOnPreferenceClickListener {
            viewModel.onClickRepeat()
            return@setOnPreferenceClickListener true
        }

        repeatDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultRepeat(repeatDialog.check)
        }
        repeatDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        signalPreference?.setOnPreferenceClickListener {
            viewModel.onClickSignal()
            return@setOnPreferenceClickListener true
        }

        signalDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSignal(signalDialog.check)
        }
        signalDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        melodyPreference?.setOnPreferenceClickListener {
            viewModel.onClickMelody(readPermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        melodyPermissionDialog.isCancelable = false
        melodyPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(readPermissionState.permission), PermissionRequest.MELODY)
        }
        melodyPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        melodyDialog.itemListener = DialogInterface.OnClickListener { _, i ->
            viewModel.onSelectMelody(i)
        }
        melodyDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            val title = with(melodyDialog) { itemArray.getOrNull(check) } ?: return@OnClickListener
            viewModel.onResultMelody(title)
        }
        melodyDialog.dismissListener = DialogInterface.OnDismissListener {
            melodyControl.stop()
            openState.clear()
        }

        volumePreference?.setOnPreferenceClickListener {
            viewModel.onClickVolume()
            return@setOnPreferenceClickListener true
        }

        volumeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultVolume(volumeDialog.progress)
        }
        volumeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun setupOther() {
        findPreference<Preference>(getString(R.string.pref_key_other_rate))?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val packageName = context?.packageName

            if (packageName != null) {
                try {
                    startActivity(intent.apply {
                        data = BuildConfig.MARKET_URL.plus(packageName).toUri()
                    })
                } catch (e: ActivityNotFoundException) {
                    startActivity(intent.apply {
                        data = BuildConfig.BROWSER_URL.plus(packageName).toUri()
                    })
                }
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

            if (aboutDialog.hideOpen) {
                viewModel.onUnlockDeveloper()
            }

            aboutDialog.clear()
        }
    }

    override fun setupDeveloper() {
        developerPreference?.isVisible = true
        developerPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(DevelopActivity[context])
            }

            return@setOnPreferenceClickListener true
        }
    }


    override fun updateThemeSummary(summary: String?) {
        themePreference?.summary = summary
    }

    override fun showThemeDialog(@Theme value: Int) = openState.tryInvoke {
        themeDialog.setArguments(value).show(fm, DialogFactory.Preference.THEME)
    }


    override fun updateExportEnabled(isEnabled: Boolean) {
        exportPreference?.isEnabled = isEnabled
    }

    override fun showExportPermissionDialog() {
        exportPermissionDialog.show(fm, DialogFactory.Preference.EXPORT_PERMISSION)
    }

    override fun showExportDenyDialog() {
        exportDenyDialog.show(fm, DialogFactory.Preference.EXPORT_DENY)
    }

    override fun showExportLoadingDialog() = openState.tryInvoke {
        loadingDialog.show(fm, DialogFactory.Preference.LOADING)
    }

    override fun hideExportLoadingDialog() = loadingDialog.safeDismiss()

    override fun updateImportEnabled(isEnabled: Boolean) {
        importPreference?.isEnabled = isEnabled
        importSkipPreference?.isEnabled = isEnabled
    }

    override fun showImportPermissionDialog() = openState.tryInvoke {
        importPermissionDialog.show(fm, DialogFactory.Preference.IMPORT_PERMISSION)
    }

    override fun showImportDialog(titleArray: Array<String>) = openState.tryInvoke {
        openState.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.show(fm, DialogFactory.Preference.IMPORT)
    }

    override fun showImportLoadingDialog() = openState.tryInvoke(OpenState.Tag.DIALOG) {
        loadingDialog.show(fm, DialogFactory.Preference.LOADING)
    }

    override fun hideImportLoadingDialog() = loadingDialog.safeDismiss()


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


    override fun updateRepeatSummary(summary: String?) {
        repeatPreference?.summary = summary
    }

    override fun showRepeatDialog(@Repeat value: Int) = openState.tryInvoke {
        repeatDialog.setArguments(value).show(fm, DialogFactory.Preference.REPEAT)
    }

    override fun updateSignalSummary(summary: String?) {
        signalPreference?.summary = summary
    }

    override fun showSignalDialog(valueArray: BooleanArray) = openState.tryInvoke {
        signalDialog.setArguments(valueArray).show(fm, DialogFactory.Preference.SIGNAL)
    }

    override fun showMelodyPermissionDialog() = openState.tryInvoke {
        melodyPermissionDialog.show(fm, DialogFactory.Preference.MELODY_PERMISSION)
    }

    override fun updateMelodyGroupEnabled(isEnabled: Boolean) {
        melodyPreference?.isEnabled = isEnabled
        increasePreference?.isEnabled = isEnabled
        volumePreference?.isEnabled = isEnabled
    }

    override fun updateMelodySummary(summary: String) {
        melodyPreference?.summary = summary
    }

    override fun showMelodyDialog(titleArray: Array<String>, value: Int) = openState.tryInvoke {
        melodyDialog.itemArray = titleArray
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

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcastControl.sendNotifyInfoBind(count)

    //endregion

}