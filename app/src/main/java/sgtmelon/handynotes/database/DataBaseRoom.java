package sgtmelon.handynotes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sgtmelon.handynotes.database.dao.DaoNote;
import sgtmelon.handynotes.database.dao.DaoRank;
import sgtmelon.handynotes.database.dao.DaoRoll;
import sgtmelon.handynotes.model.item.ItemNote;

@Database(entities = {ItemNote.class}, version = 1)
public abstract class DataBaseRoom extends RoomDatabase {

    public abstract DaoNote daoNote();

    public abstract DaoRoll daoRoll();

    public abstract DaoRank daoRank();

}
