package sgtmelon.scriptum.infrastructure.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Change entity indices names and unique.
 */
object From5To6 {

    val value = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            migrateRoll()
            migrateRank()
            migrateAlarm()
        }
    }

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
    private fun SupportSQLiteDatabase.migrateRoll() {
        execSQL("DROP INDEX index_ROLL_TABLE_RL_NOTE_ID")
        execSQL("CREATE INDEX ROLL_TABLE_NOTE_ID_INDEX ON ROLL_TABLE(RL_NOTE_ID)")
    }

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
    private fun SupportSQLiteDatabase.migrateRank() {
        execSQL("DROP INDEX index_RANK_TABLE_RK_NAME")
        execSQL("CREATE UNIQUE INDEX RANK_TABLE_NAME_INDEX ON RANK_TABLE(RK_NAME)")
    }

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
    private fun SupportSQLiteDatabase.migrateAlarm() {
        execSQL("DROP INDEX index_ALARM_TABLE_AL_NOTE_ID")
        execSQL("CREATE UNIQUE INDEX ALARM_TABLE_NOTE_ID_INDEX ON ALARM_TABLE(AL_NOTE_ID)")
    }
}