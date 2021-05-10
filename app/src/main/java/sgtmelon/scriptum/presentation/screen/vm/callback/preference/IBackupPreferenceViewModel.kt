package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.BackupPreferenceViewModel

/**
 * Interface for communication [IBackupPreferenceFragment] with [BackupPreferenceViewModel].
 */
interface IBackupPreferenceViewModel : IParentViewModel {

    fun onPause()

    fun onClickExport()

    fun onClickExport(result: PermissionResult)

    fun onClickImport()

    fun onClickImport(result: PermissionResult)

    fun onResultImport(name: String)

}