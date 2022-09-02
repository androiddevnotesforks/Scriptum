package sgtmelon.scriptum.data.dataSource.system

/**
 * Interface for [CipherDataSourceImpl].
 */
interface CipherDataSource {

    fun encrypt(text: String): String

    fun decrypt(text: String): String

}