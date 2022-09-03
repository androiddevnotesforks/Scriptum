package sgtmelon.scriptum.cleanup.domain.model.result

import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

/**
 * Class for result processing of [BackupParser.convert].
 */
data class ParserResult(
    val noteList: List<NoteEntity>,
    val rollList: List<RollEntity>,
    val rollVisibleList: List<RollVisibleEntity>,
    val rankList: List<RankEntity>,
    val alarmList: List<AlarmEntity>
)