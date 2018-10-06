package sgtmelon.scriptum.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import sgtmelon.scriptum.app.database.dao.NoteDao;
import sgtmelon.scriptum.app.database.dao.RankDao;
import sgtmelon.scriptum.app.database.dao.RollDao;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.annot.DbAnn;

@Database(entities = {NoteItem.class, RollItem.class, RankItem.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {

    public static RoomDb provideDb(Context context) {
        return Room.databaseBuilder(context, RoomDb.class, DbAnn.name)
                .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                .build();
    }

    public abstract NoteDao daoNote();

    public abstract RollDao daoRoll();

    public abstract RankDao daoRank();

}
