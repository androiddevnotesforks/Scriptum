package sgtmelon.scriptum.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.room.dao.AlarmDao
import sgtmelon.scriptum.room.dao.NoteDao
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.room.dao.RollDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Класс для общения с базой данных
 *
 * @author SerjantArbuz
 */
@Database(entities = [
    NoteEntity::class,
    RollEntity::class,
    RankEntity::class,
    AlarmEntity::class
], version = 4)
abstract class RoomDb : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    abstract fun getRollDao(): RollDao

    abstract fun getRankDao(): RankDao

    abstract fun getAlarmDao(): AlarmDao

    companion object {
        fun getInstance(context: Context): RoomDb =
                Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                        .addMigrations(
                                Migrate.FROM_1_TO_2,
                                Migrate.FROM_2_TO_3,
                                Migrate.FROM_3_TO_4
                        ).allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                        .build()
    }

}