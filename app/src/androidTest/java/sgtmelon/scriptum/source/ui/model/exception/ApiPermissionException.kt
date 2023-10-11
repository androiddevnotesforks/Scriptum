package sgtmelon.scriptum.source.ui.model.exception

import sgtmelon.scriptum.infrastructure.model.key.permission.Permission

/**
 * Exception for work with permission dialogs and detect wrong emulator api run.
 */
class ApiPermissionException(p: Permission) : IllegalStateException(
    "${p.value} permission must be in range: ${p.applyVersion} <= API < ${p.expireVersion}\n" +
    "Recommend you select emulator within the API range."
)