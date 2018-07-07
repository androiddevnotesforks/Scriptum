package sgtmelon.handynotes.app.dataBase;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import sgtmelon.handynotes.app.dataBase.dao.DaoNote;
import sgtmelon.handynotes.app.dataBase.dao.DaoRank;
import sgtmelon.handynotes.app.dataBase.dao.DaoRoll;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.item.ItemRoll;

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
