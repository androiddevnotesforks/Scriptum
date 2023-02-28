package sgtmelon.scriptum.infrastructure.screen.preference.backup

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import javax.inject.Inject
import sgtmelon.extensions.collect
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
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState
import sgtmelon.scriptum.infrastructure.utils.extensions.isGranted
import sgtmelon.scriptum.infrastructure.utils.extensions.requestPermission
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsActivity
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of backup preferences.
 */
class BackupPreferenceFragment : ParentPreferenceFragment(),
    DotAnimation.Callback {

    // TODO move dialog creation/opening inside another class (this decrease file length)

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

    private val dotAnimation = DotAnimation(lifecycle, DotAnimType.COUNT, callback = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getBackupPrefBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun onResume() {
        super.onResume()

        /** Update data after every return to screen - user may change files or permission. */
        viewModel.updateData(permissionState.getResult(activity))
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
        viewModel.exportSummary.observe(this) { observeExportSummary(it) }
        viewModel.exportEnabled.observe(this) { binding.exportButton?.isEnabled = it }
        viewModel.importSummary.observe(this) { observeImportSummary(it) }
        viewModel.importEnabled.observe(this) { binding.importButton?.isEnabled = it }
    }

    private fun observeExportSummary(it: ExportSummaryState) {
        binding.exportButton?.summary = when (it) {
            ExportSummaryState.Permission -> getString(R.string.pref_summary_no_permission)
            ExportSummaryState.Empty -> ""
        }
    }

    private fun observeImportSummary(it: ImportSummaryState) {
        when (it) {
            is ImportSummaryState.StartSearch -> {
                dotAnimation.start(context, R.string.pref_summary_import_search)
            }
            is ImportSummaryState.Permission -> {
                dotAnimation.stop()
                updateImportSummary(getString(R.string.pref_summary_no_permission))
            }
            is ImportSummaryState.Found -> {
                dotAnimation.stop()
                updateImportSummary(getString(R.string.pref_summary_import_found, it.count))
            }
            is ImportSummaryState.NoFound -> {
                dotAnimation.stop()
                updateImportSummary(getString(R.string.pref_summary_import_empty))
            }
        }
    }

    private fun updateImportSummary(text: String) {
        binding.importButton?.summary = text
    }

    /**
     * Start export only if [result] equals [PermissionResult.GRANTED], otherwise we must
     * show dialog.
     */
    private fun onExportPermission(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ASK -> showExportPermissionDialog()
            PermissionResult.FORBIDDEN -> showExportDenyDialog()
            PermissionResult.GRANTED -> onExportPermissionGranted()
        }
    }

    private fun onExportPermissionGranted() {
        viewModel.startExport().collect(owner = this) {
            when (it) {
                is ExportState.ShowLoading -> showExportLoadingDialog()
                is ExportState.HideLoading -> loadingDialog.safeDismiss(owner = this)
                is ExportState.LoadSuccess -> {
                    val text = getString(R.string.pref_toast_export_result, it.path)
                    system.toast.show(context, text, Toast.LENGTH_LONG)
                }
                is ExportState.LoadError -> {
                    system.toast.show(context, R.string.pref_toast_export_error)
                }
            }
        }
    }

    /**
     * Start import only if [result] equals [PermissionResult.GRANTED], otherwise we must
     * show dialog.
     */
    private fun onImportPermission(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ASK -> showImportPermissionDialog()
            PermissionResult.FORBIDDEN -> showImportDenyDialog()
            PermissionResult.GRANTED -> {
                viewModel.importData.collect(owner = this) { showImportDialog(it) }
            }
        }
    }

    //region Dialogs

    override fun setupDialogs() {
        super.setupDialogs()

        exportPermissionDialog.apply {
            isCancelable = false
            onPositiveClick { requestPermission(PermissionRequest.EXPORT, permissionState) }
            onDismiss { open.clear() }
        }

        exportDenyDialog.apply {
            onPositiveClick { context?.startSettingsActivity(system.toast) }
            onDismiss { open.clear() }
        }

        importPermissionDialog.apply {
            isCancelable = false
            onPositiveClick { requestPermission(PermissionRequest.IMPORT, permissionState) }
            onDismiss { open.clear() }
        }

        importDenyDialog.apply {
            onPositiveClick { context?.startSettingsActivity(system.toast) }
            onDismiss { open.clear() }
        }

        importDialog.apply {
            onPositiveClick { onImportApply(itemArray.getOrNull(check)) }
            onDismiss { open.clear() }
        }

        loadingDialog.apply {
            isCancelable = false
            onDismiss { open.clear() }
        }
    }

    private fun showExportPermissionDialog() = open.attempt {
        exportPermissionDialog
            .safeShow(DialogFactory.Preference.Backup.EXPORT_PERMISSION, owner = this)
    }

    private fun showExportDenyDialog() = open.attempt {
        exportDenyDialog.safeShow(DialogFactory.Preference.Backup.EXPORT_DENY, owner = this)
    }

    private fun showExportLoadingDialog() = open.attempt {
        loadingDialog.safeShow(DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    private fun showImportPermissionDialog() = open.attempt {
        importPermissionDialog
            .safeShow(DialogFactory.Preference.Backup.IMPORT_PERMISSION, owner = this)
    }

    private fun showImportDenyDialog() = open.attempt {
        importDenyDialog.safeShow(DialogFactory.Preference.Backup.IMPORT_DENY, owner = this)
    }

    private fun showImportDialog(titleArray: Array<String>) = open.attempt {
        open.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.safeShow(DialogFactory.Preference.Backup.IMPORT, owner = this)
    }

    private fun onImportApply(name: String?) {
        if (name == null) return

        open.skipClear = true

        viewModel.startImport(name).collect(owner = this) {
            when (it) {
                is ImportState.ShowLoading -> showImportLoadingDialog()
                is ImportState.HideLoading -> loadingDialog.safeDismiss(owner = this)
                is ImportState.LoadSuccess -> {
                    system.toast.show(context, R.string.pref_toast_import_result)
                }
                is ImportState.LoadSkip -> {
                    val text = getString(R.string.pref_toast_import_skip, it.count)
                    system.toast.show(context, text, Toast.LENGTH_LONG)
                }
                is ImportState.LoadError -> {
                    system.toast.show(context, R.string.pref_toast_import_error)
                }
                is ImportState.Finish -> {
                    system.broadcast.sendTidyUpAlarm()
                    system.broadcast.sendNotifyNotesBind()
                    system.broadcast.sendNotifyInfoBind()
                }
            }
        }
    }

    private fun showImportLoadingDialog() = open.attempt(OpenState.Tag.DIALOG) {
        loadingDialog.safeShow(DialogFactory.Preference.Backup.LOADING, owner = this)
    }

    //endregion

    override fun onDotAnimationUpdate(text: String) = updateImportSummary(text)

}