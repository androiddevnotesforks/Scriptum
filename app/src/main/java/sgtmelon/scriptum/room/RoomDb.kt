package sgtmelon.scriptum.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.dao.NoteDao
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.room.dao.RollDao

/**
 * Класс для общения с базой данных
 *
 * @author SerjantArbuz
 */
@Database(entities = [NoteItem::class, RollItem::class, RankItem::class], version = 2)
abstract class RoomDb : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    abstract fun getRollDao(): RollDao

    abstract fun getRankDao(): RankDao

    companion object {
        fun getInstance(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(Migrate.FROM_1_TO_2)
                    .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                    .build()
        }
    }

}