package sgtmelon.scriptum.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import sgtmelon.scriptum.BuildConfig;
import sgtmelon.scriptum.app.database.dao.NoteDao;
import sgtmelon.scriptum.app.database.dao.RankDao;
import sgtmelon.scriptum.app.database.dao.RollDao;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.model.item.RollItem;

/**
 * Класс для общения с базой данных
 */
@Database(entities = {NoteItem.class, RollItem.class, RankItem.class}, version = 2)
public abstract class RoomDb extends RoomDatabase {

    public static RoomDb provideDb(Context context) {
        return Room.databaseBuilder(context, RoomDb.class, BuildConfig.DB_NAME)
                .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                .build();
    }

    public abstract NoteDao daoNote();

    public abstract RollDao daoRoll();

    public abstract RankDao daoRank();

}
