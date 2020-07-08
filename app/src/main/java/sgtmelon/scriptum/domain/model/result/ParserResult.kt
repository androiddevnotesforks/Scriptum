package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.data.room.entity.*

data class ParserResult(
        val noteList: List<NoteEntity>,
        val rollList: List<RollEntity>,
        val rollVisibleList: List<RollVisibleEntity>,
        val rankList: List<RankEntity>,
        val alarmList: List<AlarmEntity>
)