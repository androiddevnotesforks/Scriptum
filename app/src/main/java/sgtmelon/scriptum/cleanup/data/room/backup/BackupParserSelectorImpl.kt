package sgtmelon.scriptum.cleanup.data.room.backup

import org.json.JSONArray
import org.json.JSONObject
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for parsing different versions of backup files.
 *
 * Need parse here because it's not common thing, like [BackupParser.collect]. Parsing may change
 * by versions and because of that we can't use here some annotations
 * like in [BackupParser.collect].
 */
class BackupParserSelectorImpl(
    private val colorConverter: ColorConverter,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) : BackupParserSelector {

    override fun parse(roomData: String, version: Int): ParserResult? {
        return when (version) {
            1 -> getModelV1(roomData)
            else -> null
        }
    }

    //region Version 1

    @RunPrivate fun getModelV1(roomData: String): ParserResult? {
        try {
            val jsonObject = JSONObject(roomData)
            val noteTable = JSONArray(jsonObject.getString(Note.TABLE))
            val rollTable = JSONArray(jsonObject.getString(Roll.TABLE))
            val rollVisibleTable = JSONArray(jsonObject.getString(RollVisible.TABLE))
            val rankTable = JSONArray(jsonObject.getString(Rank.TABLE))
            val alarmTable = JSONArray(jsonObject.getString(Alarm.TABLE))

            val noteList = getNoteTableV1(noteTable)
            val rollList = getRollTableV1(rollTable)
            val rollVisibleList = getRollVisibleTableV1(rollVisibleTable)
            val rankList = getRankTableV1(rankTable)
            val alarmList = getAlarmTableV1(alarmTable)

            return ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    @RunPrivate fun getNoteTableV1(jsonArray: JSONArray): List<NoteEntity> {
        val list = mutableListOf<NoteEntity>()

        for (i in 0 until jsonArray.length()) {
            try {
                val entity = getNoteEntityV1(jsonArray.getJSONObject(i)) ?: continue
                list.add(entity)
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    @RunPrivate fun getNoteEntityV1(jsonObject: JSONObject): NoteEntity? {
        val type = typeConverter.toEnum(jsonObject.getInt(Note.TYPE)) ?: return null
        val color = colorConverter.toEnum(jsonObject.getInt(Note.COLOR)) ?: return null

        return NoteEntity(
            jsonObject.getLong(Note.ID),
            jsonObject.getString(Note.CREATE),
            jsonObject.getString(Note.CHANGE),
            jsonObject.getString(Note.NAME),
            jsonObject.getString(Note.TEXT),
            color,
            type,
            jsonObject.getLong(Note.RANK_ID),
            jsonObject.getInt(Note.RANK_PS),
            jsonObject.getBoolean(Note.BIN),
            jsonObject.getBoolean(Note.STATUS)
        )
    }

    @RunPrivate fun getRollTableV1(jsonArray: JSONArray): List<RollEntity> {
        val list = mutableListOf<RollEntity>()

        for (i in 0 until jsonArray.length()) {
            try {
                list.add(getRollEntityV1(jsonArray.getJSONObject(i)))
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    @RunPrivate fun getRollEntityV1(jsonObject: JSONObject): RollEntity {
        return RollEntity(
            jsonObject.getLong(Roll.ID),
            jsonObject.getLong(Roll.NOTE_ID),
            jsonObject.getInt(Roll.POSITION),
            jsonObject.getBoolean(Roll.CHECK),
            jsonObject.getString(Roll.TEXT)
        )
    }

    @RunPrivate fun getRollVisibleTableV1(jsonArray: JSONArray): List<RollVisibleEntity> {
        val list = mutableListOf<RollVisibleEntity>()

        for (i in 0 until jsonArray.length()) {
            try {
                list.add(getRollVisibleEntityV1(jsonArray.getJSONObject(i)))
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    @RunPrivate fun getRollVisibleEntityV1(jsonObject: JSONObject): RollVisibleEntity {
        return RollVisibleEntity(
            jsonObject.getLong(RollVisible.ID),
            jsonObject.getLong(RollVisible.NOTE_ID),
            jsonObject.getBoolean(RollVisible.VALUE)
        )
    }

    @RunPrivate fun getRankTableV1(jsonArray: JSONArray): List<RankEntity> {
        val list = mutableListOf<RankEntity>()

        for (i in 0 until jsonArray.length()) {
            try {
                list.add(getRankEntityV1(jsonArray.getJSONObject(i)))
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    @RunPrivate fun getRankEntityV1(jsonObject: JSONObject): RankEntity {
        return RankEntity(
            jsonObject.getLong(Rank.ID),
            stringConverter.toList(jsonObject.getString(Rank.NOTE_ID)),
            jsonObject.getInt(Rank.POSITION),
            jsonObject.getString(Rank.NAME),
            jsonObject.getBoolean(Rank.VISIBLE)
        )
    }

    @RunPrivate fun getAlarmTableV1(jsonArray: JSONArray): List<AlarmEntity> {
        val list = mutableListOf<AlarmEntity>()

        for (i in 0 until jsonArray.length()) {
            try {
                list.add(getAlarmEntityV1(jsonArray.getJSONObject(i)))
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    @RunPrivate fun getAlarmEntityV1(jsonObject: JSONObject): AlarmEntity {
        return AlarmEntity(
            jsonObject.getLong(Alarm.ID),
            jsonObject.getLong(Alarm.NOTE_ID),
            jsonObject.getString(Alarm.DATE)
        )
    }

    //endregion

}