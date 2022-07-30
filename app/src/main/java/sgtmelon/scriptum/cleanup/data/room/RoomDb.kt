package sgtmelon.scriptum.cleanup.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.dao.IAlarmDao
import sgtmelon.scriptum.cleanup.data.room.dao.INoteDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRankDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRollDao
import sgtmelon.scriptum.cleanup.data.room.dao.IRollVisibleDao
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

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
        /**
         * Dao return this id when insert error happen.
         */
        const val UNIQUE_ERROR_ID = -1L

        /**
         * Overflow error may happen, when dao function contains "IN (:someList)" and "someList"
         * size bigger than 999 value.
         */
        const val OVERFLOW_COUNT = 900

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