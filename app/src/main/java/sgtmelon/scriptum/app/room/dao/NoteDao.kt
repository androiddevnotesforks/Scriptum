package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter

/**
 * Класс для общения Dao заметок [RoomDb]
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface NoteDao {

    @Insert fun insert(item: NoteItem): Long

    @Delete fun delete(item: NoteItem)

    @Delete fun delete(list: List<NoteItem>)

    @Update fun update(item: NoteItem)

    @Update fun update(list: List<NoteItem>)

    @Query(value = "UPDATE NOTE_TABLE SET NT_CHANGE = :change, NT_BIN = :bin WHERE NT_ID = :id")
    fun update(id: Long, change: String, bin: Boolean)

    @Query(value = "UPDATE NOTE_TABLE SET NT_STATUS = :status WHERE NT_ID = :id")
    fun update(id: Long, status: Boolean)

    @RawQuery fun get(query: SupportSQLiteQuery): List<NoteItem>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    fun get(id: Long): NoteItem

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN(:idList)")
    fun get(idList: List<Long>): List<NoteItem>

    @Query(value = """SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC""")
    fun get(bin: Boolean): MutableList<NoteItem>

    /**
     * @param idList - Id заметок
     * @param type   - Тип заметки
     * @return - Количество заметок по датам создания
     */
    @Query(value = "SELECT COUNT(NT_ID) FROM NOTE_TABLE WHERE NT_ID IN(:idList) AND NT_TYPE = :type")
    fun getCount(idList: List<Long>, type: Int): Int // TODO !! проверить конвертирование типов

}