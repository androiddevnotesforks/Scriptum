package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeDismiss
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.domain.model.state.PermissionState
import sgtmelon.scriptum.cleanup.extension.getSettingsIntent
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.isGranted
import sgtmelon.scriptum.cleanup.extension.startActivitySafe
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IBackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.textDotAnim.DotAnimControl
import sgtmelon.textDotAnim.DotAnimType

/**
 * Fragment of backup preferences.
 */
class BackupPreferenceFragment : ParentPreferenceFragment(),
    IBackupPreferenceFragment,
    DotAnimControl.Callback {

    @Inject internal lateinit var viewModel: IBackupPreferenceViewModel

    private val openState = OpenState()
    private val storagePermissionState by lazy {
        PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity)
    }

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference.Backup(context, fm) }

    private val exportPermissionDialog by lazy { dialogFactory.getExportPermissionDialog() }
    private val exportDenyDialog by lazy { dialogFactory.getExportDenyDialog() }
    private val importPermissionDialog by lazy { dialogFactory.getImportPermissionDialog() }
    private val importDenyDialog by lazy { dialogFactory.getImportDenyDialog() }
    private val importDialog by lazy { dialogFactory.getImportDialog() }
    private val loadingDialog by lazy { dialogFactory.getLoadingDialog() }

    //endregion

    private val exportPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_export)) }
    private val importPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_import)) }

    private val broadcast by lazy { BroadcastDelegator(context) }
    private val dotAnimControl = DotAnimControl(DotAnimType.COUNT, callback = this)

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_backup, rootKey)

        ScriptumApplication.component.getBackupPrefBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        broadcast.initLazy()

        openState.get(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onPause() {
        super.onPause()

        viewModel.onPause()

        /**
         * After call [IBackupPreferenceViewModel.onPause] this dialog will not have any items.
         */
        importDialog.safeDismiss(owner = this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.firstOrNull()?.isGranted() != true) return

        when (requestCode) {
            PermissionRequest.EXPORT -> viewModel.onClickExport(PermissionResult.GRANTED)
            PermissionRequest.IMPORT -> viewModel.onClickImport(PermissionResult.GRANTED)
        }
    }

    //endregion

    //region Toast functions

    override fun showToast(@StringRes stringId: Int) = toastControl.show(stringId)

    override fun showExportPathToast(path: String) {
        val text = getString(R.string.pref_toast_export_result, path)

        toastControl.show(text, Toast.LENGTH_LONG)
    }

    override fun showImportSkipToast(count: Int) {
        val text = getString(R.string.pref_toast_import_result_skip, count)

        toastControl.show(text, Toast.LENGTH_LONG)
    }

    //endregion

    override fun setup() {
        exportPreference?.setOnPreferenceClickListener {
            viewModel.onClickExport()
            return@setOnPreferenceClickListener true
        }

        importPreference?.setOnPreferenceClickListener {
            viewModel.onClickImport()
            return@setOnPreferenceClickListener true
        }

        exportPermissionDialog.apply {
            isCancelable = false

            onPositiveClick {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@onPositiveClick

                requestPermissions(
                    arrayOf(storagePermissionState.permission), PermissionRequest.EXPORT
                )
            }
            onDismiss { openState.clear() }
        }

        exportDenyDialog.apply {
            onPositiveClick {
                val context = context
                context?.startActivitySafe(context.getSettingsIntent(), toastControl)
            }
            onDismiss { openState.clear() }
        }

        importPermissionDialog.apply {
            isCancelable = false

            onPositiveClick {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@onPositiveClick

                requestPermissions(
                    arrayOf(storagePermissionState.permission), PermissionRequest.IMPORT
                )
            }
            onDismiss { openState.clear() }
        }

        importDenyDialog.apply {
            onPositiveClick {
                val context = context
                context?.startActivitySafe(context.getSettingsIntent(), toastControl)
            }
            onDismiss { openState.clear() }
        }

        importDialog.apply {
            onPositiveClick {
                val name = itemArray.getOrNull(check) ?: return@onPositiveClick
                openState.skipClear = true
                viewModel.onResultImport(name)
            }
            onDismiss { openState.clear() }
        }

        loadingDialog.apply {
            isCancelable = false
            onDismiss { openState.clear() }
        }
    }

    override fun getStoragePermissionResult(): PermissionResult? {
        return storagePermissionState.getResult()
    }

    //region Export functions

    override fun updateExportEnabled(isEnabled: Boolean) {
        exportPreference?.isEnabled = isEnabled
    }

    override fun updateExportSummary(summaryId: Int) {
        exportPreference?.summary = getString(summaryId)
    }

    override fun resetExportSummary() {
        exportPreference?.summary = ""
    }

    override fun showExportPermissionDialog() {
        exportPermissionDialog.safeShow(fm, DialogFactory.Preference.Backup.EXPORT_PERMISSION, owner = this)
    }

    override fun showExportDenyDialog() {
        exportDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.EXPORT_DENY, owner = this)
    }

    override fun showExportLoadingDialog() = openState.tryInvoke {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    override fun hideExportLoadingDialog() = loadingDialog.safeDismiss(owner = this)

    //endregion

    //region Import functions

    override fun updateImportEnabled(isEnabled: Boolean) {
        importPreference?.isEnabled = isEnabled
    }

    override fun startImportSummarySearch() {
        val context = context ?: return

        dotAnimControl.start(context, R.string.pref_summary_backup_import_search)
    }

    override fun stopImportSummarySearch() = dotAnimControl.stop()

    override fun onDotAnimUpdate(text: CharSequence) {
        importPreference?.summary = text
    }

    override fun updateImportSummary(@StringRes summaryId: Int) {
        importPreference?.summary = getString(summaryId)
    }

    override fun updateImportSummaryFound(count: Int) {
        importPreference?.summary = getString(R.string.pref_summary_backup_import_found, count)
    }

    override fun showImportPermissionDialog() = openState.tryInvoke {
        importPermissionDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT_PERMISSION, owner = this)
    }

    override fun showImportDenyDialog() {
        importDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT_DENY, owner = this)
    }

    override fun showImportDialog(titleArray: Array<String>) = openState.tryInvoke {
        openState.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT, owner = this)
    }

    override fun showImportLoadingDialog() = openState.tryInvoke(OpenState.Tag.DIALOG) {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    override fun hideImportLoadingDialog() = loadingDialog.safeDismiss(owner = this)

    //endregion

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = broadcast.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcast.sendNotifyInfoBind(count)

    //endregion

}