package sgtmelon.scriptum.infrastructure.model.key.permission

/**
 * Class identifying result of permission request:
 *
 * [ASK] - You may ask for permission.
 * [FORBIDDEN] - User don't want see permission request.
 * [GRANTED] - User gave you permission.
 */
enum class PermissionResult { ASK, FORBIDDEN, GRANTED }