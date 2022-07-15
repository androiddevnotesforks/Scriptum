package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel

/**
 * Interface for communication [IBackupPreferenceViewModel] with [BackupPreferenceFragment].
 */
interface IBackupPreferenceFragment : SystemReceiver.Bridge.TidyUp,
    SystemReceiver.Bridge.Bind {

    //region Toast functions

    fun showToast(@StringRes stringId: Int)

    fun showExportPathToast(path: String)

    fun showImportSkipToast(count: Int)

    //endregion

    fun setup()

    fun getStoragePermissionResult(): PermissionResult?

    //region Export functions

    fun updateExportEnabled(isEnabled: Boolean)

    fun updateExportSummary(@StringRes summaryId: Int)

    fun resetExportSummary()

    fun showExportPermissionDialog()

    fun showExportDenyDialog()

    fun showExportLoadingDialog()

    fun hideExportLoadingDialog()

    //endregion

    //region Import functions

    fun updateImportEnabled(isEnabled: Boolean)

    fun startImportSummarySearch()

    fun stopImportSummarySearch()

    fun updateImportSummary(@StringRes summaryId: Int)

    fun updateImportSummaryFound(count: Int)

    fun showImportPermissionDialog()

    fun showImportDenyDialog()

    fun showImportDialog(titleArray: Array<String>)

    fun showImportLoadingDialog()

    fun hideImportLoadingDialog()

    //endregion

}