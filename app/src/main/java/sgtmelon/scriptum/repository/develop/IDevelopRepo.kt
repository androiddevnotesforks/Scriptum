package sgtmelon.scriptum.repository.develop

/**
 * Interface for communication with [DevelopRepo]
 *
 * @author SerjantArbuz
 */
interface IDevelopRepo {

    suspend fun getNoteTableData() : String

    suspend fun getRollTableData() : String

    suspend fun getRankTableData() : String

}