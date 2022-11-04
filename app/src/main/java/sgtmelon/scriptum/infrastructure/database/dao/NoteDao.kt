package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.model.DaoDeprecated

/**
 * Interface for communication [DbData.Note.TABLE] with [Database].
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface NoteDao {

    @Deprecated(DaoDeprecated.INSERT_IGNORE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: NoteEntity): Long

    @Delete
    suspend fun delete(entity: NoteEntity)

    @Delete
    suspend fun delete(list: List<NoteEntity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: NoteEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(list: List<NoteEntity>)


    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(
        value = """SELECT COUNT(NT_ID) FROM NOTE_TABLE
            WHERE NT_ID IN (:idList) AND NT_STATUS = 1"""
    )
    suspend fun getBindCount(idList: List<Long>): Int


    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID = :id")
    suspend fun get(id: Long): NoteEntity?

    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_ID IN (:idList)")
    suspend fun getList(idList: List<Long>): List<NoteEntity>

    @Query(value = "SELECT * FROM NOTE_TABLE WHERE NT_BIN = :isBin")
    suspend fun getList(isBin: Boolean): List<NoteEntity>

    @Query(
        value = """SELECT * FROM NOTE_TABLE
            WHERE NT_BIN = :isBin
            ORDER BY DATE(NT_CHANGE) DESC, TIME(NT_CHANGE) DESC"""
    )
    suspend fun getListByChange(isBin: Boolean): List<NoteEntity>

    @Query(
        value = """SELECT * FROM NOTE_TABLE
            WHERE NT_BIN = :isBin
            ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC"""
    )
    suspend fun getListByCreate(isBin: Boolean): List<NoteEntity>

    @Query(
        value = """SELECT * FROM NOTE_TABLE
            WHERE NT_BIN = :isBin
            ORDER BY NT_RANK_PS ASC, DATE(NT_CHANGE) DESC, TIME(NT_CHANGE) DESC"""
    )
    suspend fun getListByRank(isBin: Boolean): List<NoteEntity>

    @Query(
        value = """SELECT * FROM NOTE_TABLE
            WHERE NT_BIN = :isBin
            ORDER BY NT_COLOR ASC, DATE(NT_CHANGE) DESC, TIME(NT_CHANGE) DESC"""
    )
    suspend fun getListByColor(isBin: Boolean): List<NoteEntity>
}