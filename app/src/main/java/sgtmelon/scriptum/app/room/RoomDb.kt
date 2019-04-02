package sgtmelon.scriptum.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.key.DbField
import sgtmelon.scriptum.app.model.key.DbField.Note
import sgtmelon.scriptum.app.model.key.DbField.Rank
import sgtmelon.scriptum.app.model.key.DbField.Roll
import sgtmelon.scriptum.app.room.dao.NoteDao
import sgtmelon.scriptum.app.room.dao.RankDao
import sgtmelon.scriptum.app.room.dao.RollDao

/**
 * Класс для общения с базой данных
 */
@Database(entities = [NoteItem::class, RollItem::class, RankItem::class], version = 2)
abstract class RoomDb : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    abstract fun getRollDao(): RollDao

    abstract fun getRankDao(): RankDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) = with(database) {
                execSQL("UPDATE ${Note.TABLE} SET ${Note.CREATE} = '' WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.RANK_PS} = '${DbField.Value.NONE}' WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.NAME} = '' WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.RANK_ID} = '${DbField.Value.NONE}' WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.TEXT} = '' WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.TYPE} = 0 WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.TYPE} = 0 WHERE ${Note.CREATE} IS NULL")
                execSQL("UPDATE ${Note.TABLE} SET ${Note.CHANGE} = '' WHERE ${Note.CREATE} IS NULL")

                val tempNoteTable = "tempNoteTable"

                execSQL("ALTER TABLE ${Note.TABLE} RENAME TO $tempNoteTable")
                execSQL("""CREATE TABLE ${Note.TABLE} (
                    ${Note.ID} INTEGER PRIMARY KEY NOT NULL,
                    ${Note.CREATE} TEXT NOT NULL,
                    ${Note.CHANGE} TEXT NOT NULL,
                    ${Note.NAME} TEXT NOT NULL,
                    ${Note.TEXT} TEXT NOT NULL,
                    ${Note.COLOR} INTEGER NOT NULL,
                    ${Note.TYPE} INTEGER NOT NULL,
                    ${Note.RANK_PS} TEXT NOT NULL,
                    ${Note.RANK_ID} TEXT NOT NULL,
                    ${Note.BIN} INTEGER NOT NULL,
                    ${Note.STATUS} INTEGER NOT NULL
                    )""")
                execSQL("""INSERT INTO ${Note.TABLE}(${Note.ID},
                                ${Note.CREATE},
                                ${Note.CHANGE},
                                ${Note.NAME},
                                ${Note.TEXT},
                                ${Note.COLOR},
                                ${Note.TYPE},
                                ${Note.RANK_PS},
                                ${Note.RANK_ID},
                                ${Note.BIN},
                                ${Note.STATUS})
                                SELECT ${Note.ID},
                                ${Note.CREATE},
                                ${Note.CHANGE},
                                ${Note.NAME},
                                ${Note.TEXT},
                                ${Note.COLOR},
                                ${Note.TYPE},
                                ${Note.RANK_PS},
                                ${Note.RANK_ID},
                                ${Note.BIN},
                                ${Note.STATUS} FROM $tempNoteTable""")
                execSQL("DROP TABLE $tempNoteTable")

                /**
                 *
                 */

                execSQL("UPDATE ${Roll.TABLE} SET ${Roll.TEXT} = '' WHERE ${Roll.TEXT} IS NULL")

                val tempRollTable = "tempRollTable"
                execSQL("ALTER TABLE ${Roll.TABLE} RENAME TO $tempRollTable")
                execSQL("""CREATE TABLE ${Roll.TABLE} (
                    ${Roll.ID} INTEGER PRIMARY KEY,
                    ${Roll.NOTE_ID} INTEGER NOT NULL,
                    ${Roll.POSITION} INTEGER NOT NULL,
                    ${Roll.CHECK} INTEGER NOT NULL,
                    ${Roll.TEXT} TEXT NOT NULL,
                    FOREIGN KEY (${Roll.NOTE_ID})
                    REFERENCES ${Note.TABLE}(${Note.ID})
                    ON UPDATE CASCADE
                    ON DELETE CASCADE
                    )""")
                execSQL("DROP INDEX index_ROLL_TABLE_RL_NOTE_ID")
                execSQL("CREATE INDEX index_ROLL_TABLE_RL_NOTE_ID ON ${Roll.TABLE}(${Roll.NOTE_ID})")
                execSQL("""INSERT INTO ${Roll.TABLE}(${Roll.ID},
                                ${Roll.NOTE_ID},
                                ${Roll.POSITION},
                                ${Roll.CHECK},
                                ${Roll.TEXT})
                                SELECT ${Roll.ID},
                                ${Roll.NOTE_ID},
                                ${Roll.POSITION},
                                ${Roll.CHECK},
                                ${Roll.TEXT} FROM $tempRollTable""")
                execSQL("DROP TABLE $tempRollTable")

                /**
                 *
                 */

                execSQL("UPDATE ${Rank.TABLE} SET ${Rank.NAME} = '' WHERE ${Rank.NAME} IS NULL")
                execSQL("UPDATE ${Rank.TABLE} SET ${Rank.NOTE_ID} = '${DbField.Value.NONE}' WHERE ${Rank.NOTE_ID} IS NULL")

                val tempRankTable = "tempRankTable"
                execSQL("ALTER TABLE ${Rank.TABLE} RENAME TO $tempRankTable")
                execSQL("""CREATE TABLE ${Rank.TABLE} (
                    ${Rank.ID} INTEGER PRIMARY KEY NOT NULL,
                    ${Rank.NOTE_ID} TEXT NOT NULL,
                    ${Rank.POSITION} INTEGER NOT NULL,
                    ${Rank.NAME} TEXT NOT NULL,
                    ${Rank.VISIBLE} INTEGER NOT NULL
                    )""")
                execSQL("""INSERT INTO ${Rank.TABLE}(${Rank.ID},
                                ${Rank.NOTE_ID},
                                ${Rank.POSITION},
                                ${Rank.NAME},
                                ${Rank.VISIBLE})
                                SELECT ${Rank.ID},
                                ${Rank.NOTE_ID},
                                ${Rank.POSITION},
                                ${Rank.NAME},
                                ${Rank.VISIBLE} FROM $tempRankTable""")
                execSQL("DROP TABLE $tempRankTable")
            }
        }

        fun getInstance(context: Context): RoomDb {
            return Room.databaseBuilder(context, RoomDb::class.java, BuildConfig.DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()   // TODO: 27.09.2018 Сделай нормально
                    .build()
        }
    }


    /**
     * public @interface DefDb {
     *
     *  String NT_TB = "NOTE_TABLE",
     *      NT_ID = "NT_ID",
     *      NT_CR = "NT_CREATE",
     *      NT_CH = "NT_CHANGE",
     *      NT_NM = "NT_NAME",
     *      NT_TX = "NT_TEXT",
     *      NT_CL = "NT_COLOR",
     *      NT_TP = "NT_TYPE",
     *      NT_RK_ID = "NT_RANK_ID",
     *      NT_RK_PS = "NT_RANK_PS",
     *      NT_BN = "NT_BIN",
     *      NT_ST = "NT_STATUS";
     *
     *  String RL_TB = "ROLL_TABLE",
     *      RL_ID = "RL_ID",
     *      RL_ID_NT = "RL_ID_NOTE",
     *      RL_PS = "RL_POSITION",
     *      RL_CH = "RL_CHECK",
     *      RL_TX = "RL_TEXT";
     *
     *  String RK_TB = "RANK_TABLE",
     *      RK_ID = "RK_ID",
     *      RK_ID_NT = "RK_ID_NOTE",
     *      RK_PS = "RK_POSITION",
     *      RK_NM = "RK_NAME",
     *      RK_VS = "RK_VISIBLE";
     *
     *  String none = "NONE";
     *  String divider = ",";
     *
     *  String[] orders = new String[]{
     *      "DATE(" + NT_CR + ") DESC, TIME(" + NT_CR + ") DESC",
     *      "DATE(" + NT_CH + ") DESC, TIME(" + NT_CH + ") DESC",
     *      NT_RK_PS + " ASC",
     *      NT_CL + " ASC"
     *  };
     *
     *  }
     */

}