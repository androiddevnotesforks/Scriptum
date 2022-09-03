package sgtmelon.scriptum.cleanup.data.room.backup

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for collect application data inside single string.
 */
class BackupCollectorImpl(
    private val dataSource: BackupDataSource,
    private val hashMaker: BackupHashMaker,
    private val jsonConverter: EntityJsonConverter
) : BackupCollector {

    override fun collect(model: ParserResult): String? {
        val database = collectDatabase(model) ?: return null

        try {
            return JSONObject()
                .put(dataSource.versionKey, BackupParserImpl.VERSION)
                .put(dataSource.hashKey, hashMaker.get(database))
                .put(dataSource.databaseKey, database)
                .toString()
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    @RunPrivate fun collectDatabase(model: ParserResult): String? {
        try {
            return JSONObject()
                .put(DbData.Note.TABLE, collectNoteTable(model.noteList))
                .put(DbData.Roll.TABLE, collectRollTable(model.rollList))
                .put(DbData.RollVisible.TABLE, collectRollVisibleTable(model.rollVisibleList))
                .put(DbData.Rank.TABLE, collectRankTable(model.rankList))
                .put(DbData.Alarm.TABLE, collectAlarmTable(model.alarmList))
                .toString()
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    @Throws(JSONException::class)
    @RunPrivate fun collectNoteTable(list: List<NoteEntity>): String {
        val array = JSONArray()

        for (it in list) {
            array.put(jsonConverter.toJson(it))
        }

        return array.toString()
    }

    @Throws(JSONException::class)
    @RunPrivate fun collectRollTable(list: List<RollEntity>): String {
        val array = JSONArray()

        for (it in list) {
            array.put(jsonConverter.toJson(it))
        }

        return array.toString()
    }

    @Throws(JSONException::class)
    @RunPrivate fun collectRollVisibleTable(list: List<RollVisibleEntity>): String {
        val array = JSONArray()

        for (it in list) {
            array.put(jsonConverter.toJson(it))
        }

        return array.toString()
    }

    @Throws(JSONException::class)
    @RunPrivate fun collectRankTable(list: List<RankEntity>): String {
        val array = JSONArray()

        for (it in list) {
            array.put(jsonConverter.toJson(it))
        }

        return array.toString()
    }

    @Throws(JSONException::class)
    @RunPrivate fun collectAlarmTable(list: List<AlarmEntity>): String {
        val array = JSONArray()

        for (it in list) {
            array.put(jsonConverter.toJson(it))
        }

        return array.toString()
    }
}