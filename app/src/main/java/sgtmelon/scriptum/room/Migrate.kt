package sgtmelon.scriptum.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID.randomUUID

/**
 * Класс с объектами для миграции базы данных
 *
 * @author SerjantArbuz
 */
@Suppress("KDocUnresolvedReference")
object Migrate {

    val FROM_1_TO_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Note table:
             *
             * Need:
             * TableInfo{name='NOTE_TABLE', columns={
             * NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0}},
             * NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_RANK_PS=Column{name='NT_RANK_PS', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_RANK_ID=Column{name='NT_RANK_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             *
             * Found:
             * TableInfo{name='NOTE_TABLE', columns={
             * NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0}},
             * NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_RANK_PS=Column{name='NT_RANK_PS', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * NT_RANK_ID=Column{name='NT_RANK_ID', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             */
            execSQL("UPDATE NOTE_TABLE SET NT_CREATE = '2019-03-04 19:56:00' WHERE NT_CREATE IS NULL")
            execSQL("UPDATE NOTE_TABLE SET NT_CHANGE = '2019-03-04 19:56:00' WHERE NT_CHANGE IS NULL")

            execSQL("UPDATE NOTE_TABLE SET NT_NAME = '' WHERE NT_NAME IS NULL")
            execSQL("UPDATE NOTE_TABLE SET NT_TEXT = 'Empty note text' WHERE NT_TEXT IS NULL")

            execSQL("UPDATE NOTE_TABLE SET NT_RANK_PS = 'NONE' WHERE NT_RANK_PS IS NULL")
            execSQL("UPDATE NOTE_TABLE SET NT_RANK_ID = 'NONE' WHERE NT_RANK_ID IS NULL")

            val tempNoteTable = "tempNoteTable"
            execSQL("ALTER TABLE NOTE_TABLE RENAME TO $tempNoteTable")

            execSQL("""CREATE TABLE NOTE_TABLE (
                NT_ID INTEGER PRIMARY KEY NOT NULL,
                NT_CREATE TEXT NOT NULL,
                NT_CHANGE TEXT NOT NULL,
                NT_NAME TEXT NOT NULL,
                NT_TEXT TEXT NOT NULL,
                NT_COLOR INTEGER NOT NULL,
                NT_TYPE INTEGER NOT NULL,
                NT_RANK_PS TEXT NOT NULL,
                NT_RANK_ID TEXT NOT NULL,
                NT_BIN INTEGER NOT NULL,
                NT_STATUS INTEGER NOT NULL)""")

            execSQL("""INSERT INTO NOTE_TABLE (
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS) SELECT
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS FROM $tempNoteTable""")

            execSQL("DROP TABLE $tempNoteTable")

            /**
             * Roll table:
             *
             * Need:
             * TableInfo{name='ROLL_TABLE', columns={
             * RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1},
             * RL_NOTE_ID=Column{name='RL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='index_ROLL_TABLE_RL_NOTE_ID', unique=false, columns=[RL_NOTE_ID]}]}
             *
             * Found:
             * TableInfo{name='ROLL_TABLE', columns={
             * RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * RL_ID_NOTE=Column{name='RL_ID_NOTE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_ID_NOTE], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='index_ROLL_TABLE_RL_ID_NOTE', unique=false, columns=[RL_ID_NOTE]}]}
             */
            execSQL("UPDATE ROLL_TABLE SET RL_TEXT = 'Empty roll text' WHERE RL_TEXT IS NULL")

            val tempRollTable = "tempRollTable"
            execSQL("ALTER TABLE ROLL_TABLE RENAME TO $tempRollTable")

            execSQL("""CREATE TABLE ROLL_TABLE (
                    RL_ID INTEGER PRIMARY KEY,
                    RL_NOTE_ID INTEGER NOT NULL,
                    RL_POSITION INTEGER NOT NULL,
                    RL_CHECK INTEGER NOT NULL,
                    RL_TEXT TEXT NOT NULL,
                    FOREIGN KEY (RL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)""")

            execSQL("DROP INDEX index_ROLL_TABLE_RL_ID_NOTE")
            execSQL("CREATE INDEX index_ROLL_TABLE_RL_NOTE_ID ON ROLL_TABLE(RL_NOTE_ID)")

            execSQL("""INSERT INTO ROLL_TABLE(
                RL_ID, RL_NOTE_ID, RL_POSITION, RL_CHECK, RL_TEXT) SELECT
                RL_ID, RL_ID_NOTE, RL_POSITION, RL_CHECK, RL_TEXT FROM $tempRollTable""")

            execSQL("DROP TABLE $tempRollTable")

            /**
             * Rank table:
             *
             * Need:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1}},
             * RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             *
             * Found:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * RK_ID_NOTE=Column{name='RK_ID_NOTE', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=false, primaryKeyPosition=0},
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             */
            execSQL("UPDATE RANK_TABLE SET RK_NAME = '${randomUUID()}' WHERE RK_NAME IS NULL")
            execSQL("UPDATE RANK_TABLE SET RK_ID_NOTE = 'NONE' WHERE RK_ID_NOTE IS NULL")

            val tempRankTable = "tempRankTable"
            execSQL("ALTER TABLE RANK_TABLE RENAME TO $tempRankTable")

            execSQL("""CREATE TABLE RANK_TABLE (
                    RK_ID INTEGER PRIMARY KEY NOT NULL,
                    RK_NOTE_ID TEXT NOT NULL,
                    RK_POSITION INTEGER NOT NULL,
                    RK_NAME TEXT NOT NULL,
                    RK_VISIBLE INTEGER NOT NULL)""")

            execSQL("""INSERT INTO RANK_TABLE(
                RK_ID, RK_NOTE_ID, RK_POSITION, RK_NAME, RK_VISIBLE) SELECT
                RK_ID, RK_ID_NOTE, RK_POSITION, RK_NAME, RK_VISIBLE FROM $tempRankTable""")

            execSQL("DROP TABLE $tempRankTable")
        }
    }

    val FROM_2_TO_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            /**
             * Need:
             * TableInfo{name='ALARM_TABLE', columns={
             * AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             */
        }
    }

}