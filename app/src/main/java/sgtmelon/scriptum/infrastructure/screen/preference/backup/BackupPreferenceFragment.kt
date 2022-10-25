package sgtmelon.scriptum.infrastructure.screen.preference.backup

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeDismiss
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.PermissionRequest
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.isGranted
import sgtmelon.scriptum.infrastructure.utils.requestPermission
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.startSettingsActivity
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of backup preferences.
 */
class BackupPreferenceFragment : ParentPreferenceFragment(),
    DotAnimation.Callback {

    override val xmlId: Int = R.xml.preference_backup

    private val binding = BackupPreferenceDataBinding(fragment = this)

    @Inject lateinit var viewModel: BackupPreferenceViewModel

    private val permissionState = PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val dialogs by lazy { DialogFactory.Preference.Backup(context, fm) }
    private val exportPermissionDialog by lazy { dialogs.getExportPermission() }
    private val exportDenyDialog by lazy { dialogs.getExportDeny() }
    private val importPermissionDialog by lazy { dialogs.getImportPermission() }
    private val importDenyDialog by lazy { dialogs.getImportDeny() }
    private val importDialog by lazy { dialogs.getImport() }
    private val loadingDialog by lazy { dialogs.getLoading() }

    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getBackupPrefBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /**
         * In this case we don't pass [PermissionResult.FORBIDDEN] if permission not granted.
         * Just skip it, user make a decision.
         *
         * [PermissionResult.FORBIDDEN] will be a final stage, when we display deny dialog
         * (which will open app settings after "OK" click).
         */
        if (grantResults.firstOrNull()?.isGranted() != true) return

        when (PermissionRequest.values()[requestCode]) {
            PermissionRequest.EXPORT -> onExportPermission(PermissionResult.GRANTED)
            PermissionRequest.IMPORT -> onImportPermission(PermissionResult.GRANTED)
            else -> return
        }
    }

    override fun setup() {
        binding.exportButton?.setOnClickListener {
            onExportPermission(permissionState.getResult(activity))
        }
        binding.importButton?.setOnClickListener {
            onImportPermission(permissionState.getResult(activity))
        }
    }

    override fun setupObservers() {
        TODO()
    }

    /**
     * Start export only if [result] equals [PermissionResult.LOW_API] or
     * [PermissionResult.GRANTED]. Otherwise we must show dialog.
     */
    private fun onExportPermission(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ASK -> showExportPermissionDialog()
            PermissionResult.FORBIDDEN -> showExportDenyDialog()
            PermissionResult.LOW_API, PermissionResult.GRANTED -> viewModel.startExport()
        }
    }

    /**
     * Start import only if [result] equals [PermissionResult.LOW_API] or
     * [PermissionResult.GRANTED]. Otherwise we must show dialog.
     */
    private fun onImportPermission(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ASK -> showImportPermissionDialog()
            PermissionResult.FORBIDDEN -> showImportDenyDialog()
            PermissionResult.LOW_API, PermissionResult.GRANTED -> viewModel.startImport()
        }
    }

    //region cleanup

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onPause() {
        super.onPause()

        viewModel.onPause()

        /**
         * After call [BackupPreferenceViewModel.onPause] this dialog will not have any items.
         */
        importDialog.safeDismiss()
    }

    //region Toast functions

    override fun showToast(@StringRes stringId: Int) = delegators.toast.show(context, stringId)

    override fun showExportPathToast(path: String) {
        val text = getString(R.string.pref_toast_export_result, path)

        delegators.toast.show(context, text, Toast.LENGTH_LONG)
    }

    override fun showImportSkipToast(count: Int) {
        val text = getString(R.string.pref_toast_import_result_skip, count)

        delegators.toast.show(context, text, Toast.LENGTH_LONG)
    }

    //endregion

    override fun getStoragePermissionResult(): PermissionResult? {
        return permissionState.getResult(activity)
    }

    //region Export functions

    override fun updateExportEnabled(isEnabled: Boolean) {
        binding.exportButton?.isEnabled = isEnabled
    }

    override fun updateExportSummary(summaryId: Int) {
        binding.exportButton?.summary = getString(summaryId)
    }

    override fun resetExportSummary() {
        binding.exportButton?.summary = ""
    }

    override fun hideExportLoadingDialog() = loadingDialog.safeDismiss(owner = this)

    //endregion

    //region Import functions

    override fun updateImportEnabled(isEnabled: Boolean) {
        binding.importButton?.isEnabled = isEnabled
    }

    override fun startImportSummarySearch() {
        dotAnimation.start(context, R.string.pref_summary_backup_import_search)
    }

    override fun stopImportSummarySearch() = dotAnimation.stop()

    override fun updateImportSummary(@StringRes summaryId: Int) {
        binding.importButton?.summary = getString(summaryId)
    }

    override fun updateImportSummaryFound(count: Int) {
        binding.importButton?.summary = getString(R.string.pref_summary_backup_import_found, count)
    }


    //endregion

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = delegators.broadcast.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) =
        delegators.broadcast.sendNotifyInfoBind(count)

    //endregion

    //endregion

    //region Dialogs

    private fun setupDialogs() {
        exportPermissionDialog.apply {
            isCancelable = false
            onPositiveClick { requestPermission(PermissionRequest.EXPORT, permissionState) }
            onDismiss { open.clear() }
        }

        exportDenyDialog.apply {
            onPositiveClick { context?.startSettingsActivity(delegators.toast) }
            onDismiss { open.clear() }
        }

        importPermissionDialog.apply {
            isCancelable = false
            onPositiveClick { requestPermission(PermissionRequest.IMPORT, permissionState) }
            onDismiss { open.clear() }
        }

        importDenyDialog.apply {
            onPositiveClick { context?.startSettingsActivity(delegators.toast) }
            onDismiss { open.clear() }
        }

        importDialog.apply {
            onPositiveClick {
                val name = itemArray.getOrNull(check) ?: return@onPositiveClick
                open.skipClear = true
                viewModel.onResultImport(name)
            }
            onDismiss { open.clear() }
        }

        loadingDialog.apply {
            isCancelable = false
            onDismiss { open.clear() }
        }
    }

    private fun showExportPermissionDialog() = open.attempt {
        exportPermissionDialog
            .safeShow(fm, DialogFactory.Preference.Backup.EXPORT_PERMISSION, owner = this)
    }

    private fun showExportDenyDialog() {
        exportDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.EXPORT_DENY, owner = this)
    }

    private fun showExportLoadingDialog() = open.attempt {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    private fun showImportPermissionDialog() = open.attempt {
        importPermissionDialog
            .safeShow(fm, DialogFactory.Preference.Backup.IMPORT_PERMISSION, owner = this)
    }

    private fun showImportDenyDialog() {
        importDenyDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT_DENY, owner = this)
    }

    private fun showImportDialog(titleArray: Array<String>) = open.attempt {
        open.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.safeShow(fm, DialogFactory.Preference.Backup.IMPORT, owner = this)
    }

    private fun showImportLoadingDialog() = open.attempt(OpenState.Tag.DIALOG) {
        loadingDialog.safeShow(fm, DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    private fun hideImportLoadingDialog() = loadingDialog.safeDismiss(owner = this)

    //endregion

    override fun onDotAnimationUpdate(text: String) {
        binding.importButton?.summary = text
    }

}