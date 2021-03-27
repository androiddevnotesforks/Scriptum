package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.DevelopRepo

/**
 * Interface for communicate with [DevelopRepo]
 */
interface IDevelopRepo {

    // tODO

    suspend fun getRandomNoteId(): Long
}