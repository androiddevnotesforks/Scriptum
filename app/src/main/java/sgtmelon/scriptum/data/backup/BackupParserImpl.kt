package sgtmelon.scriptum.data.backup

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for parsing application data (rely on different versions).
 */
class BackupParserImpl(
    private val dataSource: BackupDataSource,
    private val hashMaker: BackupHashMaker,
    private val jsonConverter: BackupJsonConverter
) : BackupParser {

    override fun convert(data: String): ParserResult.Import? {
        try {
            val dataObject = JSONObject(data)

            val version = dataObject.getInt(dataSource.versionKey)
            val hash = dataObject.getString(dataSource.hashKey)
            val database = dataObject.getString(dataSource.databaseKey)

            if (hash != hashMaker.get(database)) return null

            return convert(database, version)
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    private fun convert(database: String, version: Int): ParserResult.Import? {
        return when (version) {
            1 -> getModelV1(database)
            else -> null
        }
    }

    //region Version 1

    @Throws(JSONException::class)
    private fun getModelV1(database: String): ParserResult.Import {
        val dataObject = JSONObject(database)
        val noteTable = getTable(dataObject, DbData.Note.TABLE)
        val rollTable = getTable(dataObject, DbData.Roll.TABLE)
        val visibleTable = getTable(dataObject, DbData.RollVisible.TABLE)
        val rankTable = getTable(dataObject, DbData.Rank.TABLE)
        val alarmTable = getTable(dataObject, DbData.Alarm.TABLE)

        val noteList = getTableList(noteTable) { jsonConverter.toNoteV1(it) }
        val rollList = getTableList(rollTable) { jsonConverter.toRollV1(it) }
        val visibleList = getTableList(visibleTable) { jsonConverter.toRollVisibleV1(it) }
        val rankList = getTableList(rankTable) { jsonConverter.toRankV1(it) }
        val alarmList = getTableList(alarmTable) { jsonConverter.toAlarmV1(it) }

        return ParserResult.Import(noteList, rollList, visibleList, rankList, alarmList)
    }

    @Throws(JSONException::class)
    private fun getTable(dataObject: JSONObject, key: String): JSONArray {
        return JSONArray(dataObject.getString(key))
    }

    private inline fun <T> getTableList(
        tableArray: JSONArray,
        getEntity: (json: JSONObject) -> T?
    ): MutableList<T> {
        val list = mutableListOf<T>()

        for (i in 0 until tableArray.length()) {
            try {
                val entity = getEntity(tableArray.getJSONObject(i)) ?: continue
                list.add(entity)
            } catch (e: Throwable) {
                BackupParserException(e).record()
            }
        }

        return list
    }

    //endregion

    companion object {
        /**
         * When update version need add case inside [convert] func.
         */
        const val VERSION = 1
    }
}