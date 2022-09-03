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
    private val selector: BackupParserSelector,
    private val colorConverter: ColorConverter,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) : BackupParser {

    override fun collect(model: ParserResult): String = JSONObject().apply {
        val data = collectDatabase(model)

        put(dataSource.versionKey, VERSION)
        put(dataSource.hashKey, hashMaker.get(data))
        put(dataSource.databaseKey, data)
    }.toString()

    @RunPrivate fun collectDatabase(model: ParserResult): String = JSONObject().apply {
        put(Note.TABLE, collectNoteTable(model.noteList))
        put(Roll.TABLE, collectRollTable(model.rollList))
        put(RollVisible.TABLE, collectRollVisibleTable(model.rollVisibleList))
        put(Rank.TABLE, collectRankTable(model.rankList))
        put(Alarm.TABLE, collectAlarmTable(model.alarmList))
    }.toString()

    @RunPrivate fun collectNoteTable(noteList: List<NoteEntity>): String {
        return JSONArray().apply {
            for (it in noteList) {
                put(JSONObject().apply {
                    put(Note.ID, it.id)
                    put(Note.CREATE, it.create)
                    put(Note.CHANGE, it.change)
                    put(Note.NAME, it.name)
                    put(Note.TEXT, it.text)
                    put(Note.COLOR, colorConverter.toInt(it.color))
                    put(Note.TYPE, typeConverter.toInt(it.type))
                    put(Note.RANK_ID, it.rankId)
                    put(Note.RANK_PS, it.rankPs)
                    put(Note.BIN, it.isBin)
                    put(Note.STATUS, it.isStatus)
                })
            }
        }.toString()
    }

    @RunPrivate fun collectRollTable(rollList: List<RollEntity>): String {
        return JSONArray().apply {
            for (it in rollList) {
                put(JSONObject().apply {
                    put(Roll.ID, it.id)
                    put(Roll.NOTE_ID, it.noteId)
                    put(Roll.POSITION, it.position)
                    put(Roll.CHECK, it.isCheck)
                    put(Roll.TEXT, it.text)
                })
            }
        }.toString()
    }

    @RunPrivate fun collectRollVisibleTable(rollVisibleList: List<RollVisibleEntity>): String {
        return JSONArray().apply {
            for (it in rollVisibleList) {
                put(JSONObject().apply {
                    put(RollVisible.ID, it.id)
                    put(RollVisible.NOTE_ID, it.noteId)
                    put(RollVisible.VALUE, it.value)
                })
            }
        }.toString()
    }

    @RunPrivate fun collectRankTable(rankList: List<RankEntity>): String {
        return JSONArray().apply {
            for (it in rankList) {
                put(JSONObject().apply {
                    put(Rank.ID, it.id)
                    put(Rank.NOTE_ID, stringConverter.toString(it.noteId))
                    put(Rank.POSITION, it.position)
                    put(Rank.NAME, it.name)
                    put(Rank.VISIBLE, it.isVisible)
                })
            }
        }.toString()
    }

    @RunPrivate fun collectAlarmTable(alarmList: List<AlarmEntity>): String {
        return JSONArray().apply {
            for (it in alarmList) {
                put(JSONObject().apply {
                    put(Alarm.ID, it.id)
                    put(Alarm.NOTE_ID, it.noteId)
                    put(Alarm.DATE, it.date)
                })
            }
        }.toString()
    }


    override fun parse(data: String): ParserResult? {
        try {
            val jsonObject = JSONObject(data)

            val version = jsonObject.getInt(dataSource.versionKey)
            val hash = jsonObject.getString(dataSource.hashKey)
            val database = jsonObject.getString(dataSource.databaseKey)

            if (hash != hashMaker.get(database)) return null

            return selector.parse(database, version)
        } catch (e: Throwable) {
            BackupParserException(e).record()
        }

        return null
    }

    companion object {
        /**
         * When update version need add case inside [BackupParserSelectorImpl].
         */
        const val VERSION = 1
    }
}