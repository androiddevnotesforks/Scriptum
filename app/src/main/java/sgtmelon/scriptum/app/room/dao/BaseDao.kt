package sgtmelon.scriptum.app.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 * [NoteDao], [RankDao], [RollDao]
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface BaseDao {

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN(:id)")
    fun getNote(id: List<Long>): List<NoteItem>

    @Update fun updateNote(noteList: List<NoteItem>)

}