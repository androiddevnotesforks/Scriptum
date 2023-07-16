package sgtmelon.scriptum.infrastructure.screen.parent.permission

import sgtmelon.scriptum.data.model.PermissionKey

interface PermissionViewModel {

    fun isCalled(key: PermissionKey): Boolean

    fun setCalled(key: PermissionKey)
}