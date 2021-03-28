package sgtmelon.scriptum.domain.model.item

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
    data class Preference(val title: String, val value: String) : PrintItem()
}