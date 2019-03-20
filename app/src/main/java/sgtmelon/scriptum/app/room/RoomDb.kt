package sgtmelon.scriptum.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.dao.NoteDao
import sgtmelon.scriptum.app.room.dao.RankDao
import sgtmelon.scriptum.app.room.dao.RollDao

/**
 * Класс для общения с базой данных
 */
@Database(entities = [NoteItem::class, RollItem::class, RankItem::class], version = 1)
abstract class RoomDb : RoomDatabase() {

    abstract fun daoNote(): NoteDao

    abstract fun daoRoll(): RollDao

    abstract fun daoRank(): RankDao

    companion object {
        fun getInstance(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                    .build()
        }
    }

}