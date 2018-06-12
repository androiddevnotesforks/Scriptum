package sgtmelon.handynotes.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sgtmelon.handynotes.db.dao.DaoNote;
import sgtmelon.handynotes.db.dao.DaoRank;
import sgtmelon.handynotes.db.dao.DaoRoll;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.model.item.ItemRoll;

//TODO SingleTone

@Database(entities = {ItemNote.class, ItemRoll.class, ItemRank.class}, version = 1)
public abstract class DbRoom extends RoomDatabase {

    public abstract DaoNote daoNote();

    public abstract DaoRoll daoRoll();

    public abstract DaoRank daoRank();

}
