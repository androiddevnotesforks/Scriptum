package sgtmelon.scriptum.cleanup.presentation.control.cipher

/**
 * Interface for [CipherControl].
 */
interface ICipherControl {

    fun encrypt(text: String): String

    fun decrypt(text: String): String

}