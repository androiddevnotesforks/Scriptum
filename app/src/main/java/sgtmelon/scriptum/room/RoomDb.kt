package sgtmelon.scriptum.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.room.dao.IAlarmDao
import sgtmelon.scriptum.room.dao.INoteDao
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.dao.IRollDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Class for communication with [RoomDatabase]
 */
@Database(entities = [
    NoteEntity::class,
    RollEntity::class,
    RankEntity::class,
    AlarmEntity::class
], version = RoomDb.VERSION)
abstract class RoomDb : RoomDatabase() {

    abstract val iNoteDao: INoteDao

    abstract val iRollDao: IRollDao

    abstract val iRankDao: IRankDao

    abstract val iAlarmDao: IAlarmDao

    companion object {
        const val VERSION = 7

        operator fun get(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(*Migrate.sequence)
                    .build()
        }
    }

}