package sgtmelon.test.prod

import androidx.annotation.VisibleForTesting

/**
 * Short annotation for make variables and functions visible for tests.
 */
@Deprecated("Better test class as it is, without testing complicated functional | Try to avoid this annotation")
@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
annotation class RunProtected