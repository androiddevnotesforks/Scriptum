package sgtmelon.handynotes.app.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import sgtmelon.handynotes.app.data.dao.DaoNote;
import sgtmelon.handynotes.app.data.dao.DaoRank;
import sgtmelon.handynotes.app.data.dao.DaoRoll;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.item.ItemRoll;

@Database(entities = {ItemNote.class, ItemRoll.class, ItemRank.class}, version = 1)
public abstract class DataRoom extends RoomDatabase {

    public abstract DaoNote daoNote();

    public abstract DaoRoll daoRoll();

    public abstract DaoRank daoRank();

    public static DataRoom provideDb(Context context){
        return Room.databaseBuilder(context, DataRoom.class, "HandyNotes")
                    .allowMainThreadQueries()
                    .build();
    }

}
