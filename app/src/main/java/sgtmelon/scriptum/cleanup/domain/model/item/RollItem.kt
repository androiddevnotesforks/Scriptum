package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.adapter.RollAdapter
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.database.DbData.Roll
import sgtmelon.scriptum.infrastructure.database.DbData.Roll.Default

/**
 * Model for store short information about roll, use in [RollAdapter].
 *
 * [uniqueId] - needed for compare not created [RollItem]'s (without [id]).
 *
 * But we must not rely on [uniqueId] value when we compare two items (using [equals] and
 * [hashCode]). Because may be the same item (equals by [id]), but [uniqueId] was generated
 * and it's different.
 *
 * Example: in notes screen every onResume fetch new list with data (for fetch changes), and
 * every fetch we will get different [uniqueId] on the same items.
 */
@Serializable
@TypeConverters(BoolConverter::class)
data class RollItem(
    @ColumnInfo(name = Roll.ID) var id: Long? = Default.ID,
    @ColumnInfo(name = Roll.POSITION) var position: Int,
    @ColumnInfo(name = Roll.CHECK) var isCheck: Boolean = Default.CHECK,
    @ColumnInfo(name = Roll.TEXT) var text: String,
    val uniqueId: String = sgtmelon.extensions.uniqueId
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RollItem

        if (id != other.id) return false
        if (position != other.position) return false
        if (isCheck != other.isCheck) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + position
        result = 31 * result + isCheck.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}