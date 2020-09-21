package sgtmelon.scriptum.data.room.backup

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Roll
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.domain.model.result.ParserResult
import java.security.MessageDigest

/**
 * Class for help control backup file parsing.
 */
class BackupParser(
    private val context: Context,
    private val selector: IBackupSelector,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) : IBackupParser {

    override fun collect(model: ParserResult): String = JSONObject().apply {
        val roomData = collectRoom(model)

        put(context.getString(R.string.backup_version), VERSION)
        put(context.getString(R.string.backup_hash), getHash(roomData))
        put(context.getString(R.string.backup_room), roomData)
    }.toString()

    @RunPrivate fun collectRoom(model: ParserResult): String = JSONObject().apply {
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
                    put(Note.COLOR, it.color)
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
            val dataObject = JSONObject(data)

            val version = dataObject.getInt(context.getString(R.string.backup_version))
            val hash = dataObject.getString(context.getString(R.string.backup_hash))
            val roomData = dataObject.getString(context.getString(R.string.backup_room))

            if (hash != getHash(roomData)) return null

            return selector.parseByVersion(roomData, version)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        return null
    }


    @RunPrivate fun getHash(data: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(data.toByteArray())

        return hashToHex(hash)
    }

    @RunPrivate fun hashToHex(hash: ByteArray): String = StringBuilder().apply {
        for (it in hash.map { Integer.toHexString(0xFF and it.toInt()) }) {
            if (it.length == 1) append('0')

            append(it)
        }
    }.toString()

    companion object {
        private val TAG = BackupParser::class.java.simpleName

        /**
         * When update version need add case inside [BackupSelector].
         */
        const val VERSION = 1
    }

}