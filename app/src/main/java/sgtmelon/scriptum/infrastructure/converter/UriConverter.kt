package sgtmelon.scriptum.infrastructure.converter

import android.net.Uri
import sgtmelon.scriptum.infrastructure.utils.record

class UriConverter {

    fun toUri(uri: String): Uri? {
        return try {
            Uri.parse(uri)
        } catch (e: Throwable) {
            e.record()
            null
        }
    }
}