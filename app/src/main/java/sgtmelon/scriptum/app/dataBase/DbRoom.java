package sgtmelon.scriptum.app.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import sgtmelon.scriptum.app.dataBase.dao.DaoNote;
import sgtmelon.scriptum.app.dataBase.dao.DaoRank;
import sgtmelon.scriptum.app.dataBase.dao.DaoRoll;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRank;
import sgtmelon.scriptum.app.model.item.ItemRoll;

@Database(entities = {ItemNote.class, ItemRoll.class, ItemRank.class}, version = 1)
public abstract class DbRoom extends RoomDatabase {

    public abstract DaoNote daoNote();

    public abstract DaoRoll daoRoll();

    public abstract DaoRank daoRank();

    public static DbRoom provideDb(Context context){
        return Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                    .allowMainThreadQueries()
                    .build();
    }

}
