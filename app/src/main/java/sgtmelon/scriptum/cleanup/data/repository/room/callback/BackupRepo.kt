package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult

/**
 * Interface for [BackupRepoImpl].
 */
interface BackupRepo {

    suspend fun getNoteList(): List<NoteEntity>

    suspend fun getRollList(noteIdList: List<Long>): List<RollEntity>

    suspend fun getRollVisibleList(noteIdList: List<Long>): List<RollVisibleEntity>

    suspend fun getRankList(): List<RankEntity>

    suspend fun getAlarmList(noteIdList: List<Long>): List<AlarmEntity>

    suspend fun insertData(model: BackupRepoImpl.Model, isSkipImports: Boolean): ImportResult
}