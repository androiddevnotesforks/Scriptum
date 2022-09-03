package sgtmelon.scriptum.cleanup.data.room.backup

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sgtmelon.common.test.annotation.RunPrivate
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
    private val jsonConverter: BackupJsonConverter
) : BackupCollector {

    override fun convert(model: ParserResult): String? {
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
            val noteTable = collectTable(model.noteList) { jsonConverter.toJson(it) }
            val rollTable = collectTable(model.rollList) { jsonConverter.toJson(it) }
            val visibleTable = collectTable(model.rollVisibleList) { jsonConverter.toJson(it) }
            val rankTable = collectTable(model.rankList) { jsonConverter.toJson(it) }
            val alarmTable = collectTable(model.alarmList) { jsonConverter.toJson(it) }

            return JSONObject()
                .put(DbData.Note.TABLE, noteTable)
                .put(DbData.Roll.TABLE, rollTable)
                .put(DbData.RollVisible.TABLE, visibleTable)
                .put(DbData.Rank.TABLE, rankTable)
                .put(DbData.Alarm.TABLE, alarmTable)
                .toString()
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    @Throws(JSONException::class)
    private inline fun <T> collectTable(list: List<T>, toJson: (T) -> JSONObject): String {
        val array = JSONArray()

        for (it in list) {
            array.put(toJson(it))
        }

        return array.toString()
    }
}