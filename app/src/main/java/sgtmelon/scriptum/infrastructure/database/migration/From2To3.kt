package sgtmelon.scriptum.infrastructure.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity

/**
 * Add [AlarmEntity].
 */
@Suppress("KDocUnresolvedReference")
object From2To3 {

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
    val value = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            execSQL(
                """CREATE TABLE ALARM_TABLE (
                    AL_ID INTEGER NOT NULL PRIMARY KEY,
                    AL_NOTE_ID INTEGER NOT NULL,
                    AL_DATE TEXT NOT NULL,
                    FOREIGN KEY (AL_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                    ON UPDATE CASCADE ON DELETE CASCADE)"""
            )

            execSQL("CREATE INDEX index_ALARM_TABLE_AL_NOTE_ID ON ALARM_TABLE(AL_NOTE_ID)")
        }
    }
}