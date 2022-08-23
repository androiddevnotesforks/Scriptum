package sgtmelon.scriptum.cleanup.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao

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

    abstract val noteDao: NoteDao

    abstract val rollDao: RollDao

    abstract val rollVisibleDao: RollVisibleDao

    abstract val rankDao: RankDao

    abstract val alarmDao: AlarmDao

    companion object {

        /**
         * !!! CAUTION !!!
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