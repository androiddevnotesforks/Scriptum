package sgtmelon.scriptum.data.backup

import org.json.JSONException
import org.json.JSONObject
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter

class BackupJsonConverter(
    private val colorConverter: ColorConverter,
    private val typeConverter: NoteTypeConverter,
    private val stringConverter: StringConverter
) {

    //region toJson

    @Throws(JSONException::class)
    fun toJson(entity: NoteEntity): JSONObject {
        return JSONObject()
            .put(DbData.Note.ID, entity.id)
            .put(DbData.Note.CREATE, entity.create)
            .put(DbData.Note.CHANGE, entity.change)
            .put(DbData.Note.NAME, entity.name)
            .put(DbData.Note.TEXT, entity.text)
            .put(DbData.Note.COLOR, colorConverter.toInt(entity.color))
            .put(DbData.Note.TYPE, typeConverter.toInt(entity.type))
            .put(DbData.Note.RANK_ID, entity.rankId)
            .put(DbData.Note.RANK_PS, entity.rankPs)
            .put(DbData.Note.BIN, entity.isBin)
            .put(DbData.Note.STATUS, entity.isStatus)
    }

    @Throws(JSONException::class)
    fun toJson(entity: RollEntity): JSONObject {
        return JSONObject()
            .put(DbData.Roll.ID, entity.id)
            .put(DbData.Roll.NOTE_ID, entity.noteId)
            .put(DbData.Roll.POSITION, entity.position)
            .put(DbData.Roll.CHECK, entity.isCheck)
            .put(DbData.Roll.TEXT, entity.text)
    }

    @Throws(JSONException::class)
    fun toJson(entity: RollVisibleEntity): JSONObject {
        return JSONObject()
            .put(DbData.RollVisible.ID, entity.id)
            .put(DbData.RollVisible.NOTE_ID, entity.noteId)
            .put(DbData.RollVisible.VALUE, entity.value)
    }

    @Throws(JSONException::class)
    fun toJson(entity: RankEntity): JSONObject {
        return JSONObject()
            .put(DbData.Rank.ID, entity.id)
            .put(DbData.Rank.NOTE_ID, stringConverter.toString(entity.noteId))
            .put(DbData.Rank.POSITION, entity.position)
            .put(DbData.Rank.NAME, entity.name)
            .put(DbData.Rank.VISIBLE, entity.isVisible)

    }

    @Throws(JSONException::class)
    fun toJson(entity: AlarmEntity): JSONObject {
        return JSONObject()
            .put(DbData.Alarm.ID, entity.id)
            .put(DbData.Alarm.NOTE_ID, entity.noteId)
            .put(DbData.Alarm.DATE, entity.date)
    }

    //endregion

    //region toEntityV1

    fun toNoteV1(jsonObject: JSONObject): NoteEntity? {
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

    fun toRollV1(jsonObject: JSONObject): RollEntity {
        return RollEntity(
            jsonObject.getLong(DbData.Roll.ID),
            jsonObject.getLong(DbData.Roll.NOTE_ID),
            jsonObject.getInt(DbData.Roll.POSITION),
            jsonObject.getBoolean(DbData.Roll.CHECK),
            jsonObject.getString(DbData.Roll.TEXT)
        )
    }

    fun toRollVisibleV1(jsonObject: JSONObject): RollVisibleEntity {
        return RollVisibleEntity(
            jsonObject.getLong(DbData.RollVisible.ID),
            jsonObject.getLong(DbData.RollVisible.NOTE_ID),
            jsonObject.getBoolean(DbData.RollVisible.VALUE)
        )
    }

    fun toRankV1(jsonObject: JSONObject): RankEntity {
        return RankEntity(
            jsonObject.getLong(DbData.Rank.ID),
            stringConverter.toList(jsonObject.getString(DbData.Rank.NOTE_ID)),
            jsonObject.getInt(DbData.Rank.POSITION),
            jsonObject.getString(DbData.Rank.NAME),
            jsonObject.getBoolean(DbData.Rank.VISIBLE)
        )
    }

    fun toAlarmV1(jsonObject: JSONObject): AlarmEntity {
        return AlarmEntity(
            jsonObject.getLong(DbData.Alarm.ID),
            jsonObject.getLong(DbData.Alarm.NOTE_ID),
            jsonObject.getString(DbData.Alarm.DATE)
        )
    }

    //endregion
}