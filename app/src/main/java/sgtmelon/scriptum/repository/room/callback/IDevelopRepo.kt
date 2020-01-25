package sgtmelon.scriptum.repository.room.callback

import sgtmelon.scriptum.repository.room.DevelopRepo

/**
 * Interface for communicate with [DevelopRepo]
 */
interface IDevelopRepo {

    suspend fun getNoteTablePrint() : String

    suspend fun getRollTablePrint() : String

    suspend fun getRankTablePrint() : String

    suspend fun getPreferencePrint() : String

}