package sgtmelon.scriptum.data.dataSource.system

interface CipherDataSource {

    fun encrypt(text: String): String

    /** Return null in case if [text] was incorrect for decrypting. */
    fun decrypt(text: String): String?
}