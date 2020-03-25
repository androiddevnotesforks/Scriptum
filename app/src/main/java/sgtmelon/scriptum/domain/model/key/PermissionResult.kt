package sgtmelon.scriptum.domain.model.key

/**
 * Class identifying result of permission request
 *
 * [LOW_API] - Permission was granted on app install
 * [ALLOWED] - You may ask for permission
 * [FORBIDDEN] - User don't want see permission request
 * [GRANTED] - User gave you permission
 */
enum class PermissionResult {LOW_API, ALLOWED, FORBIDDEN, GRANTED}