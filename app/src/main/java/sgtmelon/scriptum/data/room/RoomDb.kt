package sgtmelon.scriptum.data.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.data.room.dao.*
import sgtmelon.scriptum.data.room.entity.*

/**
 * Class for communication with [RoomDatabase].
 */
@Database(entities = [
    NoteEntity::class,
    RollEntity::class,
    RollVisibleEntity::class,
    RankEntity::class,
    AlarmEntity::class
], version = RoomDb.VERSION)
abstract class RoomDb : RoomDatabase() {

    abstract val noteDao: INoteDao

    abstract val rollDao: IRollDao

    abstract val rollVisibleDao: IRollVisibleDao

    abstract val rankDao: IRankDao

    abstract val alarmDao: IAlarmDao

    fun checkInsertIgnore(id: Long): Long? = id.takeIf { it != UNIQUE_ERROR_ID }

    companion object {
        @VisibleForTesting
        const val UNIQUE_ERROR_ID = -1L

        const val VERSION = 8

        operator fun get(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(*Migrate.sequence)
                    .build()
        }
    }

}