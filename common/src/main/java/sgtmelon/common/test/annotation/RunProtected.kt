package sgtmelon.common.test.annotation

import androidx.annotation.VisibleForTesting

/**
 * Short annotation for make variables and functions visible for tests.
 */
@Deprecated("Better test class as it is, without testing complicated functional")
@VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
annotation class RunProtected