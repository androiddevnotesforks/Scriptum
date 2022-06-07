package sgtmelon.common.utils

import kotlinx.coroutines.*
import sgtmelon.common.test.annotation.RunPrivate

/**
 * Variable only for coroutine tests.
 */
@RunPrivate var isTesting = false

/**
 * Use this function for hard calculation operations.
 */
suspend inline fun <T> runBack(crossinline func: suspend () -> T): T {
    if (isTesting) return func()

    return withContext(Dispatchers.IO) { func() }
}

suspend inline fun <T> runMain(crossinline func: () -> T): T {
    return withContext(Dispatchers.Main) { func() }
}

inline fun CoroutineScope.launchBack(crossinline func: suspend () -> Unit): Job {
    return launch { runBack(func) }
}