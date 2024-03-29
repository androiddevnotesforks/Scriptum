package sgtmelon.scriptum.infrastructure.screen.preference.backup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.collect
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.dialogs.LoadingDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.item.BackupImportItem
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState
import sgtmelon.scriptum.infrastructure.utils.extensions.getPickFileIntent
import sgtmelon.scriptum.infrastructure.utils.extensions.launch
import sgtmelon.scriptum.infrastructure.utils.extensions.registerFileRequest
import sgtmelon.scriptum.infrastructure.utils.extensions.registerPermissionRequest
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsActivity
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimationImpl
import sgtmelon.textDotAnim.DotText
import javax.inject.Inject

/**
 * Fragment of backup preferences.
 */
class BackupPreferenceFragment : PreferenceFragment<BackupPreferenceBinding>(),
    DotAnimationImpl.Callback {

    // TODO move dialog creation/opening inside another class (this decrease file length)

    // TODO import dialog / manual button

    override val xmlId: Int = R.xml.preference_backup

    override fun createBinding(): BackupPreferenceBinding = BackupPreferenceBinding(fragment = this)

    @Inject lateinit var viewModel: BackupPreferenceViewModel
    @Inject lateinit var permissionViewModel: PermissionViewModel

    /**
     * [isFilesAutoFetch] - TRUE -> files will be fetched automatically, FALSE -> user need to
     * select files manually, with file picker.
     */
    private val isFilesAutoFetch: Boolean = BuildProvider.Version.isPre30

    private val fileImportRequest = registerFileRequest {
        val uri = UriConverter().toString(value = it ?: return@registerFileRequest)
        viewModel.startImport(BackupImportItem.Manual(uri)).collectImport()
    }

    private val writePermissionState = PermissionState(Permission.WriteExternalStorage)

    /**
     * We don't pass [PermissionResult.FORBIDDEN] (isGranted==false) if permission not granted.
     * Just skip it, user make a decision.
     *
     * [PermissionResult.FORBIDDEN] will be a final stage, when we display deny dialog
     * (which will open app settings after "OK" click).
     */
    private val exportPermissionRequest = registerPermissionRequest { isGranted ->
        if (isGranted) onExportPermission(PermissionResult.GRANTED)
    }
    private val importPermissionRequest = registerPermissionRequest { isGranted ->
        if (isGranted) onImportPermission(PermissionResult.GRANTED)
    }

    private val dialogs by lazy { DialogFactory.Preference.Backup(resources) }
    private val exportDenyDialog = DialogStorage(
        DialogFactory.Preference.Backup.EXPORT_DENY, owner = this,
        create = { dialogs.getExportDeny() },
        setup = { setupExportDenyDialog(it) }
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

    private val dotAnimation = DotAnimationImpl[lifecycle, DotAnimType.COUNT, this]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getBackupPrefBuilder()
            .set(owner = this)
            .set(isFilesAutoFetch)
            .build()
            .inject(fragment = this)
    }

    override fun onResume() {
        super.onResume()

        /** Update data after every return to screen - user may change files or permission. */
        viewModel.updateData(writePermissionState.getResult(activity, permissionViewModel))
    }

    override fun setupView() {
        binding?.exportButton?.setOnClickListener {
            onExportPermission(writePermissionState.getResult(activity, permissionViewModel))
        }
        binding?.importButton?.setOnClickListener {
            onImportPermission(writePermissionState.getResult(activity, permissionViewModel))
        }
    }

    override fun setupObservers() {
        viewModel.exportSummary.observe(this) { observeExportSummary(it) }
        viewModel.exportEnabled.observe(this) { binding?.exportButton?.isEnabled = it }
        viewModel.importSummary.observe(this) { observeImportSummary(it) }
        viewModel.importEnabled.observe(this) { binding?.importButton?.isEnabled = it }
    }

    private fun observeExportSummary(it: ExportSummaryState) {
        binding?.exportButton?.summary = when (it) {
            is ExportSummaryState.Permission -> getString(R.string.button_request_permission)
            is ExportSummaryState.Path -> getString(R.string.pref_summary_export_path, it.value)
        }
    }

    private fun observeImportSummary(it: ImportSummaryState) {
        if (it != ImportSummaryState.StartSearch) {
            dotAnimation.stop()
        }

        when (it) {
            is ImportSummaryState.StartSearch -> {
                dotAnimation.start(context, R.string.pref_summary_import_search)
            }
            is ImportSummaryState.Permission -> {
                updateImportSummary(R.string.button_request_permission)
            }
            is ImportSummaryState.Found -> {
                updateImportSummary(getString(R.string.pref_summary_import_found, it.count))
            }
            is ImportSummaryState.NoFound -> {
                updateImportSummary(R.string.pref_summary_import_empty)
            }
            is ImportSummaryState.Manual -> {
                updateImportSummary(R.string.pref_summary_import_manual)
            }
        }
    }

    private fun updateImportSummary(@StringRes id: Int) = updateImportSummary(getString(id))

    private fun updateImportSummary(text: CharSequence) {
        binding?.importButton?.summary = text
    }

    /**
     * Start export only if [result] equals [PermissionResult.GRANTED], otherwise we must
     * show dialog.
     */
    private fun onExportPermission(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.OLD_API -> return /** Not reachable for WRITE_STORAGE permission. */
            PermissionResult.ASK -> {
                exportPermissionRequest.launch(writePermissionState, permissionViewModel)
            }
            PermissionResult.FORBIDDEN -> showExportDenyDialog()
            PermissionResult.GRANTED -> onExportPermissionGranted()
            PermissionResult.NEW_API -> onExportPermissionGranted()
        }
    }

    private fun onExportPermissionGranted() {
        viewModel.startExport().collect(owner = this) {
            when (it) {
                is ExportState.ShowLoading -> showExportLoadingDialog()
                is ExportState.LoadSuccess -> {
                    system?.toast?.show(context, R.string.pref_toast_export_result)
                }
                is ExportState.LoadError -> {
                    system?.toast?.show(context, it.value)
                }
                is ExportState.Finish -> loadingDialog.dismiss()
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
            PermissionResult.OLD_API -> return /** Not reachable for WRITE_STORAGE permission. */
            PermissionResult.ASK -> {
                importPermissionRequest.launch(writePermissionState, permissionViewModel)
            }
            PermissionResult.FORBIDDEN -> showImportDenyDialog()
            PermissionResult.GRANTED -> onImportPermissionGranted()
            PermissionResult.NEW_API -> onImportPermissionGranted()
        }
    }

    private fun onImportPermissionGranted() {
        if (isFilesAutoFetch) {
            viewModel.importData.collect(owner = this) { showImportDialog(it) }
        } else {
            fileImportRequest.launch(getPickFileIntent())
        }
    }

    //region Dialogs setup

    override fun setupDialogs() {
        super.setupDialogs()

        exportDenyDialog.restore()
        importDenyDialog.restore()
        importDialog.restore()
        loadingDialog.restore()
    }

    private fun setupExportDenyDialog(dialog: MessageDialog): Unit = with(dialog) {
        onPositiveClick { context?.startSettingsActivity(system?.toast) }
        onDismiss {
            exportDenyDialog.release()
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

    private fun showExportDenyDialog() = open.attempt { exportDenyDialog.show() }

    private fun showExportLoadingDialog() = open.attempt { loadingDialog.show() }

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

        viewModel.startImport(BackupImportItem.AutoFetch(name)).collectImport()
    }

    private fun Flow<ImportState>.collectImport() = collect(owner = this@BackupPreferenceFragment) {
        when (it) {
            is ImportState.ShowLoading -> showImportLoadingDialog()
            is ImportState.LoadSuccess -> {
                system?.toast?.show(context, R.string.pref_toast_import_result)
            }
            is ImportState.LoadSkip -> {
                val text = getString(R.string.pref_toast_import_skip, it.count)
                system?.toast?.show(context, text, Toast.LENGTH_LONG)
            }
            is ImportState.LoadError -> {
                system?.toast?.show(context, it.value)
            }
            is ImportState.Finish -> {
                loadingDialog.dismiss()

                system?.broadcast?.sendTidyUpAlarm()
                system?.broadcast?.sendNotifyNotesBind()
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
    }

    //endregion

    override fun onDotAnimationUpdate(text: DotText) = updateImportSummary(text.value)

}