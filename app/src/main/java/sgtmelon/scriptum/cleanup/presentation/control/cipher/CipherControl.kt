package sgtmelon.scriptum.cleanup.presentation.control.cipher

import android.util.Base64

/**
 * Class for help control text ciphering with [Base64].
 */
class CipherControl : ICipherControl {

    override fun encrypt(text: String): String {
        return String(Base64.encode(text.toByteArray(), Base64.URL_SAFE))
    }

    override fun decrypt(text: String): String {
        return String(Base64.decode(text.toByteArray(), Base64.URL_SAFE))
    }

}