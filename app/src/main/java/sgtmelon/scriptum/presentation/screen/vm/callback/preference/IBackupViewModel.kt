package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.BackupViewModel

/**
 * Interface for communication [IBackupFragment] with [BackupViewModel].
 */
interface IBackupViewModel : IParentViewModel {

    fun onPause()

    fun onClickExport(result: PermissionResult?)

    fun onClickImport(result: PermissionResult?)

    fun onResultImport(name: String)

}