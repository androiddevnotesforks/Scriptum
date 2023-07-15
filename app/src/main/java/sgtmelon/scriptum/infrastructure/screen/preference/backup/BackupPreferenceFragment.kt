package sgtmelon.scriptum.infrastructure.screen.preference.backup

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import sgtmelon.extensions.collect
import sgtmelon.extensions.emptyString
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.dialog.LoadingDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.PermissionRequest
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState
import sgtmelon.scriptum.infrastructure.utils.extensions.isGranted
import sgtmelon.scriptum.infrastructure.utils.extensions.requestPermission
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsActivity
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimationImpl
import javax.inject.Inject

/**
 * Fragment of backup preferences.
 */
class BackupPreferenceFragment : PreferenceFragment(),
    DotAnimationImpl.Callback {

    // TODO move dialog creation/opening inside another class (this decrease file length)

    override val xmlId: Int = R.xml.preference_backup

    private val binding = BackupPreferenceBinding(fragment = this)

    @Inject lateinit var viewModel: BackupPreferenceViewModel

    private val permissionState = PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val dialogs by lazy { DialogFactory.Preference.Backup(resources) }
    private val exportPermissionDialog = DialogStorage(
        DialogFactory.Preference.Backup.EXPORT_PERMISSION, owner = this,
        create = { dialogs.getExportPermission() },
        setup = { setupExportPermissionDialog(it) }
    )
    private val exportDenyDialog = DialogStorage(
        DialogFactory.Preference.Backup.EXPORT_DENY, owner = this,
        create = { dialogs.getExportDeny() },
        setup = { setupExportDenyDialog(it) }
    )
    private val importPermissionDialog = DialogStorage(
        DialogFactory.Preference.Backup.IMPORT_PERMISSION, owner = this,
        create = { dialogs.getImportPermission() },
        setup = { setupImportPermissionDialog(it) }
    )
    private val importDenyDialog = DialogStorage(
        DialogFactory.Preference.Backup.IMPORT_DENY, owner = this,
        create = { dialogs.getImportDeny() },
        setup = { setupImportDenyDialog(it) }
    )
    private val importDialog = DialogStorage(
        DialogFactory.Preference.Backup.IMPORT, owner = this,
        create = { dialogs.getImport() },
        setup = { setupImportDialog(it) }
    )
    private val loadingDialog = DialogStorage(
        DialogFactory.Preference.Backup.LOADING, owner = this,
        create = { dialogs.getLoading() },
        setup = { setupLoadingDialog(it) }
    )

    private val dotAnimation = DotAnimationImpl(lifecycle, DotAnimType.COUNT, callback = this)

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

        when (PermissionRequest.values().getOrNull(requestCode)) {
            PermissionRequest.EXPORT -> onExportPermission(PermissionResult.GRANTED)
            PermissionRequest.IMPORT -> onImportPermission(PermissionResult.GRANTED)
            else -> return
        }
    }

    override fun setupView() {
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
            ExportSummaryState.Empty -> emptyString()
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
                is ExportState.HideLoading -> loadingDialog.dismiss()
                is ExportState.LoadSuccess -> {
                    val text = getString(R.string.pref_toast_export_result, it.path)
                    system?.toast?.show(context, text, Toast.LENGTH_LONG)
                }
                is ExportState.LoadError -> {
                    system?.toast?.show(context, R.string.pref_toast_export_error)
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

    //region Dialogs setup

    override fun setupDialogs() {
        super.setupDialogs()

        exportPermissionDialog.restore()
        exportDenyDialog.restore()
        importPermissionDialog.restore()
        importDenyDialog.restore()
        importDialog.restore()
        loadingDialog.restore()
    }

    private fun setupExportPermissionDialog(dialog: MessageDialog): Unit = with(dialog) {
        isCancelable = false
        onPositiveClick { requestPermission(PermissionRequest.EXPORT, permissionState) }
        onDismiss {
            exportPermissionDialog.release()
            open.clear()
        }
    }

    private fun setupExportDenyDialog(dialog: MessageDialog): Unit = with(dialog) {
        onPositiveClick { context?.startSettingsActivity(system?.toast) }
        onDismiss {
            exportDenyDialog.release()
            open.clear()
        }
    }

    private fun setupImportPermissionDialog(dialog: MessageDialog): Unit = with(dialog) {
        isCancelable = false
        onPositiveClick { requestPermission(PermissionRequest.IMPORT, permissionState) }
        onDismiss {
            importPermissionDialog.release()
            open.clear()
        }
    }

    private fun setupImportDenyDialog(dialog: MessageDialog): Unit = with(dialog) {
        onPositiveClick { context?.startSettingsActivity(system?.toast) }
        onDismiss {
            importDenyDialog.release()
            open.clear()
        }
    }

    private fun setupImportDialog(dialog: SingleDialog): Unit = with(dialog) {
        onPositiveClick { onImportApply(itemArray.getOrNull(check)) }
        onDismiss {
            importDialog.release()
            open.clear()
        }
    }

    private fun setupLoadingDialog(dialog: LoadingDialog): Unit = with(dialog) {
        isCancelable = false
        onDismiss {
            loadingDialog.release()
            open.clear()
        }
    }

    //endregion

    //region Dialogs show and actions

    private fun showExportPermissionDialog() = open.attempt { exportPermissionDialog.show() }

    private fun showExportDenyDialog() = open.attempt { exportDenyDialog.show() }

    private fun showExportLoadingDialog() = open.attempt { loadingDialog.show() }

    private fun showImportPermissionDialog() = open.attempt { importPermissionDialog.show() }

    private fun showImportDenyDialog() = open.attempt { importDenyDialog.show() }

    private fun showImportDialog(titleArray: Array<String>) = open.attempt {
        open.tag = OpenState.Tag.DIALOG
        importDialog.show { this.itemArray = titleArray }
    }

    private fun showImportLoadingDialog() = open.attempt(OpenState.Tag.DIALOG) {
        loadingDialog.show()
    }

    private fun onImportApply(name: String?) {
        if (name == null) return

        open.skipClear = true

        viewModel.startImport(name).collect(owner = this) {
            when (it) {
                is ImportState.ShowLoading -> showImportLoadingDialog()
                is ImportState.HideLoading -> loadingDialog.dismiss()
                is ImportState.LoadSuccess -> {
                    system?.toast?.show(context, R.string.pref_toast_import_result)
                }
                is ImportState.LoadSkip -> {
                    val text = getString(R.string.pref_toast_import_skip, it.count)
                    system?.toast?.show(context, text, Toast.LENGTH_LONG)
                }
                is ImportState.LoadError -> {
                    system?.toast?.show(context, R.string.pref_toast_import_error)
                }
                is ImportState.Finish -> {
                    system?.broadcast?.sendTidyUpAlarm()
                    system?.broadcast?.sendNotifyNotesBind()
                    system?.broadcast?.sendNotifyInfoBind()
                }
            }
        }
    }

    //endregion

    override fun onDotAnimationUpdate(text: String) = updateImportSummary(text)

}