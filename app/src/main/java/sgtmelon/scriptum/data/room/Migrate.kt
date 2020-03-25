package sgtmelon.scriptum.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import java.util.UUID.randomUUID

/**
 * Class with objects for db migration
 */
@Suppress("KDocUnresolvedReference")
object Migrate {

    /**
     * Add defaultValues to room entities
     */
    private val FROM_6_TO_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
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
            val tempNoteTable = "tempNoteTable"
            execSQL("ALTER TABLE NOTE_TABLE RENAME TO $tempNoteTable")

            execSQL("""CREATE TABLE NOTE_TABLE (
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
                NT_STATUS INTEGER NOT NULL DEFAULT 0)""")

            execSQL("""INSERT INTO NOTE_TABLE (
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS) SELECT
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS FROM $tempNoteTable""")

            execSQL("DROP TABLE $tempNoteTable")

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
            val tempRollTable = "tempRollTable"
            execSQL("ALTER TABLE ROLL_TABLE RENAME TO $tempRollTable")

            execSQL("""CREATE TABLE ROLL_TABLE (
                    RL_ID INTEGER PRIMARY KEY DEFAULT 0,
                    RL_NOTE_ID INTEGER NOT NULL DEFAULT 0,
                    RL_POSITION INTEGER NOT NULL DEFAULT 0,
                    RL_CHECK INTEGER NOT NULL DEFAULT 0,
                    RL_TEXT TEXT NOT NULL DEFAULT '',
                    FOREIGN KEY (RL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)""")

            execSQL("DROP INDEX ROLL_TABLE_NOTE_ID_INDEX")
            execSQL("CREATE INDEX ROLL_TABLE_NOTE_ID_INDEX ON ROLL_TABLE(RL_NOTE_ID)")

            execSQL("""INSERT INTO ROLL_TABLE(
                RL_ID, RL_NOTE_ID, RL_POSITION, RL_CHECK, RL_TEXT) SELECT
                RL_ID, RL_NOTE_ID, RL_POSITION, RL_CHECK, RL_TEXT FROM $tempRollTable""")

            execSQL("DROP TABLE $tempRollTable")

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
            val tempRankTable = "tempRankTable"
            execSQL("ALTER TABLE RANK_TABLE RENAME TO $tempRankTable")

            execSQL("""CREATE TABLE RANK_TABLE (
                    RK_ID INTEGER PRIMARY KEY NOT NULL DEFAULT 0,
                    RK_NOTE_ID TEXT NOT NULL DEFAULT '${StringConverter.NONE}',
                    RK_POSITION INTEGER NOT NULL DEFAULT 0,
                    RK_NAME TEXT NOT NULL DEFAULT '',
                    RK_VISIBLE INTEGER NOT NULL DEFAULT 1)""")

            execSQL("DROP INDEX RANK_TABLE_NAME_INDEX")
            execSQL("CREATE UNIQUE INDEX RANK_TABLE_NAME_INDEX ON RANK_TABLE(RK_NAME)")

            execSQL("""INSERT INTO RANK_TABLE(
                RK_ID, RK_NOTE_ID, RK_POSITION, RK_NAME, RK_VISIBLE) SELECT
                RK_ID, RK_NOTE_ID, RK_POSITION, RK_NAME, RK_VISIBLE FROM $tempRankTable""")

            execSQL("DROP TABLE $tempRankTable")

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
            val tempAlarmTable = "tempAlarmTable"
            execSQL("ALTER TABLE ALARM_TABLE RENAME TO $tempAlarmTable")

            execSQL("""CREATE TABLE ALARM_TABLE (
                    AL_ID INTEGER NOT NULL PRIMARY KEY DEFAULT 0,
                    AL_NOTE_ID INTEGER NOT NULL DEFAULT 0,
                    AL_DATE TEXT NOT NULL DEFAULT '',
                    FOREIGN KEY (AL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)""")

            execSQL("DROP INDEX ALARM_TABLE_NOTE_ID_INDEX")
            execSQL("CREATE UNIQUE INDEX ALARM_TABLE_NOTE_ID_INDEX ON ALARM_TABLE(AL_NOTE_ID)")

            execSQL("""INSERT INTO ALARM_TABLE(
                AL_ID, AL_NOTE_ID, AL_DATE) SELECT
                AL_ID, AL_NOTE_ID, AL_DATE FROM $tempAlarmTable""")

            execSQL("DROP TABLE $tempAlarmTable")
        }
    }

    /**
     * Change entity indices names and unique
     */
    private val FROM_5_TO_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Roll table:
             *
             * Expected:
             * TableInfo{name='ROLL_TABLE', columns={
             * RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'},
             * RL_NOTE_ID=Column{name='RL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='RL_NOTE_ID_INDEX', unique=false, columns=[RL_NOTE_ID]}]}
             *
             * Found:
             * TableInfo{name='ROLL_TABLE', columns={
             * RL_POSITION=Column{name='RL_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_TEXT=Column{name='RL_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_ID=Column{name='RL_ID', type='INTEGER', affinity='3', notNull=false, primaryKeyPosition=1, defaultValue='null'},
             * RL_CHECK=Column{name='RL_CHECK', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RL_NOTE_ID=Column{name='RL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[RL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='index_ROLL_TABLE_RL_NOTE_ID', unique=false, columns=[RL_NOTE_ID]}]}
             */
            execSQL("DROP INDEX index_ROLL_TABLE_RL_NOTE_ID")
            execSQL("CREATE INDEX ROLL_TABLE_NOTE_ID_INDEX ON ROLL_TABLE(RL_NOTE_ID)")

            /**
             * Rank table:
             *
             * Expected:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'}},
             * foreignKeys=[],
             * indices=[Index{name='RANK_TABLE_NAME_INDEX', unique=true, columns=[RK_NAME]}]}
             *
             * Found:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}},
             * foreignKeys=[],
             * indices=[Index{name='index_RANK_TABLE_RK_NAME', unique=true, columns=[RK_NAME]}]}
             */
            execSQL("DROP INDEX index_RANK_TABLE_RK_NAME")
            execSQL("CREATE UNIQUE INDEX RANK_TABLE_NAME_INDEX ON RANK_TABLE(RK_NAME)")

            /**
             * Alarm table:
             *
             * Expected:
             * TableInfo{name='ALARM_TABLE', columns={
             * AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
             * AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[AL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='ALARM_TABLE_NOTE_ID_INDEX', unique=true, columns=[AL_NOTE_ID]}]}
             *
             * Found:
             * TableInfo{name='ALARM_TABLE', columns={
             * AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1, defaultValue='null'},
             * AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0, defaultValue='null'},
             * AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0, defaultValue='null'}},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[AL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='index_ALARM_TABLE_AL_NOTE_ID', unique=false, columns=[AL_NOTE_ID]}]}
             */
            execSQL("DROP INDEX index_ALARM_TABLE_AL_NOTE_ID")
            execSQL("CREATE UNIQUE INDEX ALARM_TABLE_NOTE_ID_INDEX ON ALARM_TABLE(AL_NOTE_ID)")
        }
    }

    private val FROM_4_TO_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Note table:
             *
             * Expected:
             * TableInfo{name='NOTE_TABLE', columns={
             * NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0}},
             * NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             *
             * NT_RANK_ID=Column{name='NT_RANK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_RANK_PS=Column{name='NT_RANK_PS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             *
             * NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             *
             * Found:
             * TableInfo{name='NOTE_TABLE', columns={
             * NT_ID=Column{name='NT_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * NT_CREATE=Column{name='NT_CREATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_CHANGE=Column{name='NT_CHANGE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0}},
             * NT_NAME=Column{name='NT_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_TEXT=Column{name='NT_TEXT', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_COLOR=Column{name='NT_COLOR', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_TYPE=Column{name='NT_TYPE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             *
             * NT_RANK_ID=Column{name='NT_RANK_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * NT_RANK_PS=Column{name='NT_RANK_PS', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             *
             * NT_BIN=Column{name='NT_BIN', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * NT_STATUS=Column{name='NT_STATUS', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[], indices=[]}
             */

            /**
             * Remove multiply [NoteEntity.id] from [RankEntity.noteId].
             *
             * Because from this version note can can be connected only to one rank.
             * One note - one rank.
             */
            query("""SELECT RK_ID, RK_NOTE_ID FROM RANK_TABLE 
                WHERE RK_NOTE_ID LIKE '%,%' ORDER BY RK_POSITION ASC""").apply {
                if (moveToFirst()) {
                    /**
                     * This set save already used id's.
                     */
                    val noteIdUsedSet: MutableSet<Long> = mutableSetOf()

                    do {
                        val id = getString(getColumnIndex("RK_ID"))
                        val noteId = getString(getColumnIndex("RK_NOTE_ID"))

                        val noteIdList: MutableList<Long> = ArrayList(noteId.split(",".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .map { it.toLong() })

                        /**
                         * Remove already used [NoteEntity.id].
                         */
                        noteIdUsedSet.apply {
                            forEach { if (noteIdList.contains(it)) noteIdList.remove(it) }

                            /**
                             * Add not deleted id's for next forEach run.
                             */
                            addAll(noteIdList)
                        }

                        execSQL("""UPDATE RANK_TABLE 
                                SET RK_NOTE_ID = '${noteIdList.joinToString()}' 
                                WHERE RK_ID = $id""")
                    } while (moveToNext())
                }
            }.close()

            /**
             * Update [NoteEntity.rankId]/[NoteEntity.rankPs] from NONE value to -1
             */
            execSQL("UPDATE NOTE_TABLE SET NT_RANK_ID = '-1' WHERE NT_RANK_ID = 'NONE'")
            execSQL("UPDATE NOTE_TABLE SET NT_RANK_PS = '-1' WHERE NT_RANK_PS = 'NONE'")

            /**
             * Remove multiply [RankEntity.id]/[RankEntity.position]
             * from [NoteEntity.rankId]/[NoteEntity.rankPs]
             */
            query("""SELECT NT_ID, NT_RANK_ID, NT_RANK_PS FROM NOTE_TABLE
                WHERE NT_RANK_ID LIKE '%,%' OR NT_RANK_PS LIKE '%,%'""").apply {
                if (moveToFirst()) {
                    do {
                        val id = getString(getColumnIndex("NT_ID"))
                        val rankId = getString(getColumnIndex("NT_RANK_ID"))
                        val rankPs = getString(getColumnIndex("NT_RANK_PS"))

                        val newRankId: Long = rankId.split(",".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .map { it.toLong() }
                                .first()

                        val newRankPs: Int = rankPs.split(",".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .map { it.toInt() }
                                .first()

                        execSQL("""UPDATE NOTE_TABLE 
                            SET NT_RANK_ID = $newRankId, NT_RANK_PS = $newRankPs
                            WHERE NT_ID = $id""")
                    } while (moveToNext())
                }
            }.close()

            /**
             * Change column [NoteEntity.rankId] and [NoteEntity.rankPs] type
             */
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
                NT_RANK_PS INTEGER NOT NULL,
                NT_RANK_ID INTEGER NOT NULL,
                NT_BIN INTEGER NOT NULL,
                NT_STATUS INTEGER NOT NULL)""")

            execSQL("""INSERT INTO NOTE_TABLE (
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS) SELECT
                NT_ID, NT_CREATE, NT_CHANGE, NT_NAME, NT_TEXT, NT_COLOR, NT_TYPE,
                NT_RANK_PS, NT_RANK_ID, NT_BIN, NT_STATUS FROM $tempNoteTable""")

            execSQL("DROP TABLE $tempNoteTable")
        }
    }

    private val FROM_3_TO_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Rank table:
             *
             * Expected:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1}},
             * RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[],
             * indices=[Index{name='index_RANK_TABLE_RK_NAME', unique=true, columns=[RK_NAME]}]}
             *
             * Found:
             * TableInfo{name='RANK_TABLE', columns={
             * RK_ID=Column{name='RK_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * RK_NOTE_ID=Column{name='RK_NOTE_ID', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_POSITION=Column{name='RK_POSITION', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * RK_NAME=Column{name='RK_NAME', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * RK_VISIBLE=Column{name='RK_VISIBLE', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[],
             * indices=[]}
             */
            execSQL("CREATE UNIQUE INDEX index_RANK_TABLE_RK_NAME ON RANK_TABLE(RK_NAME)")
        }
    }

    private val FROM_2_TO_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Alarm table:
             *
             * Expected:
             * TableInfo{name='ALARM_TABLE', columns={
             * AL_ID=Column{name='AL_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=1},
             * AL_NOTE_ID=Column{name='AL_NOTE_ID', type='INTEGER', affinity='3', notNull=true, primaryKeyPosition=0}},
             * AL_DATE=Column{name='AL_DATE', type='TEXT', affinity='2', notNull=true, primaryKeyPosition=0},
             * foreignKeys=[ForeignKey{referenceTable='NOTE_TABLE', onDelete='CASCADE', onUpdate='CASCADE', columnNames=[AL_NOTE_ID], referenceColumnNames=[NT_ID]}],
             * indices=[Index{name='index_ALARM_TABLE_AL_NOTE_ID', unique=false, columns=[AL_NOTE_ID]}]}
             */
            execSQL("""CREATE TABLE ALARM_TABLE (
                    AL_ID INTEGER NOT NULL PRIMARY KEY,
                    AL_NOTE_ID INTEGER NOT NULL,
                    AL_DATE TEXT NOT NULL,
                    FOREIGN KEY (AL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)""")

            execSQL("CREATE INDEX index_ALARM_TABLE_AL_NOTE_ID ON ALARM_TABLE(AL_NOTE_ID)")
        }
    }

    private val FROM_1_TO_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Note table:
             *
             * Expected:
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
             * Expected:
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
             * Expected:
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

    val sequence = arrayOf(
            FROM_1_TO_2, FROM_2_TO_3,
            FROM_3_TO_4, FROM_4_TO_5,
            FROM_5_TO_6, FROM_6_TO_7
    )

}