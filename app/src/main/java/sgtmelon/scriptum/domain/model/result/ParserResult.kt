package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

/**
 * Class with result of backup processing (export/import).
 */
data class ParserResult(
    val noteList: List<NoteEntity>,
    val rollList: List<RollEntity>,
    val rollVisibleList: List<RollVisibleEntity>,
    val rankList: List<RankEntity>,
    val alarmList: List<AlarmEntity>
)