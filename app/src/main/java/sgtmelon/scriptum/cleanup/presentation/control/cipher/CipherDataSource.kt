package sgtmelon.scriptum.cleanup.presentation.control.cipher

/**
 * Interface for [CipherDataSourceImpl].
 */
interface CipherDataSource {

    fun encrypt(text: String): String

    fun decrypt(text: String): String

}