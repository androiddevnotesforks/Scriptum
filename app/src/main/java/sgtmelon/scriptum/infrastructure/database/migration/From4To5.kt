package sgtmelon.scriptum.infrastructure.database.migration

import android.database.Cursor
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity

/**
 * Remove multi Note<->Rank connection.
 */
object From4To5 {

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
    val value = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            /**
             * Remove multiply [NoteEntity.id] from [RankEntity.noteId].
             *
             * Because from this version note can can be connected only to one rank.
             * One note - one rank.
             */
            val multiNoteCursor = query(
                """SELECT RK_ID, RK_NOTE_ID FROM RANK_TABLE 
                WHERE RK_NOTE_ID LIKE '%,%' ORDER BY RK_POSITION ASC"""
            )
            removeMultiNoteFromRank(multiNoteCursor)
            multiNoteCursor.close()

            /** Update [NoteEntity.rankId]/[NoteEntity.rankPs] from NONE value to -1 */
            execSQL("UPDATE NOTE_TABLE SET NT_RANK_ID = '-1' WHERE NT_RANK_ID = 'NONE'")
            execSQL("UPDATE NOTE_TABLE SET NT_RANK_PS = '-1' WHERE NT_RANK_PS = 'NONE'")

            /**
             * Remove multiply [RankEntity.id]/[RankEntity.position]
             * from [NoteEntity.rankId]/[NoteEntity.rankPs]
             */
            val multiRankCursor = query(
                """SELECT NT_ID, NT_RANK_ID, NT_RANK_PS FROM NOTE_TABLE
                WHERE NT_RANK_ID LIKE '%,%' OR NT_RANK_PS LIKE '%,%'"""
            )
            removeMultiRankFromNote(multiRankCursor)
            multiRankCursor.close()

            /** Change column [NoteEntity.rankId] and [NoteEntity.rankPs] type */
            val tempNoteTable = "tempNoteTable"
            execSQL("ALTER TABLE NOTE_TABLE RENAME TO $tempNoteTable")

            execSQL(
                """CREATE TABLE NOTE_TABLE (
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
                NT_STATUS INTEGER NOT NULL)"""
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
    }

    private fun SupportSQLiteDatabase.removeMultiNoteFromRank(cursor: Cursor) {
        if (!cursor.moveToFirst()) return

        /** This set save already used id's. */
        val noteIdUsedSet: MutableSet<Long> = mutableSetOf()

        do {
            val idIndex = cursor.getColumnIndexOrNull(name = "RK_ID") ?: continue
            val noteIdIndex = cursor.getColumnIndexOrNull(name = "RK_NOTE_ID") ?: continue

            val id = cursor.getString(idIndex)
            val noteId = cursor.getString(noteIdIndex)

            val noteIdList: MutableList<Long> = ArrayList(
                noteId.clearSplit().mapNotNull { it.toLongOrNull() }
            )

            /** Remove already used [NoteEntity.id]. */
            for (it in noteIdUsedSet) {
                if (noteIdList.contains(it)) noteIdList.remove(it)
            }

            /** Add not deleted id's for next FOR run. */
            noteIdUsedSet.addAll(noteIdList)

            execSQL(
                """UPDATE RANK_TABLE 
                            SET RK_NOTE_ID = '${noteIdList.joinToString()}' 
                            WHERE RK_ID = $id"""
            )
        } while (cursor.moveToNext())
    }

    private fun SupportSQLiteDatabase.removeMultiRankFromNote(cursor: Cursor) {
        if (!cursor.moveToFirst()) return

        do {
            val idIndex = cursor.getColumnIndexOrNull(name = "NT_ID") ?: continue
            val rankIdIndex = cursor.getColumnIndexOrNull(name = "NT_RANK_ID") ?: continue
            val rankPsIndex = cursor.getColumnIndexOrNull(name = "NT_RANK_PS") ?: continue

            val id = cursor.getString(idIndex)
            val rankId = cursor.getString(rankIdIndex)
            val rankPs = cursor.getString(rankPsIndex)

            val newRankId: Long = rankId.clearSplit().firstOrNull()?.toLongOrNull() ?: continue
            val newRankPs: Int = rankPs.clearSplit().firstOrNull()?.toIntOrNull() ?: continue

            execSQL(
                """UPDATE NOTE_TABLE 
                        SET NT_RANK_ID = $newRankId, NT_RANK_PS = $newRankPs
                        WHERE NT_ID = $id"""
            )
        } while (cursor.moveToNext())
    }
}