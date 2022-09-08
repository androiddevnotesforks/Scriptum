package sgtmelon.scriptum.data.backup

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.prod.RunPrivate

/**
 * Class for collect application data inside single string.
 */
class BackupCollectorImpl(
    private val dataSource: BackupDataSource,
    private val hashMaker: BackupHashMaker,
    private val jsonConverter: BackupJsonConverter
) : BackupCollector {

    override fun convert(result: ParserResult.Export): String? {
        val database = convertDatabase(result) ?: return null

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

    @RunPrivate fun convertDatabase(result: ParserResult.Export): String? {
        try {
            val noteTable = collectTable(result.noteList) { jsonConverter.toJson(it) }
            val rollTable = collectTable(result.rollList) { jsonConverter.toJson(it) }
            val visibleTable = collectTable(result.rollVisibleList) { jsonConverter.toJson(it) }
            val rankTable = collectTable(result.rankList) { jsonConverter.toJson(it) }
            val alarmTable = collectTable(result.alarmList) { jsonConverter.toJson(it) }

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
    private inline fun <T> collectTable(list: List<T>, toJson: (entity: T) -> JSONObject): String {
        val array = JSONArray()

        for (it in list) {
            array.put(toJson(it))
        }

        return array.toString()
    }
}