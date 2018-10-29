package sgtmelon.scriptum.db;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import androidx.annotation.CallSuper;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.database.dao.NoteDao;
import sgtmelon.scriptum.app.database.dao.RankDao;
import sgtmelon.scriptum.app.database.dao.RollDao;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


/**
 * Родительский класс включающий в себе объявление часто используемых переменных при общении с {@link RoomDb}
 */
@RunWith(AndroidJUnit4.class)
public abstract class ParentDbTest {

    private RoomDb db;

    protected NoteDao noteDao;
    protected RankDao rankDao;
    protected RollDao rollDao;

    @Before
    @CallSuper
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, RoomDb.class).build();

        noteDao = db.daoNote();
        rankDao = db.daoRank();
        rollDao = db.daoRoll();
    }

    @After
    @CallSuper
    public void tearDown() {
        db.close();
    }

}
