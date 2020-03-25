package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.DevelopRepo

/**
 * Interface for communicate with [DevelopRepo]
 */
interface IDevelopRepo {

    suspend fun getNoteTablePrint() : String

    suspend fun getRollTablePrint() : String

    suspend fun getRankTablePrint() : String

    suspend fun getPreferencePrint() : String

}