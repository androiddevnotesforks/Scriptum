package sgtmelon.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Variable only for coroutine tests. */
@Deprecated("Try do something without it")
var isCoTesting = false

/** Use this function for hard calculation operations. */
suspend inline fun <T> runBack(crossinline func: suspend () -> T): T {
    if (isCoTesting) return func()

    return withContext(Dispatchers.IO) { func() }
}

suspend inline fun <T> runMain(crossinline func: () -> T): T {
    return withContext(Dispatchers.Main) { func() }
}

inline fun CoroutineScope.launchMain(crossinline func: () -> Unit): Job {
    return launch { runMain(func) }
}

inline fun CoroutineScope.launchBack(crossinline func: suspend () -> Unit): Job {
    return launch { runBack(func) }
}

inline fun <T> flowOnBack(crossinline func: suspend FlowCollector<T>.() -> Unit): Flow<T> {
    return flow { func() }.flowOn(Dispatchers.IO)
}

/** Short collect realization. */
inline fun <T> Flow<T>.collect(owner: LifecycleOwner, crossinline onCollect: (T) -> Unit) {
    owner.lifecycleScope.launch { collect { onCollect(it) } }
}

inline fun <T> MutableLiveData<T>.postValueWithChange(onChange: (T) -> Unit) {
    value?.let {
        it.apply(onChange)
        postValue(it)
    }
}