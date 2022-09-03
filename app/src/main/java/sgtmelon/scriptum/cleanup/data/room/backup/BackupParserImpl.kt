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
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for help control backup file parsing.
 */
class BackupParserImpl(
    private val dataSource: BackupDataSource,
    private val hashMaker: BackupHashMaker,
    private val colorConverter: ColorConverter,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) : BackupParser {

    override fun convert(data: String): ParserResult? {
        try {
            val jsonObject = JSONObject(data)

            val version = jsonObject.getInt(dataSource.versionKey)
            val hash = jsonObject.getString(dataSource.hashKey)
            val database = jsonObject.getString(dataSource.databaseKey)

            if (hash != hashMaker.get(database)) return null

            return convert(database, version)
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    @RunPrivate fun convert(data: String, version: Int): ParserResult? {
        return when (version) {
            1 -> getModelV1(data)
            else -> null
        }
    }

    //region Version 1

    @RunPrivate fun getModelV1(data: String): ParserResult? {
        try {
            val jsonObject = JSONObject(data)
            val noteTable = JSONArray(jsonObject.getString(DbData.Note.TABLE))
            val rollTable = JSONArray(jsonObject.getString(DbData.Roll.TABLE))
            val rollVisibleTable = JSONArray(jsonObject.getString(DbData.RollVisible.TABLE))
            val rankTable = JSONArray(jsonObject.getString(DbData.Rank.TABLE))
            val alarmTable = JSONArray(jsonObject.getString(DbData.Alarm.TABLE))

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
        val type = typeConverter.toEnum(jsonObject.getInt(DbData.Note.TYPE)) ?: return null
        val color = colorConverter.toEnum(jsonObject.getInt(DbData.Note.COLOR)) ?: return null

        return NoteEntity(
            jsonObject.getLong(DbData.Note.ID),
            jsonObject.getString(DbData.Note.CREATE),
            jsonObject.getString(DbData.Note.CHANGE),
            jsonObject.getString(DbData.Note.NAME),
            jsonObject.getString(DbData.Note.TEXT),
            color,
            type,
            jsonObject.getLong(DbData.Note.RANK_ID),
            jsonObject.getInt(DbData.Note.RANK_PS),
            jsonObject.getBoolean(DbData.Note.BIN),
            jsonObject.getBoolean(DbData.Note.STATUS)
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
            jsonObject.getLong(DbData.Roll.ID),
            jsonObject.getLong(DbData.Roll.NOTE_ID),
            jsonObject.getInt(DbData.Roll.POSITION),
            jsonObject.getBoolean(DbData.Roll.CHECK),
            jsonObject.getString(DbData.Roll.TEXT)
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
            jsonObject.getLong(DbData.RollVisible.ID),
            jsonObject.getLong(DbData.RollVisible.NOTE_ID),
            jsonObject.getBoolean(DbData.RollVisible.VALUE)
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
            jsonObject.getLong(DbData.Rank.ID),
            stringConverter.toList(jsonObject.getString(DbData.Rank.NOTE_ID)),
            jsonObject.getInt(DbData.Rank.POSITION),
            jsonObject.getString(DbData.Rank.NAME),
            jsonObject.getBoolean(DbData.Rank.VISIBLE)
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
            jsonObject.getLong(DbData.Alarm.ID),
            jsonObject.getLong(DbData.Alarm.NOTE_ID),
            jsonObject.getString(DbData.Alarm.DATE)
        )
    }

    //endregion

    companion object {
        /** When update version need add case inside [convert] func. */
        const val VERSION = 1
    }
}