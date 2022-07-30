package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.presentation.adapter.PrintAdapter
import java.io.File as JavaFile

/**
 * Model for store information about entities and preference keys, use in [PrintAdapter].
 */
sealed class PrintItem {

    data class Note(val entity: NoteEntity) : PrintItem()

    data class Roll(val entity: RollEntity) : PrintItem()

    data class Visible(val entity: RollVisibleEntity) : PrintItem()

    data class Rank(val entity: RankEntity) : PrintItem()

    data class Alarm(val entity: AlarmEntity) : PrintItem()

    sealed class Preference : PrintItem() {
        data class Title(@StringRes val title: Int) : Preference()

        data class Key(val key: String, val def: String, val value: String) : Preference() {
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

        data class Path(val file: JavaFile) : Preference()

        data class File(val file: FileItem) : Preference()
    }
}