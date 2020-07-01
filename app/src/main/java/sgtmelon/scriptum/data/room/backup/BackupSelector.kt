package sgtmelon.scriptum.data.room.backup

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Roll
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible

/**
 * Class for parsing different versions of backup files.
 *
 * Need parse here because it's not common thing, like [IBackupParser.collect]. Parsing may change
 * by versions and because of that we can't use here some annotations
 * like in [IBackupParser.collect].
 */
class BackupSelector(
        private val typeConverter: NoteTypeConverter,
        private val stringConverter: StringConverter
) {

    fun parseByVersion(roomData: String, version: Int): BackupParser.Model? {
        return when (version) {
            1 -> getModelV1(roomData)
            else -> null
        }
    }

    //region Version 1

    @RunPrivate fun getModelV1(roomData: String): BackupParser.Model? {
        try {
            val jsonObject = JSONObject(roomData)
            val noteTable = jsonObject.getJSONArray(Note.TABLE)
            val rollTable = jsonObject.getJSONArray(Roll.TABLE)
            val rollVisibleTable = jsonObject.getJSONArray(RollVisible.TABLE)
            val rankTable = jsonObject.getJSONArray(Rank.TABLE)
            val alarmTable = jsonObject.getJSONArray(Alarm.TABLE)

            val noteList = getNoteTableV1(noteTable) ?: return null
            val rollList = getRollTableV1(rollTable) ?: return null
            val rollVisibleList = getRollVisibleTableV1(rollVisibleTable) ?: return null
            val rankList = getRankTableV1(rankTable) ?: return null
            val alarmList = getAlarmTableV1(alarmTable) ?: return null

            return BackupParser.Model(noteList, rollList, rollVisibleList, rankList, alarmList)
        } catch (e: Throwable) {
            Log.i(TAG, e.toString())
        }

        return null
    }

    @RunPrivate fun getNoteTableV1(jsonArray: JSONArray): List<NoteEntity>? {
        val list = mutableListOf<NoteEntity>()

        for (i in 0 until jsonArray.length()) {
            (jsonArray.get(i) as? JSONObject)?.apply {
                val type = typeConverter.toEnum(getInt(Note.TYPE)) ?: return null

                list.add(NoteEntity(
                        getLong(Note.ID),
                        getString(Note.CREATE),
                        getString(Note.CHANGE),
                        getString(Note.NAME),
                        getString(Note.TEXT),
                        getInt(Note.COLOR),
                        type,
                        getLong(Note.RANK_ID),
                        getInt(Note.RANK_PS),
                        getBoolean(Note.BIN),
                        getBoolean(Note.STATUS)
                ))
            } ?: return null
        }

        return list
    }

    @RunPrivate fun getRollTableV1(jsonArray: JSONArray): List<RollEntity>? {
        val list = mutableListOf<RollEntity>()

        for (i in 0 until jsonArray.length()) {
            (jsonArray.get(i) as? JSONObject)?.apply {
                list.add(RollEntity(
                        getLong(Roll.ID),
                        getLong(Roll.NOTE_ID),
                        getInt(Roll.POSITION),
                        getBoolean(Roll.CHECK),
                        getString(Roll.TEXT)
                ))
            } ?: return null
        }

        return list
    }

    @RunPrivate fun getRollVisibleTableV1(jsonArray: JSONArray): List<RollVisibleEntity>? {
        val list = mutableListOf<RollVisibleEntity>()

        for (i in 0 until jsonArray.length()) {
            (jsonArray.get(i) as? JSONObject)?.apply {
                list.add(RollVisibleEntity(
                        getLong(RollVisible.ID),
                        getLong(RollVisible.NOTE_ID),
                        getBoolean(RollVisible.VALUE)
                ))
            } ?: return null
        }

        return list
    }

    @RunPrivate fun getRankTableV1(jsonArray: JSONArray): List<RankEntity>? {
        val list = mutableListOf<RankEntity>()

        for (i in 0 until jsonArray.length()) {
            (jsonArray.get(i) as? JSONObject)?.apply {
                list.add(RankEntity(
                        getLong(Rank.ID),
                        stringConverter.toList(getString(Rank.NOTE_ID)),
                        getInt(Rank.POSITION),
                        getString(Rank.NAME),
                        getBoolean(Rank.VISIBLE)
                ))
            } ?: return null
        }

        return list
    }

    @RunPrivate fun getAlarmTableV1(jsonArray: JSONArray): List<AlarmEntity>? {
        val list = mutableListOf<AlarmEntity>()

        for (i in 0 until jsonArray.length()) {
            (jsonArray.get(i) as? JSONObject)?.apply {
                list.add(AlarmEntity(
                        getLong(Alarm.ID),
                        getLong(Alarm.NOTE_ID),
                        getString(Alarm.DATE)
                ))
            } ?: return null
        }

        return list
    }

    //endregion

    companion object {
        private val TAG = BackupSelector::class.java.simpleName
    }

}