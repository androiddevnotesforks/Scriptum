package sgtmelon.scriptum.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.entity.NoteEntity

/**
 * Класс для общения Dao заметок [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface NoteDao {

    @Insert fun insert(noteEntity: NoteEntity): Long

    @Delete fun delete(noteEntity: NoteEntity)

    @Delete fun delete(list: List<NoteEntity>)

    @Update fun update(noteEntity: NoteEntity)

    @Update fun update(list: List<NoteEntity>)

    @RawQuery operator fun get(query: SupportSQLiteQuery): List<NoteEntity>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    operator fun get(id: Long): NoteEntity

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN(:idList)")
    operator fun get(idList: List<Long>): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC""")
    operator fun get(bin: Boolean): MutableList<NoteEntity>

    /**
     * @param idList список id заметок привязанных к категории
     * @return Количество заметок с id из списка и определённого типа
     */
    @Query(value = "SELECT COUNT(NT_ID) FROM NOTE_TABLE WHERE NT_ID IN(:idList) AND NT_TYPE = :type")
    fun getCount(idList: List<Long>, type: Int): Int

}