package sgtmelon.scriptum.infrastructure.utils

import android.content.pm.PackageManager

/**
 * For short check permission is Granted or Denied.
 */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED
fun Int.notGranted() = !isGranted()