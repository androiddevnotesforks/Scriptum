package sgtmelon.scriptum.data.dataSource.system

interface CipherDataSource {

    fun encrypt(text: String): String

    fun decrypt(text: String): String
}