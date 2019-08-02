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
 * Класс для общения с базой данных
 *
 * @author SerjantArbuz
 */
@Database(entities = [
    NoteEntity::class,
    RollEntity::class,
    RankEntity::class,
    AlarmEntity::class
], version = 5)
abstract class RoomDb : RoomDatabase() {

    abstract val iNoteDao: INoteDao

    abstract val iRollDao: IRollDao

    abstract val iRankDao: IRankDao

    abstract val iAlarmDao: IAlarmDao

    companion object {
        fun getInstance(context: Context): RoomDb =
                Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                        .addMigrations(
                                Migrate.FROM_1_TO_2, Migrate.FROM_2_TO_3, Migrate.FROM_3_TO_4,
                                Migrate.FROM_4_TO_5
                        ).allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                        .build()
    }

}