package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.entity.NoteEntity

/**
 * Interface for communications [DbData.Note.TABLE] with [RoomDb]
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface INoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(noteEntity: NoteEntity): Long

    @Delete fun delete(noteEntity: NoteEntity)

    @Delete fun delete(list: List<NoteEntity>)

    @Update fun update(noteEntity: NoteEntity)

    @Update fun update(list: List<NoteEntity>)

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    operator fun get(id: Long): NoteEntity?

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN(:idList)")
    operator fun get(idList: List<Long>): List<NoteEntity>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_BIN = :bin")
    operator fun get(bin: Boolean): List<NoteEntity>


    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CHANGE) DESC, TIME(NT_CHANGE) DESC
    """)
    fun getByChange(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    fun getByCreate(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY NT_RANK_PS ASC, DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    fun getByRank(bin: Boolean): List<NoteEntity>

    @Query(value = """SELECT * FROM NOTE_TABLE
        WHERE NT_BIN = :bin
        ORDER BY NT_COLOR ASC, DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC
    """)
    fun getByColor(bin: Boolean): List<NoteEntity>

}