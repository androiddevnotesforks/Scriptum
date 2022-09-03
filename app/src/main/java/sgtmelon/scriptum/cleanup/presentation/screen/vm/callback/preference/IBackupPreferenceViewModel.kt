package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

interface IBackupPreferenceViewModel : IParentViewModel {

    fun onPause()

    fun onClickExport()

    fun onClickExport(result: PermissionResult)

    fun onClickImport()

    fun onClickImport(result: PermissionResult)

    fun onResultImport(name: String)

}