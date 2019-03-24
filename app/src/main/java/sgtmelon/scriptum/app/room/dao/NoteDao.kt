package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.room.RoomDb

/**
 * Класс для общения Dao заметок [RoomDb]
 */
@Dao
interface NoteDao : BaseDao {

    @Insert fun insert(noteItem: NoteItem): Long

    @Delete fun delete(noteItem: NoteItem)

    @Delete fun delete(lisNote: List<NoteItem>)

    @Update fun update(noteItem: NoteItem)

    @Query(value = "UPDATE NOTE_TABLE SET NT_CHANGE = :change, NT_BIN = :bin WHERE NT_ID = :id")
    fun update(id: Long, change: String, bin: Boolean)

    @Query(value = "UPDATE NOTE_TABLE SET NT_STATUS = :status WHERE NT_ID = :id")
    fun update(id: Long, status: Boolean)

    @RawQuery fun get(query: SupportSQLiteQuery): List<NoteItem>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    fun get(id: Long): NoteItem

    @Query(value = """SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC""")
    fun get(bin: Boolean): MutableList<NoteItem>

}