package sgtmelon.scriptum.infrastructure.model.key.permission

/**
 * Class identifying result of permission request:
 *
 * [OLD_API] --- Don't need to request a permission, because current Android version doesn't
 *               require permission for an action.
 * [ASK] ------- You may ask for permission.
 * [FORBIDDEN] - User don't want see permission request.
 * [GRANTED] --- User gave you permission.
 * [NEW_API] --- Don't need to request a permission, but need to do a workaround due to Android API
 *               changes.
 */
enum class PermissionResult { OLD_API, ASK, FORBIDDEN, GRANTED, NEW_API }