package sgtmelon.scriptum.model.key

/**
 * Class identifying result of permission request
 *
 * [LOW_API] - Permission was granted on app install
 * [ALLOWED] - You may ask for permission
 * [FORBIDDEN] - User don't want see permission request
 * [GRANTED] - User gave you permission
 *
 * @author SerjantArbuz
 */
enum class PermissionResult {LOW_API, ALLOWED, FORBIDDEN, GRANTED}