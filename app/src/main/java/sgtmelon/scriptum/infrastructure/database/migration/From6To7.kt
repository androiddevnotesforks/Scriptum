package sgtmelon.scriptum.infrastructure.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.infrastructure.converter.types.LongListConverter

/**
 * Add defaultValues to room entities.
 */
@Suppress("KDocUnresolvedReference")
object From6To7 {

    val value = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            migrateNote()
            migrateRoll()
            migrateRank()
            migrateAlarm()
        }
    }

    /**
     * Note table:
     *
     * Expected:
     * TableInfo{name='NOTE_TABLE', columns={
     *      NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='0'},
     *      NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      NT_RANK_PS=Column{name='NT_RANK_PS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='-1'},
     *      NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      NT_RANK_ID=Column{name='NT_RANK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='-1'},
     *      NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''}
     * }, foreignKeys=[], indices=[]}
     *
     * Found:
     * TableInfo{name='NOTE_TABLE', columns={
     *      NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
     *      NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_RANK_PS=Column{name='NT_RANK_PS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_RANK_ID=Column{name='NT_RANK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'}
     * }, foreignKeys=[], indices=[]}
     */
    private fun SupportSQLiteDatabase.migrateNote() {
        val tempNoteTable = "tempNoteTable"
        execSQL("ALTER TABLE NOTE_TABLE RENAME TO $tempNoteTable")

        execSQL(
            """CREATE TABLE NOTE_TABLE (
                NT_ID INTEGER PRIMARY KEY NOT NULL DEFAULT 0,
                NT_CREATE TEXT NOT NULL DEFAULT '',
                NT_CHANGE TEXT NOT NULL DEFAULT '',
                NT_NAME TEXT NOT NULL DEFAULT '',
                NT_TEXT TEXT NOT NULL DEFAULT '',
                NT_COLOR INTEGER NOT NULL DEFAULT 0,
                NT_TYPE INTEGER NOT NULL DEFAULT 0,
                NT_RANK_PS INTEGER NOT NULL DEFAULT -1,
                NT_RANK_ID INTEGER NOT NULL DEFAULT -1,
                NT_BIN INTEGER NOT NULL DEFAULT 0,
                NT_STATUS INTEGER NOT NULL DEFAULT 0)"""
        )

        execSQL(
            """INSERT INTO NOTE_TABLE (
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS) SELECT
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS FROM $tempNoteTable"""
        )

        execSQL("DROP TABLE $tempNoteTable")


    }

    /**
     * Roll table:
     *
     * Expected:
     * TableInfo{name='ROLL_TABLE', columns={
     *      RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='0'},
     *      RL_NOTE_ID=Column{name='RL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'}
     * },
     * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_NOTE_ID], referenceColumnNames=[NT_ID]}],
     * indices=[Index{name='ROLL_TABLE_NOTE_ID_INDEX', unique=false, columns=[RL_NOTE_ID]}]}
     *
     * Found:
     * TableInfo{name='ROLL_TABLE', columns={
     *      RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'},
     *      RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RL_NOTE_ID=Column{name='RL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}
     * },
     * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_NOTE_ID], referenceColumnNames=[NT_ID]}],
     * indices=[Index{name='ROLL_TABLE_NOTE_ID_INDEX', unique=false, columns=[RL_NOTE_ID]}]}
     */
    private fun SupportSQLiteDatabase.migrateRoll() {
        val tempRollTable = "tempRollTable"
        execSQL("ALTER TABLE ROLL_TABLE RENAME TO $tempRollTable")

        execSQL(
            """CREATE TABLE ROLL_TABLE (
                    RL_ID INTEGER PRIMARY KEY DEFAULT 0,
                    RL_NOTE_ID INTEGER NOT NULL DEFAULT 0,
                    RL_POSITION INTEGER NOT NULL DEFAULT 0,
                    RL_CHECK INTEGER NOT NULL DEFAULT 0,
                    RL_TEXT TEXT NOT NULL DEFAULT '',
                    FOREIGN KEY (RL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)"""
        )

        execSQL("DROP INDEX ROLL_TABLE_NOTE_ID_INDEX")
        execSQL("CREATE INDEX ROLL_TABLE_NOTE_ID_INDEX ON ROLL_TABLE(RL_NOTE_ID)")

        execSQL(
            """INSERT INTO ROLL_TABLE(
                RL_ID, RL_NOTE_ID, RL_POSITION, RL_CHECK, RL_TEXT) SELECT
                RL_ID, RL_NOTE_ID, RL_POSITION, RL_CHECK, RL_TEXT FROM $tempRollTable"""
        )

        execSQL("DROP TABLE $tempRollTable")

    }

    /**
     * Rank table:
     *
     * Expected:
     * TableInfo{name='RANK_TABLE', columns={
     *      RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='1'},
     *      RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'},
     *      RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''NONE''},
     *      RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='0'}
     * }, foreignKeys=[], indices=[Index{name='RANK_TABLE_NAME_INDEX', unique=true, columns=[RK_NAME]}]}
     *
     * Found:
     * TableInfo{name='RANK_TABLE', columns={
     *      RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
     *      RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'
     * }}, foreignKeys=[], indices=[Index{name='RANK_TABLE_NAME_INDEX', unique=true, columns=[RK_NAME]}]}
     */
    private fun SupportSQLiteDatabase.migrateRank() {
        val tempRankTable = "tempRankTable"
        execSQL("ALTER TABLE RANK_TABLE RENAME TO $tempRankTable")

        execSQL(
            """CREATE TABLE RANK_TABLE (
                    RK_ID INTEGER PRIMARY KEY NOT NULL DEFAULT 0,
                    RK_NOTE_ID TEXT NOT NULL DEFAULT '${LongListConverter.EMPTY}',
                    RK_POSITION INTEGER NOT NULL DEFAULT 0,
                    RK_NAME TEXT NOT NULL DEFAULT '',
                    RK_VISIBLE INTEGER NOT NULL DEFAULT 1)"""
        )

        execSQL("DROP INDEX RANK_TABLE_NAME_INDEX")
        execSQL("CREATE UNIQUE INDEX RANK_TABLE_NAME_INDEX ON RANK_TABLE(RK_NAME)")

        execSQL(
            """INSERT INTO RANK_TABLE(
                RK_ID, RK_NOTE_ID, RK_POSITION, RK_NAME, RK_VISIBLE) SELECT
                RK_ID, RK_NOTE_ID, RK_POSITION, RK_NAME, RK_VISIBLE FROM $tempRankTable"""
        )

        execSQL("DROP TABLE $tempRankTable")

    }

    /**
     * Alarm table:
     *
     * Expected:
     * TableInfo{name='ALARM_TABLE', columns={
     *      AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='0'},
     *      AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue=''''},
     *      AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='0'}
     * },
     * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[AL_NOTE_ID], referenceColumnNames=[NT_ID]}],
     * indices=[Index{name='ALARM_TABLE_NOTE_ID_INDEX', unique=true, columns=[AL_NOTE_ID]}]}
     *
     * Found:
     * TableInfo{name='ALARM_TABLE', columns={
     *      AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
     *      AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
     *      AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}
     * },
     * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[AL_NOTE_ID], referenceColumnNames=[NT_ID]}],
     * indices=[Index{name='ALARM_TABLE_NOTE_ID_INDEX', unique=true, columns=[AL_NOTE_ID]}]}
     */
    private fun SupportSQLiteDatabase.migrateAlarm() {
        val tempAlarmTable = "tempAlarmTable"
        execSQL("ALTER TABLE ALARM_TABLE RENAME TO $tempAlarmTable")

        execSQL(
            """CREATE TABLE ALARM_TABLE (
                    AL_ID INTEGER NOT NULL PRIMARY KEY DEFAULT 0,
                    AL_NOTE_ID INTEGER NOT NULL DEFAULT 0,
                    AL_DATE TEXT NOT NULL DEFAULT '',
                    FOREIGN KEY (AL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)"""
        )

        execSQL("DROP INDEX ALARM_TABLE_NOTE_ID_INDEX")
        execSQL("CREATE UNIQUE INDEX ALARM_TABLE_NOTE_ID_INDEX ON ALARM_TABLE(AL_NOTE_ID)")

        execSQL(
            """INSERT INTO ALARM_TABLE(
                AL_ID, AL_NOTE_ID, AL_DATE) SELECT
                AL_ID, AL_NOTE_ID, AL_DATE FROM $tempAlarmTable"""
        )

        execSQL("DROP TABLE $tempAlarmTable")
    }
}