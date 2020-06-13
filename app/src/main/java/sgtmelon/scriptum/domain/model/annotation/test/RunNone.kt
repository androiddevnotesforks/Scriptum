package sgtmelon.scriptum.domain.model.annotation.test

import androidx.annotation.VisibleForTesting

/**
 * Short annotation for make variables and functions visible for tests.
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
annotation class RunNone