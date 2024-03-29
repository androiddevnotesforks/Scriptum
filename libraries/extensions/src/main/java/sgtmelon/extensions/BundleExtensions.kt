package sgtmelon.extensions

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> Context.intent(vararg pairs: Pair<String, Any?>): Intent =
    intentFor<T>(this).setExtras<T>(pairs)

inline fun <reified T : Any> intentFor(context: Context): Intent = Intent(context, T::class.java)

fun <T : Any> Intent.setExtras(pairs: Array<out Pair<String, Any?>>): Intent = apply {
    putExtras(bundleOf(*pairs))
}

inline fun <reified T> T.encode(): String? {
    return try {
        Json.encodeToString(value = this)
    } catch (e: Throwable) {
        null
    }
}

inline fun <reified T> String.decode(): T? {
    return try {
        Json.decodeFromString(string = this)
    } catch (e: Throwable) {
        null
    }
}