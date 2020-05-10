package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.DevelopRepo
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity

/**
 * Interface for communicate with [DevelopRepo]
 */
interface IDevelopRepo {

    suspend fun getNoteList(): List<NoteEntity>?

    suspend fun getRollList(): List<RollEntity>?

    suspend fun getRankList(): List<RankEntity>?

}