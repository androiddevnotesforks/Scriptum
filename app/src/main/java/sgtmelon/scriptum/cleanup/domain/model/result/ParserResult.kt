package sgtmelon.scriptum.cleanup.domain.model.result

import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.entity.*

/**
 * Class for result processing of [IBackupParser.parse].
 */
data class ParserResult(
        val noteList: List<NoteEntity>,
        val rollList: List<RollEntity>,
        val rollVisibleList: List<RollVisibleEntity>,
        val rankList: List<RankEntity>,
        val alarmList: List<AlarmEntity>
)