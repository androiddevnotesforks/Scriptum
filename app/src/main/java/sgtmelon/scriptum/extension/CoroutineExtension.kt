package sgtmelon.scriptum.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate

/**
 * Variable only for coroutine tests.
 */
@RunPrivate var isTesting = false

/**
 * Use this function for hard calculation operations.
 */
suspend fun <T> runCalculation(func: suspend () -> T): T {
    if (isTesting) return func()

    return withContext(Dispatchers.IO) { func() }
}