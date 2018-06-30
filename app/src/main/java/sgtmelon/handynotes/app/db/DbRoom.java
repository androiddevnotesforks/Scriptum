package sgtmelon.handynotes.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import sgtmelon.handynotes.db.dao.DaoNote;
import sgtmelon.handynotes.db.dao.DaoRank;
import sgtmelon.handynotes.db.dao.DaoRoll;
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
