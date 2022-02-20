package sgtmelon.common.test.annotation

import androidx.annotation.VisibleForTesting

/**
 * Short annotation for make variables and functions visible for tests.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
annotation class RunPrivate