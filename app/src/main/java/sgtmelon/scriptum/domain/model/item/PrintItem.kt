package sgtmelon.scriptum.domain.model.item

import androidx.annotation.StringRes
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.presentation.adapter.PrintAdapter

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
        data class Divider(@StringRes val title: Int) : Preference()

        data class Item(val key: String, val def: String, val value: String) : Preference() {
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

        data class Custom(@StringRes val title: Int, val value: String) : Preference()
    }
}