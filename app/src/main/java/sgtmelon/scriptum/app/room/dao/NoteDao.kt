package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter

/**
 * Класс для общения Dao заметок [RoomDb]
 *
 * @author SerjantArbuz
 * @version 1.0
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
     * @param idList - Id заметок
     * @param type   - Тип заметки
     * @return - Количество заметок по датам создания
     */
    @Query(value = "SELECT COUNT(NT_ID) FROM NOTE_TABLE WHERE NT_ID IN(:idList) AND NT_TYPE = :type")
    fun getCount(idList: List<Long>, type: Int): Int // TODO !! проверить конвертирование типов

}