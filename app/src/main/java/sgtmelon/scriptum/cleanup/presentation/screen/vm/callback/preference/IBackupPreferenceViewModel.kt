package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult

interface IBackupPreferenceViewModel : IParentViewModel {

    fun onPause()

    fun onClickExport()

    fun onClickExport(result: PermissionResult)

    fun onClickImport()

    fun onClickImport(result: PermissionResult)

    fun onResultImport(name: String)

}