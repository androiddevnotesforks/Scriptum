package sgtmelon.scriptum.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.data.room.dao.*
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.data.room.entity.RollEntity

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

    abstract val noteDao: INoteDao

    abstract val rollDao: IRollDao

    abstract val rollVisibleDao: IRollVisibleDao

    abstract val rankDao: IRankDao

    abstract val alarmDao: IAlarmDao

    companion object {
        const val VERSION = 7

        operator fun get(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(*Migrate.sequence)
                    .build()
        }
    }

}