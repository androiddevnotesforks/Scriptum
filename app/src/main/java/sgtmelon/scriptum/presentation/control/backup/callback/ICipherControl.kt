package sgtmelon.scriptum.presentation.control.backup.callback

import sgtmelon.scriptum.presentation.control.backup.CipherControl

/**
 * Interface for [CipherControl].
 */
interface ICipherControl {

    fun encrypt(text: String): String

    fun decrypt(text: String): String

}