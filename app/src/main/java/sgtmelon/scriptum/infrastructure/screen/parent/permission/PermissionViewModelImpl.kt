package sgtmelon.scriptum.infrastructure.screen.parent.permission

import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

class PermissionViewModelImpl(private val preferencesRepo: PreferencesRepo) : ViewModel(),
    PermissionViewModel {

    override fun isCalled(key: PermissionKey): Boolean = preferencesRepo.isPermissionCalled(key)

    override fun setCalled(key: PermissionKey) = preferencesRepo.setPermissionCalled(key)

}