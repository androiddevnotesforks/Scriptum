package sgtmelon.scriptum.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.entity.NoteItem

/**
 * Класс для общения Dao заметок [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface NoteDao {

    @Insert fun insert(item: NoteItem): Long

    @Delete fun delete(item: NoteItem)

    @Delete fun delete(list: List<NoteItem>)

    @Update fun update(item: NoteItem)

    @Update fun update(list: List<NoteItem>)

    @RawQuery operator fun get(query: SupportSQLiteQuery): List<NoteItem>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    operator fun get(id: Long): NoteItem

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN(:idList)")
    operator fun get(idList: List<Long>): List<NoteItem>

    @Query(value = """SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC""")
    operator fun get(bin: Boolean): MutableList<NoteItem>

    /**
     * @param idList список id заметок привязанных к категории
     * @return Количество заметок с id из списка и определённого типа
     */
    @Query(value = "SELECT COUNT(NT_ID) FROM NOTE_TABLE WHERE NT_ID IN(:idList) AND NT_TYPE = :type")
    fun getCount(idList: List<Long>, type: Int): Int

}