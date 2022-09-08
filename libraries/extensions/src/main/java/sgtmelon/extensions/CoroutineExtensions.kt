package sgtmelon.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Variable only for coroutine tests.
 */
@Deprecated("Try do something without it")
var isCoTesting = false

/**
 * Use this function for hard calculation operations.
 */
suspend inline fun <T> runBack(crossinline func: suspend () -> T): T {
    if (isCoTesting) return func()

    return withContext(Dispatchers.IO) { func() }
}

suspend inline fun <T> runMain(crossinline func: () -> T): T {
    return withContext(Dispatchers.Main) { func() }
}

inline fun CoroutineScope.launchBack(crossinline func: suspend () -> Unit): Job {
    return launch { runBack(func) }
}