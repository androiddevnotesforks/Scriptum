package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.DialogInterface
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
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.isGranted
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IBackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.text.dotanim.DotAnimControl
import sgtmelon.text.dotanim.DotAnimType

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

    private val broadcastControl by lazy { BroadcastControl[context] }
    private val dotAnimControl = DotAnimControl(DotAnimType.COUNT, callback = this)

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_backup, rootKey)

        ScriptumApplication.component.getBackupPrefBuilder().set(fragment = this).build()
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

    override fun onPause() {
        super.onPause()

        viewModel.onPause()

        /**
         * After call [IBackupPreferenceViewModel.onPause] this dialog will not have any items.
         */
        importDialog.safeDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = grantResults.firstOrNull()?.isGranted() ?: return
        val result = if (isGranted) PermissionResult.GRANTED else PermissionResult.FORBIDDEN

        when (requestCode) {
            PermissionRequest.EXPORT -> viewModel.onClickExport(result)
            PermissionRequest.IMPORT -> viewModel.onClickImport(result)
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

        exportPermissionDialog.isCancelable = false
        exportPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(storagePermissionState.permission), PermissionRequest.EXPORT)
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

            requestPermissions(arrayOf(storagePermissionState.permission), PermissionRequest.IMPORT)
        }
        importPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        importDenyDialog.dismissListener = DialogInterface.OnDismissListener {
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
        exportPermissionDialog.safeShow(fm, DialogFactory.Preference.Backup.EXPORT_PERMISSION)
    }

    override fun showExportDenyDialog() {
        exportDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.EXPORT_DENY)
    }

    override fun showExportLoadingDialog() = openState.tryInvoke {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING)
    }

    override fun hideExportLoadingDialog() = loadingDialog.safeDismiss()

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
        importPermissionDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT_PERMISSION)
    }

    override fun showImportDenyDialog() {
        importDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT_DENY)
    }

    override fun showImportDialog(titleArray: Array<String>) = openState.tryInvoke {
        openState.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT)
    }

    override fun showImportLoadingDialog() = openState.tryInvoke(OpenState.Tag.DIALOG) {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING)
    }

    override fun hideImportLoadingDialog() = loadingDialog.safeDismiss()

    //endregion

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = broadcastControl.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = broadcastControl.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcastControl.sendNotifyInfoBind(count)

    //endregion

}