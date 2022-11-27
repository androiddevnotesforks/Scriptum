package sgtmelon.scriptum.infrastructure.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

/**
 * Add [RollVisibleEntity].
 */
object From7To8 {

    val value = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) = with(database) {
            execSQL("DROP TABLE IF EXISTS ROLL_VISIBLE_TABLE")

            execSQL(
                """CREATE TABLE ROLL_VISIBLE_TABLE (
                RL_VS_ID INTEGER PRIMARY KEY NOT NULL DEFAULT 0,
                RL_VS_NOTE_ID INTEGER NOT NULL DEFAULT 0,
                RL_VS_VALUE INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY (RL_VS_NOTE_ID) REFERENCES NOTE_TABLE(NT_ID)
                ON UPDATE CASCADE ON DELETE CASCADE)"""
            )

            execSQL(
                """CREATE UNIQUE INDEX ROLL_VISIBLE_TABLE_NOTE_ID_INDEX
                ON ROLL_VISIBLE_TABLE(RL_VS_NOTE_ID)"""
            )

            query("SELECT NT_ID, NT_TYPE FROM NOTE_TABLE WHERE NT_TYPE = '1'").apply {
                if (moveToFirst()) {
                    do {
                        val idIndex = getColumnIndexOrNull(name = "NT_ID") ?: continue
                        val id = getString(idIndex)

                        execSQL("INSERT INTO ROLL_VISIBLE_TABLE (RL_VS_NOTE_ID) VALUES ($id)")
                    } while (moveToNext())
                }
            }.close()
        }
    }
}