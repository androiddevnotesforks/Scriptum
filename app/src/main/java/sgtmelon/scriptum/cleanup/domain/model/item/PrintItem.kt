package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import java.io.File as JavaFile

/**
 * Model for store information about entities and preference keys.
 */
sealed class PrintItem(val type: Type, val id: Any?) {

    data class Note(val entity: NoteEntity) : PrintItem(Type.NOTE, entity.id)

    data class Roll(val entity: RollEntity) : PrintItem(Type.ROLL, entity.id)

    data class Visible(val entity: RollVisibleEntity) : PrintItem(Type.VISIBLE, entity.id)

    data class Rank(val entity: RankEntity) : PrintItem(Type.RANK, entity.id)

    data class Alarm(val entity: AlarmEntity) : PrintItem(Type.ALARM, entity.id)

    sealed class Preference(type: Type, id: Any) : PrintItem(type, id) {

        data class Title(@StringRes val title: Int) : Preference(Type.PREF_TITLE, title)

        data class Key(
            val key: String,
            val def: String,
            val value: String
        ) : Preference(Type.PREF_KEY, key) {

            constructor(
                key: String,
                def: Boolean,
                value: Boolean
            ) : this(key, def.toString(), value.toString())

            constructor(
                key: String,
                def: Int,
                value: Int
            ) : this(key, def.toString(), value.toString())
        }

        data class Path(val file: JavaFile) : Preference(Type.PREF_PATH, file.name)

        data class File(val file: FileItem) : Preference(Type.PREF_FILE, file.name)
    }

    enum class Type {
        NOTE, ROLL, VISIBLE, RANK, ALARM,
        PREF_TITLE, PREF_KEY, PREF_PATH, PREF_FILE
    }
}