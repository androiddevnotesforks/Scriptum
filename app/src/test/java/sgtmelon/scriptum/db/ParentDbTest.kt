package sgtmelon.scriptum.db

import androidx.annotation.CallSuper
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.database.dao.NoteDao
import sgtmelon.scriptum.app.database.dao.RankDao
import sgtmelon.scriptum.app.database.dao.RollDao


/**
 * Родительский класс включающий в себе объявление часто используемых переменных при общении с [RoomDb]
 */
@RunWith(AndroidJUnit4::class)
abstract class ParentDbTest {

    private var db: RoomDb? = null

    protected lateinit var noteDao: NoteDao
    protected lateinit var rankDao: RankDao
    protected lateinit var rollDao: RollDao

    @Before
    @CallSuper
    fun setUp() {
        val context = getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, RoomDb::class.java).build()

        noteDao = db!!.daoNote()
        rankDao = db!!.daoRank()
        rollDao = db!!.daoRoll()
    }

    @After
    @CallSuper
    fun tearDown() {
        db!!.close()
    }

}
