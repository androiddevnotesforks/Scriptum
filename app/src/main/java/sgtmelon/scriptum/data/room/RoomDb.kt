package sgtmelon.scriptum.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.data.room.backup.IBackupParser
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

    companion object {
        const val UNIQUE_ERROR_ID = -1L

        /**
         * Caution!
         *
         * After change room version you must provide backport and update [IBackupParser.collect].
         */
        const val VERSION = 8

        operator fun get(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(*RoomMigrate.sequence)
                    .build()
        }
    }

}