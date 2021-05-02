package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupViewModel

/**
 * Interface for communication [IBackupViewModel] with [BackupFragment].
 */
interface IBackupFragment : EternalReceiver.Bridge.Bind {

    //region Toast functions

    fun showToast(@StringRes stringId: Int)

    fun showExportPathToast(path: String)

    fun showImportSkipToast(count: Int)

    //endregion

    fun setup()

    //region Dialog functions

    fun updateExportEnabled(isEnabled: Boolean)

    fun showExportPermissionDialog()

    fun showExportDenyDialog()

    fun showExportLoadingDialog()

    fun hideExportLoadingDialog()

    fun updateImportEnabled(isEnabled: Boolean)

    fun showImportPermissionDialog()

    fun showImportDialog(titleArray: Array<String>)

    fun showImportLoadingDialog()

    fun hideImportLoadingDialog()

    //endregion

}