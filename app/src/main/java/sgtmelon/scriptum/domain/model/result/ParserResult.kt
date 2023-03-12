package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

/**
 * Class with result of backup processing (export/import).
 */
sealed class ParserResult {

    abstract val noteList: List<NoteEntity>
    abstract val rollList: List<RollEntity>
    abstract val rollVisibleList: List<RollVisibleEntity>
    abstract val rankList: List<RankEntity>
    abstract val alarmList: List<AlarmEntity>

    data class Export(
        override val noteList: List<NoteEntity>,
        override val rollList: List<RollEntity>,
        override val rollVisibleList: List<RollVisibleEntity>,
        override val rankList: List<RankEntity>,
        override val alarmList: List<AlarmEntity>
    ) : ParserResult()

    data class Import(
        override val noteList: MutableList<NoteEntity>,
        override val rollList: MutableList<RollEntity>,
        override val rollVisibleList: MutableList<RollVisibleEntity>,
        override val rankList: MutableList<RankEntity>,
        override val alarmList: MutableList<AlarmEntity>
    ) : ParserResult()
}