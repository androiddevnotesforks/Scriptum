package sgtmelon.scriptum.infrastructure.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
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
import androidx.room.Database as DatabaseInit

/**
 * Class for work with [RoomDatabase] and providing different dao.
 */
@DatabaseInit(
    entities = [
        NoteEntity::class,
        RollEntity::class,
        RollVisibleEntity::class,
        RankEntity::class,
        AlarmEntity::class
    ], version = Database.VERSION
)
abstract class Database : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val rollDao: RollDao

    abstract val rollVisibleDao: RollVisibleDao

    abstract val rankDao: RankDao

    abstract val alarmDao: AlarmDao

    companion object {

        /**
         * !!! CAUTION !!!
         *
         * After change room version you must provide backport and update [BackupParser.collect].
         */
        const val VERSION = 8

        operator fun get(context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, BuildConfig.DB_NAME)
                .addMigrations(*DatabaseMigration.sequence)
                .build()
        }
    }
}