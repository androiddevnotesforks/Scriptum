package sgtmelon.scriptum.infrastructure.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object From3To4 {

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
    val value = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            execSQL("CREATE UNIQUE INDEX index_RANK_TABLE_RK_NAME ON RANK_TABLE(RK_NAME)")
        }
    }
}