package sgtmelon.scriptum.source.cases.permissions

import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.model.annotation.ApiPolicy
import sgtmelon.scriptum.source.ui.model.annotation.SpecificApi

/**
 * Case for describe behaviour of [Permission.PostNotifications] and note bind feature.
 */
@SpecificApi(ApiPolicy.STRICT)
interface BindNotePermissionCase : PermissionCase {

    override val permission: Permission get() = Permission.PostNotifications

    /** User grant permission - feature available to use. */
    fun allow()

    /** User deny permission, show information dialog + open settings. */
    fun denyInfo()

    /** User deny permission, show information dialog + close. */
    fun denyInfoClose()

    /** Same as [denyInfo] but with rotation. */
    fun denyInfoRotateWork()

    /** Same as [denyInfoClose] but with rotation. */
    fun denyInfoRotateClose()

}