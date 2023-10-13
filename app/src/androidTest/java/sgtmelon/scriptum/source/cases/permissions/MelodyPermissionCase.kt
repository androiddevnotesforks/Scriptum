package sgtmelon.scriptum.source.cases.permissions

import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.model.annotation.ApiPolicy
import sgtmelon.scriptum.source.ui.model.annotation.SpecificApi

/**
 * Case for describe behaviour of [Permission.WriteExternalStorage] and select alarm melody feature.
 */
@SpecificApi(ApiPolicy.STRICT)
interface MelodyPermissionCase : PermissionCase {

    override val permission: Permission get() = Permission.WriteExternalStorage

    /** Show information dialog before permission request + permission request. */
    fun info()

    /** Show information dialog before permission request + close. */
    fun infoClose()

    /** Same as [info] but with rotation. */
    fun infoRotateWork()

    /** Same as [infoClose] but with rotation. */
    fun infoRotateClose()

    /** User grant permission - feature available to use.  */
    fun allow()

    /** User deny permission - feature (also) available to use, but with some restrictions. */
    fun deny()

}