package sgtmelon.scriptum.repository.develop

/**
 * Interface for communication with [DevelopRepo]
 */
interface IDevelopRepo {

    suspend fun getNoteTablePrint() : String

    suspend fun getRollTablePrint() : String

    suspend fun getRankTablePrint() : String

    suspend fun getPreferencePrint() : String

}