package sgtmelon.scriptum.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import sgtmelon.scriptum.app.database.dao.DaoNote;
import sgtmelon.scriptum.app.database.dao.DaoRank;
import sgtmelon.scriptum.app.database.dao.DaoRoll;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRank;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.office.annot.AnnDb;

@Database(entities = {ItemNote.class, ItemRoll.class, ItemRank.class}, version = 1)
public abstract class DbRoom extends RoomDatabase {

    public static DbRoom provideDb(Context context) {
        return Room.databaseBuilder(context, DbRoom.class, AnnDb.name)
                .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                .build();
    }

    public abstract DaoNote daoNote();

    public abstract DaoRoll daoRoll();

    public abstract DaoRank daoRank();

}
