package sgtmelon.scriptum.infrastructure.bundle

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.appcompat.app.AppCompatActivity as Activity

inline fun <reified T : Activity> Context.intent(vararg pairs: Pair<String, Any?>): Intent =
    intentFor<T>(this).setExtras<T>(pairs)

inline fun <reified T : Any> intentFor(context: Context): Intent = Intent(context, T::class.java)

fun <T : Activity> Intent.setExtras(pairs: Array<out Pair<String, Any?>>): Intent = apply {
    putExtras(bundleOf(*pairs))
}