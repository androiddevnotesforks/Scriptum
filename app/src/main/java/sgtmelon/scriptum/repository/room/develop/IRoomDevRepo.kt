package sgtmelon.scriptum.repository.room.develop

/**
 * Interface for communication with [RoomDevRepo]
 *
 * @author SerjantArbuz
 */
interface IRoomDevRepo {

    suspend fun getNoteTablePrint() : String

    suspend fun getRollTablePrint() : String

    suspend fun getRankTablePrint() : String

    suspend fun getPreferencePrint() : String

}