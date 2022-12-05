package sgtmelon.scriptum.infrastructure.converter

import android.net.Uri
import sgtmelon.scriptum.infrastructure.utils.extensions.record

class UriConverter {

    fun toUri(value: String): Uri? {
        return try {
            Uri.parse(value)
        } catch (e: Throwable) {
            e.record()
            null
        }
    }
}