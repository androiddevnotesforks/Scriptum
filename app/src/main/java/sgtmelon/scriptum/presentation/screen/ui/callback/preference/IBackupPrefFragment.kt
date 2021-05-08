package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupPrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupPrefViewModel

/**
 * Interface for communication [IBackupPrefViewModel] with [BackupPrefFragment].
 */
interface IBackupPrefFragment : SystemReceiver.Bridge.TidyUp,
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