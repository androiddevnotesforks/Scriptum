package sgtmelon.scriptum.repository.room.develop

/**
 * Interface for communication with [DevelopRepo]
 *
 * @author SerjantArbuz
 */
interface IDevelopRepo {

    suspend fun getNoteTablePrint() : String

    suspend fun getRollTablePrint() : String

    suspend fun getRankTablePrint() : String

    suspend fun getPreferencePrint() : String

}