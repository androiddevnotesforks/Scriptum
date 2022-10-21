package sgtmelon.scriptum.infrastructure.model.key

/**
 * Class identifying result of permission request:
 *
 * [LOW_API] - Permission was granted on app install.
 * [ASK] - You may ask for permission.
 * [FORBIDDEN] - User don't want see permission request.
 * [GRANTED] - User gave you permission.
 */
enum class PermissionResult { LOW_API, ASK, FORBIDDEN, GRANTED }