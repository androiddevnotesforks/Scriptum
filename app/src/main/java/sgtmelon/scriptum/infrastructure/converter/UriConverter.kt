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

    /**
     * Allow not to mistake with Uri converting to string and back. E.g. if you'll try to pass
     * [Uri.getPath] to [toUri] - it will fail at the end (during file request).
     */
    fun toString(value: Uri): String = value.toString()
}