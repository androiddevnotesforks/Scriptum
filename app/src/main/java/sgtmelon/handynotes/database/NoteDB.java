package sgtmelon.handynotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.model.manager.ManagerRoll;
import sgtmelon.handynotes.model.manager.ManagerStatus;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.model.item.ItemStatus;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.service.Help;

public class NoteDB extends SQLiteOpenHelper {

    //TODO переделать модели и базу данных с помощью библиотеки ROOM

    private static final String DB_NAME = "Notes.db"; //TODO HandyNotes.db
    private static final int version = 1;

    //region KeyWords

    @SuppressWarnings("WeakerAccess")
    public static final String NT_TB = "NOTE_TABLE",
            KEY_NT_ID = "NT_ID",
            KEY_NT_CR = "NT_CREATE",
            KEY_NT_CH = "NT_CHANGE",
            KEY_NT_NM = "NT_NAME",
            KEY_NT_TX = "NT_TEXT",
            KEY_NT_CL = "NT_COLOR",
            KEY_NT_TP = "NT_TYPE",
            KEY_NT_RK_ID = "NT_RANK_ID",
            KEY_NT_RK_PS = "NT_RANK_PS",
            KEY_NT_BN = "NT_BIN",
            KEY_NT_ST = "NT_STATUS";

    private final String KEY_NONE = "NONE";
    private final String rkDivider = ",";

    @SuppressWarnings("WeakerAccess")
    public static final int NT_ID = 0, NT_CR = 1, NT_CH = 2, NT_NM = 3, NT_TX = 4, NT_CL = 5, NT_TP = 6, NT_RK_ID = 7, NT_RK_PS = 8, NT_BN = 9, NT_ST = 10;

    @SuppressWarnings("WeakerAccess")
    public static final String RL_TB = "ROLL_TABLE",
            KEY_RL_ID = "RL_ID",
            KEY_RL_CR = "RL_CREATE",
            KEY_RL_PS = "RL_POSITION",
            KEY_RL_CH = "RL_CHECK",
            KEY_RL_TX = "RL_TEXT";

    @SuppressWarnings("WeakerAccess")
    public static final int RL_ID = 0, RL_CR = 1, RL_PS = 2, RL_CH = 3, RL_TX = 4;

    @SuppressWarnings("WeakerAccess")
    public static final String RK_TB = "RANK_TABLE",
            KEY_RK_ID = "RK_ID",
            KEY_RK_PS = "RK_POSITION",
            KEY_RK_NM = "RK_NAME",
            KEY_RK_CR = "RK_CREATE",
            KEY_RK_VS = "KEY_VISIBLE";

    @SuppressWarnings("WeakerAccess")
    public static final int RK_ID = 0, RK_PS = 1, RK_NM = 2, RK_CR = 3, RK_VS = 4;

    //endregion

    //region KeyValues
    @SuppressWarnings("WeakerAccess")
    public static final int
            typeText = 0, typeRoll = 1,         //Типы заметок
            binFalse = 0, binTrue = 1,          //Расположение относительно карзины
            checkFalse = 0, checkTrue = 1,      //Выполнение пункта
            visibleFalse = 0, visibleTrue = 1,  //Видимость категории
            statusFalse = 0, statusTrue = 1;    //Закрепление в статус-баре
    //endregion

    private final Context context;
    private final SQLiteDatabase db;

    public NoteDB(Context context) {
        super(context, DB_NAME, null, version);
        Log.i("NoteDB", "NoteDB");

        this.context = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("NoteDB", "onCreate");

        sqLiteDatabase.execSQL("CREATE TABLE " + NT_TB + "(" +
                KEY_NT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +    //0
                KEY_NT_CR + " TEXT NOT NULL, " +                        //1 Дата создания
                KEY_NT_CH + " TEXT NOT NULL, " +                        //2 Дата изменения
                KEY_NT_NM + " TEXT NOT NULL, " +                        //3 Название заметки
                KEY_NT_TX + " TEXT NOT NULL, " +                        //4 Текст заметки
                KEY_NT_CL + " INTEGER NOT NULL, " +                     //5 Цвет заметки
                KEY_NT_TP + " INTEGER NOT NULL, " +                     //6 0 - текст, 1 - список
                KEY_NT_RK_ID + " TEXT," +                               //7 ID категорий к которым принадлежит заметка
                KEY_NT_RK_PS + " TEXT," +                               //8 PS категорий к которым принадлежит заметка
                KEY_NT_BN + " INTEGER NOT NULL," +                      //9 0 - не удалили, 1 - удалили
                KEY_NT_ST + " INTEGER NOT NULL);");                     //10 0 - не закреплена, 1 - закреплена

        sqLiteDatabase.execSQL("CREATE TABLE " + RL_TB + "(" +
                KEY_RL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +    //0
                KEY_RL_CR + " TEXT, " +                                 //1 Дата создания
                KEY_RL_PS + " INTEGER NOT NULL, " +                     //2 Позиция в скиске
                KEY_RL_CH + " INTEGER NOT NULL, " +                     //3 Выполнен пункт или нет
                KEY_RL_TX + " TEXT NOT NULL);");                        //4 Текст пункта

        sqLiteDatabase.execSQL("CREATE TABLE " + RK_TB + "(" +
                KEY_RK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +    //0
                KEY_RK_PS + " INTEGER NOT NULL, " +                     //1 Позиция
                KEY_RK_NM + " TEXT UNIQUE NOT NULL, " +                 //2 Уникальное имя категории
                KEY_RK_CR + " TEXT, " +                                 //3 Дата создания, ссылка на заметку
                KEY_RK_VS + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("NoteDB", "onUpgrade");
    }

    //Удаление базы данных
    @SuppressWarnings("unused")
    public void deleteSQLite() {
        Log.i("NoteDB", "deleteSQLite");

        String DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        SQLiteDatabase.deleteDatabase(new File(DB_PATH));
    }

    //region NoteTable

    //DONE
    //Запись данных
    public int insertNote(ItemNote itemNote) {
        Log.i("NoteDB", "insertNote");

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NT_CR, itemNote.getCreate());     //1
        contentValues.put(KEY_NT_CH, itemNote.getChange());     //2
        contentValues.put(KEY_NT_NM, itemNote.getName());       //3
        contentValues.put(KEY_NT_TX, itemNote.getText());       //4
        contentValues.put(KEY_NT_CL, itemNote.getColor());      //5
        contentValues.put(KEY_NT_TP, itemNote.getType());       //6

        String noteRkId = KEY_NONE, noteRkPs = KEY_NONE;

        String[] ntRankId = itemNote.getRankId();
        if (ntRankId.length != 0) {
            noteRkId = TextUtils.join(rkDivider, ntRankId);
            noteRkPs = convertRankIdToPos(ntRankId);
        }

        contentValues.put(KEY_NT_RK_ID, noteRkId);                  //7
        contentValues.put(KEY_NT_RK_PS, noteRkPs);                  //8
        contentValues.put(KEY_NT_BN, binFalse);                     //9
        contentValues.put(KEY_NT_ST, statusFalse);                  //10

        return (int) db.insertOrThrow(NT_TB, "", contentValues); //Запись
    }

    //DONE
    //Возвращает строку с методом сортировки заметок
    private String getNoteOrder(String sortKeys) {
        Log.i("NoteDB", "getNoteOrder");

        String[] orders = new String[]{
                "DATE(" + KEY_NT_CR + ") DESC, TIME(" + KEY_NT_CR + ") DESC",
                "DATE(" + KEY_NT_CH + ") DESC, TIME(" + KEY_NT_CH + ") DESC",
                KEY_NT_RK_PS + " ASC",
                KEY_NT_CL + " ASC"
        };

        StringBuilder order = new StringBuilder();

        String[] sortKeysArr = sortKeys.split(Help.Pref.divider);

        for (String aSortKey : sortKeysArr) {
            int key = Integer.parseInt(aSortKey);

            order.append(orders[key]);

            if (key != Help.Pref.sortCr && key != Help.Pref.sortCh) {
                order.append(Help.Pref.divider);
            } else break;
        }
        return order.toString();
    }

    //DONE
    //Забирает значения по ID
    public ItemNote getNote(int ntId) {
        Log.i("NoteDB", "getNote");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_ID + "='" + ntId + "'", null);

        cursor.moveToFirst();

        //region ItemNote data
        ItemNote itemNote = new ItemNote();

        itemNote.setId(cursor.getInt(NT_ID));
        itemNote.setCreate(cursor.getString(NT_CR));
        itemNote.setChange(cursor.getString(NT_CH));
        itemNote.setName(cursor.getString(NT_NM));
        itemNote.setText(cursor.getString(NT_TX));
        itemNote.setColor(cursor.getInt(NT_CL));
        itemNote.setType(cursor.getInt(NT_TP));

        String[] rankId = new String[0];
        String rankIdCursor = cursor.getString(NT_RK_ID);
        if (!rankIdCursor.equals(KEY_NONE)) rankId = rankIdCursor.split(rkDivider);

        itemNote.setRankId(rankId);
        itemNote.setBin(cursor.getInt(NT_BN) == binTrue);
        itemNote.setStatus(cursor.getInt(NT_ST) == statusTrue);
        //endregion

        cursor.close();
        return itemNote;
    }

    //DONE
    //Забирает значения по значениям фильтра
    public List<ItemNote> getNote(int ntBin, String sortKeys) {
        Log.i("NoteDB", "getNote");

        List<ItemNote> listNote = new ArrayList<>();

        String order = getNoteOrder(sortKeys);
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_BN + " ='" + ntBin + "'" +
                " ORDER BY " + order, null);

        List<String> rankVs = getRankVisible();

        //region ItemNote data
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String[] rankId = new String[0];
            String rankIdCursor = cursor.getString(NT_RK_ID);
            if (!rankIdCursor.equals(KEY_NONE)) rankId = rankIdCursor.split(rkDivider);

            if (rankId.length == 0 || rankVs.contains(rankId[0])) {    //Если нет категорий или первая ID категории заметки находится в списке видимых, то добавить в список
                ItemNote itemNote = new ItemNote();

                itemNote.setId(cursor.getInt(NT_ID));
                itemNote.setCreate(cursor.getString(NT_CR));
                itemNote.setChange(cursor.getString(NT_CH));
                itemNote.setName(cursor.getString(NT_NM));
                itemNote.setText(cursor.getString(NT_TX));
                itemNote.setColor(cursor.getInt(NT_CL));
                itemNote.setType(cursor.getInt(NT_TP));
                itemNote.setRankId(rankId);
                itemNote.setBin(cursor.getInt(NT_BN) == binTrue);
                itemNote.setStatus(cursor.getInt(NT_ST) == statusTrue);

                listNote.add(itemNote);
            }
        }
        //endregion

        cursor.close();
        return listNote;
    }

    //DONE
    public ManagerStatus getListStatusManager() {
        Log.i("NoteDB", "getListStatusManager");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_ST + "='" + statusTrue + "'" +
                " ORDER BY DATE(" + KEY_NT_CR + ") DESC, TIME(" + KEY_NT_CR + ") DESC", null);

        List<String> listNoteCreate = new ArrayList<>();
        List<ItemStatus> listStatus = new ArrayList<>();

        List<String> rankVs = getRankVisible();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            //region ItemNote data
            ItemNote itemNote = new ItemNote();

            itemNote.setId(cursor.getInt(NT_ID));
            itemNote.setCreate(cursor.getString(NT_CR));
            itemNote.setChange(cursor.getString(NT_CH));
            itemNote.setName(cursor.getString(NT_NM));
            itemNote.setText(cursor.getString(NT_TX));
            itemNote.setColor(cursor.getInt(NT_CL));
            itemNote.setType(cursor.getInt(NT_TP));

            String[] rankId = new String[0];
            String rankIdCursor = cursor.getString(NT_RK_ID);
            if (!rankIdCursor.equals(KEY_NONE)) rankId = rankIdCursor.split(rkDivider);

            itemNote.setRankId(rankId);
            itemNote.setBin(cursor.getInt(NT_BN) == binTrue);
            itemNote.setStatus(cursor.getInt(NT_ST) == statusTrue);
            //endregion

            ItemStatus itemStatus = new ItemStatus(context, itemNote, Help.Array.strListToArr(rankVs));
            if (rankId.length != 0 && !rankVs.contains(rankId[0])) itemStatus.cancelNote();

            listNoteCreate.add(itemNote.getCreate());
            listStatus.add(itemStatus);
        }

        cursor.close();

        return new ManagerStatus(context, listNoteCreate, listStatus);
    }

    //DONE
    //Обновление значения заметки в базе (при сохранении результатов изменения)
    public void updateNote(ItemNote itemNote) {
        Log.i("NoteDB", "updateNote");

        String noteRkId = KEY_NONE, noteRkPs = KEY_NONE;

        String[] ntRankId = itemNote.getRankId();
        if (ntRankId.length != 0) {
            noteRkId = TextUtils.join(rkDivider, ntRankId);
            noteRkPs = convertRankIdToPos(ntRankId);
        }

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_CH + "='" + itemNote.getChange() + "' , " +
                KEY_NT_NM + "='" + itemNote.getName() + "' , " +
                KEY_NT_TX + "='" + itemNote.getText() + "' , " +
                KEY_NT_CL + "='" + itemNote.getColor() + "' , " +
                KEY_NT_RK_ID + "='" + noteRkId + "' , " +
                KEY_NT_RK_PS + "='" + noteRkPs + "'" +
                " WHERE " + KEY_NT_ID + "='" + itemNote.getId() + "'");
    }

    //DONE
    //Обновление удаления (в корзину или из неё)
    public void updateNote(int ntId, String ntChange, int ntBin) {
        Log.i("NoteDB", "updateNote");

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_CH + "='" + ntChange + "' , " +
                KEY_NT_BN + "='" + ntBin + "'" +
                " WHERE " + KEY_NT_ID + "='" + ntId + "'");
    }

    //DONE
    //Обновление привязки к статус бару
    public void updateNote(int ntId, boolean ntStatus) {
        Log.i("NoteDB", "updateNote");

        int noteSt = ntStatus ? statusTrue : statusFalse;

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_ST + "='" + noteSt + "'" +
                " WHERE " + KEY_NT_ID + "='" + ntId + "'");
    }

    //DONE использовать обычное обновление
    //Смена типа
    public void updateNoteType(ItemNote itemNote) {
        Log.i("NoteDB", "updateNoteType");

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_CH + "='" + itemNote.getChange() + "' , " +
                KEY_NT_TP + "='" + itemNote.getType() + "' , " +
                KEY_NT_TX + "='" + itemNote.getText() + "'" +
                " WHERE " + KEY_NT_ID + "='" + itemNote.getId() + "'");
    }

    //DONE использовать обычное обновление
    //Обновление текста (для списков, при выполении пунктов)
    public void updateNoteText(ItemNote itemNote) {
        Log.i("NoteDB", "updateNoteText");

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_CH + "='" + itemNote.getChange() + "' , " +
                KEY_NT_TX + "='" + itemNote.getText() + "'" +
                " WHERE " + KEY_NT_ID + "='" + itemNote.getId() + "'");
    }

    //DONE
    //Обновление при удалении категории (убираем связь)
    private void updateNote(String[] ntCreate, String ntRankId) {
        Log.i("NoteDB", "updateNote");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_CR + " IN ('" + TextUtils.join("', '", ntCreate) + "')", null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String[] rankIdOld = cursor.getString(NT_RK_ID).split(rkDivider);

            List<String> rankId = new ArrayList<>(Arrays.asList(rankIdOld));
            int index = rankId.indexOf(ntRankId);
            rankId.remove(index);

            String[] rankIdNew = Help.Array.strListToArr(rankId);

            String noteRkId = KEY_NONE, noteRkPs = KEY_NONE;
            if (rankIdNew.length != 0) {
                noteRkId = TextUtils.join(rkDivider, rankIdOld);
                noteRkPs = convertRankIdToPos(rankIdNew);
            }

            db.execSQL("UPDATE " + NT_TB + " SET " +
                    KEY_NT_RK_ID + "='" + noteRkId + "' , " +
                    KEY_NT_RK_PS + "='" + noteRkPs + "'" +
                    " WHERE " + KEY_NT_ID + "='" + cursor.getInt(NT_ID) + "'");
        }
        cursor.close();
    }

    //DONE
    //Обновление при перетаскивании категории
    private void updateNote(int ntId, String[] ntRankId) {
        Log.i("NoteDB", "updateNote");

        db.execSQL("UPDATE " + NT_TB + " SET " +
                KEY_NT_RK_ID + "='" + TextUtils.join(rkDivider, ntRankId) + "' , " +
                KEY_NT_RK_PS + "='" + convertRankIdToPos(ntRankId) + "'" +
                " WHERE " + KEY_NT_ID + "='" + ntId + "'");
    }

    //DONE
    //Удаление заметок из корзины
    public void clearBin() { //В курсор загружаем данные, только те, где в колонке KEY_NT_BN = 1
        Log.i("NoteDB", "prefClearBin");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_BN + "='" + binTrue + "'", null);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String[] rankId = new String[0];
            String rankIdCursor = cursor.getString(NT_RK_ID);
            if (!rankIdCursor.equals(KEY_NONE)) rankId = rankIdCursor.split(rkDivider);

            List<String> rankVs = getRankVisible();

            if (rankId.length == 0 || rankVs.contains(rankId[0])) {     //Если нет категорий или первая ID категории заметки находится в списке видимых, то удалить
                String noteCr = cursor.getString(NT_CR);

                if (cursor.getInt(NT_TP) == typeRoll) {
                    deleteRoll(noteCr);               //Если это список, то удаляем и пункты из него
                }
                if (rankId.length != 0) {
                    clearRank(noteCr, rankId);        //Если есть категории, то чистим их
                }

                db.execSQL("DELETE FROM " + NT_TB + " WHERE " + KEY_NT_ID + "='" + cursor.getInt(NT_ID) + "'");
            }
        }
        cursor.close();
    }

    //DONE
    //Удаление заметки
    public void deleteNote(int id) { //В курсор загружаем лишь одну заметку, где KEY_NT_ID = id
        Log.i("NoteDB", "deleteNote");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_ID + "='" + id + "'", null);

        cursor.moveToFirst();

        String noteCr = cursor.getString(NT_CR);            //Если это список, то удаляем и пункты из него
        if (cursor.getInt(NT_TP) == typeRoll) {
            deleteRoll(noteCr);
        }

        String rankIdCursor = cursor.getString(NT_RK_ID);   //Если есть категории, то чистим их
        if (!rankIdCursor.equals(KEY_NONE)) {
            String[] rankId = rankIdCursor.split(rkDivider);
            clearRank(noteCr, rankId);
        }

        cursor.close();

        db.execSQL("DELETE FROM " + NT_TB + " WHERE " + KEY_NT_ID + "='" + id + "'");
    }
    //endregion

    //region RollTable

    //DONE
    //Заполнение данных
    public int insertRoll(String rlCreate, int rlPosition, boolean rlCheck, String rlText) {
        Log.i("NoteDB", "insertRoll");

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_RL_CR, rlCreate);       //1
        contentValues.put(KEY_RL_PS, rlPosition);     //2
        contentValues.put(KEY_RL_CH, rlCheck);        //3
        contentValues.put(KEY_RL_TX, rlText);         //4

        return (int) db.insertOrThrow(RL_TB, "", contentValues); //Запись
    }

    //DONE
    //Заполнение данных при переводе текстовой заметки в список
    public ItemRollView insertRoll(String rlCreate, String[] rlText) {
        Log.i("NoteDB", "insertRoll");

        ItemRollView itemRollView = new ItemRollView();

        List<ItemRoll> listRoll = new ArrayList<>();
        int rollPs = 0;                                     //Если пустое место то будет сдвиг по позиции

        for (String aRollTx : rlText) {
            if (!aRollTx.equals("")) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(KEY_RL_CR, rlCreate);     //1
                contentValues.put(KEY_RL_PS, rollPs);       //2
                contentValues.put(KEY_RL_CH, checkFalse);   //3
                contentValues.put(KEY_RL_TX, aRollTx);      //4

                int rollId = (int) db.insertOrThrow(RL_TB, "", contentValues); //Запись

                if (rollPs <= 3) {
                    ItemRoll itemRoll = new ItemRoll();

                    itemRoll.setId(rollId);             //0
                    itemRoll.setPosition(rollPs);       //2
                    itemRoll.setCheck(false);           //3
                    itemRoll.setText(aRollTx);          //4
                    itemRoll.setExist(true);

                    listRoll.add(itemRoll);
                }
                rollPs++;
            }
        }

        itemRollView.setListRoll(listRoll);
        itemRollView.setSize(rollPs);

        return itemRollView;
    }

    //DONE
    //Забирает значение списка
    public List<ItemRoll> getRoll(String rlCreate) {
        Log.i("NoteDB", "getRoll");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RL_TB +
                " WHERE " + KEY_RL_CR + "='" + rlCreate + "'" +
                " ORDER BY " + KEY_RL_PS, null);

        List<ItemRoll> listRoll = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            ItemRoll itemRoll = new ItemRoll();

            itemRoll.setId(cursor.getInt(RL_ID));
            itemRoll.setPosition(cursor.getInt(RL_PS));
            itemRoll.setCheck(cursor.getInt(RL_CH) == checkTrue);
            itemRoll.setText(cursor.getString(RL_TX));
            itemRoll.setExist(true);

            listRoll.add(itemRoll);
        }
        cursor.close();
        return listRoll;
    }

    //DONE
    public ManagerRoll getListRollManager() { //TODO подумай
        Log.i("NoteDB", "getListRollManager");

        List<String> listNoteCreate = new ArrayList<>();
        List<ItemRollView> listRollView = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RL_TB +
                " WHERE (" + KEY_RL_PS + " BETWEEN 0 AND 3)" +
                " ORDER BY DATE(" + KEY_RL_CR + ") DESC, TIME(" + KEY_RL_CR + ") DESC, " + KEY_RL_PS + " ASC", null);

        List<ItemRoll> listRoll = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String ntCreate = cursor.getString(RL_CR);
            if (!listNoteCreate.contains(ntCreate)) {
                listNoteCreate.add(ntCreate);

                if (listRoll.size() != 0) {
                    listRollView.add(new ItemRollView(listRoll));
                    listRoll = new ArrayList<>();
                }
            }

            //region ItemRoll data
            ItemRoll itemRoll = new ItemRoll();

            itemRoll.setId(cursor.getInt(RL_ID));
            itemRoll.setPosition(cursor.getInt(RL_PS));
            itemRoll.setCheck(cursor.getInt(RL_CH) == checkTrue);
            itemRoll.setText(cursor.getString(RL_TX));
            itemRoll.setExist(true);
            //endregion

            listRoll.add(itemRoll);
        }

        if (listRoll.size() != 0) {
            listRollView.add(new ItemRollView(listRoll));
        }

        cursor.close();

        return new ManagerRoll(listNoteCreate, listRollView);
    }

    //DONE
    //Возвращает строку текста собранную из списка
    public String getRollText(String rlCreate) {
        Log.i("NoteDB", "getRollText");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RL_TB +
                " WHERE " + KEY_RL_CR + "='" + rlCreate + "'" +
                " ORDER BY " + KEY_RL_PS, null);

        StringBuilder rollTx = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            if (i != 0) rollTx.append("\n");

            rollTx.append(cursor.getString(RL_TX));
        }

        cursor.close();
        return rollTx.toString();
    }

    //DONE
    //Возвращает строку текста собранную из списка для уведомления
    public String getRollText(String rlCreate, String rlCheck) {
        Log.i("NoteDB", "getRollText");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RL_TB +
                " WHERE " + KEY_RL_CR + "='" + rlCreate + "'" +
                " ORDER BY " + KEY_RL_PS, null);

        StringBuilder rollTx = new StringBuilder();
        rollTx.append(rlCheck).append(" |");

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            if (cursor.getInt(RL_CH) == checkTrue) rollTx.append(" \u2713 ");
            else rollTx.append(" - ");

            rollTx.append(cursor.getString(RL_TX));

            if (i != cursor.getCount() - 1) rollTx.append(" |");
        }

        cursor.close();
        return rollTx.toString();
    }

    //DONE
    //Обновление позиции в списке и текста
    public void updateRoll(int rlId, int rlPosition, String rlText) {
        Log.i("NoteDB", "updateRoll");

        db.execSQL("UPDATE " + RL_TB +
                " SET " + KEY_RL_PS + "='" + rlPosition + "' , " + KEY_RL_TX + "='" + rlText + "'" +
                " WHERE " + KEY_RL_ID + "='" + rlId + "'");
    }

    //DONE
    //Обновление выполнения для одного пункта
    public void updateRoll(int rlId, boolean rlCheck) {
        Log.i("NoteDB", "updateRoll");

        int rollCh = rlCheck ? NoteDB.checkTrue : NoteDB.checkFalse;

        db.execSQL("UPDATE " + RL_TB +
                " SET " + KEY_RL_CH + "='" + rollCh + "'" +
                " WHERE " + KEY_RL_ID + "='" + rlId + "'");
    }

    //DONE
    //Обновление выполнения для всех пунктов
    public void updateRoll(String rlCreate, int rlCheck) {
        Log.i("NoteDB", "updateRoll");

        db.execSQL("UPDATE " + RL_TB +
                " SET " + KEY_RL_CH + "='" + rlCheck + "'" +
                " WHERE " + KEY_RL_CR + "='" + rlCreate + "'");
    }

    //DONE
    //Удаление пункта при сохранении после свайпа
    public void deleteRoll(String rlCreate, String[] notSwipeRlId) {
        Log.i("NoteDB", "deleteRoll");

        db.execSQL("DELETE FROM " + RL_TB +
                " WHERE " + KEY_RL_CR + "='" + rlCreate + "'" +
                " AND " + KEY_RL_ID + " NOT IN ('" + TextUtils.join("', '", notSwipeRlId) + "')");
    }

    //DONE
    //Удаление пунктов при удалении заметки
    public void deleteRoll(String rlCreate) {
        Log.i("NoteDB", "deleteRoll");

        db.execSQL("DELETE FROM " + RL_TB + " WHERE " + KEY_RL_CR + "='" + rlCreate + "'");
    }

    //endregion

    //region RankTable

    //DONE
    //Количество категорий
    public int getRankCount() {  //TODO переделать запрос
        Log.i("NoteDB", "getRankCount");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //DONE - 3 метода
    //Возвращает массив с количеством текстов/списков/выполненых пунктов/всего пунктов
    private int[] getRankCount(String[] ntCreate) {
        Log.i("NoteDB", "getRankCount");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_CR + " IN ('" + TextUtils.join("', '", ntCreate) + "')" +
                " ORDER BY DATE(" + KEY_NT_CR + ") DESC, TIME(" + KEY_NT_CR + ") DESC", null);

        int[] rankCount = new int[]{0, 0, 0, 0}; //typeText, typeRoll, rollCheckCount, rollAllCount

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            if (cursor.getInt(NT_TP) == typeText) rankCount[typeText]++;
            else {
                rankCount[typeRoll]++;
                int[] rollCh = Help.Note.getCheckValue(cursor.getString(NT_TX));
                rankCount[2] += rollCh[0];  //rollCheckCount
                rankCount[3] += rollCh[1];  //rollAllCount
            }
        }

        cursor.close();
        return rankCount;
    }

    //DONE
    //Заполнение данных
    public int insertRank(int rkPosition, String rkName) {
        Log.i("NoteDB", "insertRank");

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_RK_PS, rkPosition);   //1 (-1 в начало, rankCount в конец)
        contentValues.put(KEY_RK_NM, rkName);       //2
        contentValues.put(KEY_RK_CR, "");           //3
        contentValues.put(KEY_RK_VS, visibleTrue);  //4

        return (int) db.insertOrThrow(RK_TB, "", contentValues);
    }

    //DONE
    //Забирает значения категорий из стола
    public List<ItemRank> getRank() {
        Log.i("NoteDB", "getRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        List<ItemRank> listRank = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            ItemRank itemRank = new ItemRank();
            itemRank.setId(cursor.getInt(RK_ID));
            itemRank.setPosition(cursor.getInt(RK_PS));
            itemRank.setName(cursor.getString(RK_NM));

            String rankCr = cursor.getString(RK_CR);
            if (!rankCr.equals("")) itemRank.setCreate(rankCr.split(rkDivider));
            else itemRank.setCreate(new String[0]);

            itemRank.setVisible(cursor.getInt(RK_VS) == visibleTrue);

            int[] rankCount = getRankCount(itemRank.getCreate());
            itemRank.setTextCount(rankCount[typeText]);
            itemRank.setRollCount(rankCount[typeRoll]);
            itemRank.setRollCheck(rankCount[2], rankCount[3]);

            listRank.add(itemRank);
        }
        cursor.close();
        return listRank;
    }

    //DONE
    private List<String> getRankVisible() {
        Log.i("NoteDB", "getRankVisible");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " WHERE " + KEY_RK_VS + "='" + visibleTrue + "'" +
                " ORDER BY " + KEY_RK_PS, null);

        List<String> rankVs = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            rankVs.add(cursor.getString(RK_ID));
        }
        cursor.close();

        return rankVs;
    }

    //DONE
    //Забирает значения имён категорий из стола
    public List<String> getRankName() {
        Log.i("NoteDB", "getRankName");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        List<String> listRankName = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            listRankName.add(cursor.getString(RK_NM).toUpperCase());
        }
        cursor.close();
        return listRankName;
    }

    //DONE - 2 метода
    //Возвращает значения относительно указанной колонки
    public String[] getRankColumn(int column) {
        Log.i("NoteDB", "getRankColumn");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        String[] rankColumn = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            rankColumn[i] = cursor.getString(column);
        }
        cursor.close();
        return rankColumn;
    }

    //DONE
    //Возвращает массив, в котором отмечается есть ли категория в массиве с ID категорий
    public boolean[] getRankCheck(String[] rkId) {
        Log.i("NoteDB", "getRankCheck");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        boolean[] rankCh = new boolean[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            rankCh[i] = Arrays.asList(rkId).contains(cursor.getString(RK_ID));
        }
        cursor.close();
        return rankCh;
    }

    //DONE
    //Обновление позиций при перемещении
    public void updateRank(int startPs, int endPs) { //TODO избежать for
        Log.i("NoteDB", "updateRank");

        Cursor rankCursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        int rankPs;
        String[] noteCr = new String[0];            //Массив дат создания заметок, которые привязаны к сместившимся категориям

        if (startPs < endPs) {                        //Перемещение плитки вниз
            for (int i = startPs; i <= endPs; i++) {  //от начальной позиции и до конечной
                rankCursor.moveToPosition(i);

                if (i == startPs)
                    rankPs = endPs;                  //Если это плитка которую переместили, то её позиция = конечной
                else rankPs = i - 1;                  //В других случаях позиции уменьшаем на 1

                String[] rankCr = rankCursor.getString(RK_CR).split(rkDivider); //Узнаём к каким заметкам привязана категория
                for (String aRankCr : rankCr) {                                 //Для каждой привязки проверяем присутствие в созданном масиве
                    if (!Arrays.asList(noteCr).contains(aRankCr))               //Если есть
                        noteCr = Help.Array.addStrItem(noteCr, aRankCr);        //Добавляем туда новую ДАТУ
                }

                Log.i("NoteDB, updateRank", "Old pos: " + rankCursor.getInt(RK_PS) + " | " + "New pos: " + rankPs);

                db.execSQL("UPDATE " + RK_TB +
                        " SET " + KEY_RK_PS + "='" + rankPs + "'" +
                        " WHERE " + KEY_RK_ID + "='" + rankCursor.getInt(RK_ID) + "'");

            }
        } else {                                        //Перемещение плитки вверх
            for (int i = endPs; i <= startPs; i++) {  //от конечной позиции и до начальной
                rankCursor.moveToPosition(i);

                if (i == startPs)
                    rankPs = endPs;                  //Если это плитка которую переместили, то её позиция = конечной
                else rankPs = i + 1;                  //В других случаях позиции увеличиваем на 1

                String[] rankCr = rankCursor.getString(RK_CR).split(rkDivider); //Узнаём к каким заметкам привязана категория
                for (String aRankCr : rankCr) {                                 //Для каждой привязки проверяем присутствие в созданном масиве
                    if (!Arrays.asList(noteCr).contains(aRankCr))               //Если есть
                        noteCr = Help.Array.addStrItem(noteCr, aRankCr);        //Добавляем туда новую ДАТУ
                }

                Log.i("NoteDB, updateRank", "Old pos: " + rankCursor.getInt(RK_PS) + " | " + "New pos: " + rankPs);

                db.execSQL("UPDATE " + RK_TB +
                        " SET " + KEY_RK_PS + "='" + rankPs + "'" +
                        " WHERE " + KEY_RK_ID + "='" + rankCursor.getInt(RK_ID) + "'");
            }
        }
        rankCursor.close();

        //Получаем те заметки, которые нужно обновить
        Cursor noteCursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB +
                " WHERE " + KEY_NT_CR + " IN ('" + TextUtils.join("', '", noteCr) + "')" +
                " ORDER BY DATE(" + KEY_NT_CR + ") DESC, TIME(" + KEY_NT_CR + ") DESC", null);

        for (int i = 0; i < noteCursor.getCount(); i++) {
            noteCursor.moveToPosition(i);

            String[] rankIdOld = noteCursor.getString(NT_RK_ID).split(rkDivider);   //Те категории, которые уже имеются в заметке (не обновлена позиция)
            String[] rankIdNew = new String[rankIdOld.length];                      //Массив для отсортированых заметок

            int j = 0;
            for (String aRankId : getRankColumn(RK_ID)) {           //Перебор массива с id категорий ("1", "2", "3", ...)
                if (Arrays.asList(rankIdOld).contains(aRankId)) {   //Если заметка привязана к ней
                    rankIdNew[j] = aRankId;                         //То записываем уже с новой позицией
                    j++;                                            //Увеличиваем индекс
                }
            }
            updateNote(noteCursor.getInt(NT_ID), rankIdNew);        //Обновляем заметку (категории)
        }
        noteCursor.close();
    }

    //DONE
    //Обновление позиций при удалении категории
    public void updateRank(int startPs) { //TODO избежать for
        Log.i("NoteDB", "updateRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        for (int i = startPs; i < cursor.getCount(); i++) { //От позиции удаления и до конца
            cursor.moveToPosition(i);

            db.execSQL("UPDATE " + RK_TB +
                    " SET " + KEY_RK_PS + "='" + i + "'" +
                    " WHERE " + KEY_RK_ID + "='" + cursor.getInt(RK_ID) + "'");
        }
        cursor.close();
    }

    //DONE
    //Обновление категории при сохранении заметки
    public void updateRank(String ntCreate, String[] ntRankId) { //TODO избежать for
        Log.i("NoteDB", "updateRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " ORDER BY " + KEY_RK_PS, null);

        boolean[] rankCheck = getRankCheck(ntRankId);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String rankCrCursor = cursor.getString(RK_CR);
            List<String> rankCrTemp = new ArrayList<>(Arrays.asList(rankCrCursor.split(rkDivider)));

            String rankCr = "";

            if (rankCheck[i]) {                                         //Если категория отмечена
                if (!rankCrCursor.equals("")) {
                    if (rankCrTemp.contains(ntCreate)) {                //Если есть дата, которую хотим добавить
                        rankCr = rankCrCursor;                          //Возвращаем строку, которая и была
                    } else
                        rankCr = rankCrCursor + rkDivider + ntCreate;   //Добавляем новую дату
                } else
                    rankCr = ntCreate;                                  //Добавляем новую дату
            } else {
                if (!rankCrCursor.equals("")) {
                    if (rankCrTemp.contains(ntCreate)) {                //Если есть дата, которую хотим добавить
                        rankCrTemp.remove(ntCreate);                    //Убираем дату, которая не отмечена
                        rankCr = TextUtils.join(rkDivider, rankCrTemp); //Собираем строку для записи
                    } else
                        rankCr = rankCrCursor;                          //Строка, которая и была
                }
            }

            db.execSQL("UPDATE " + RK_TB +
                    " SET " + KEY_RK_CR + "='" + rankCr + "'" +
                    " WHERE " + KEY_RK_ID + "='" + cursor.getInt(RK_ID) + "'");
        }
        cursor.close();
    }

    //DONE
    //Обновление при смене имени
    public void updateRank(int rkId, String rkName) {
        Log.i("NoteDB", "updateRank");

        db.execSQL("UPDATE " + RK_TB +
                " SET " + KEY_RK_NM + "='" + rkName + "'" +
                " WHERE " + KEY_RK_ID + "='" + rkId + "'");
    }

    //DONE
    //Обновление при нажатие на кнопку скрыть/показать
    public void updateRank(int rkId, boolean rkVisible) {
        Log.i("NoteDB", "updateRank");

        int rankVs = rkVisible ? visibleTrue : visibleFalse;
        db.execSQL("UPDATE " + RK_TB +
                " SET " + KEY_RK_VS + "='" + rankVs + "'" +
                " WHERE " + KEY_RK_ID + "='" + rkId + "'");
    }

    //DONE
    //Обновление при долгом нажатие на кнопку скрыть/показать
    public void updateRank(List<ItemRank> listRank) { //TODO избежать for
        Log.i("NoteDB", "updateRank");

        for (ItemRank itemRank : listRank) {
            updateRank(itemRank.getId(), itemRank.isVisible());
        }
    }

    //DONE
    //Удаление категории и её данных из базы данных заметок
    public void deleteRank(String rkName) {
        Log.i("NoteDB", "deleteRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " WHERE " + KEY_RK_NM + "='" + rkName + "'", null);

        cursor.moveToFirst();

        String rankCr = cursor.getString(RK_CR);
        if (!rankCr.equals("")) {
            updateNote(rankCr.split(rkDivider), cursor.getString(RK_ID));
        }

        db.execSQL("DELETE FROM " + RK_TB +
                " WHERE " + KEY_RK_NM + "='" + rkName + "'");

        cursor.close();
    }

    //DONE
    //Чистит категории при удалении заметки
    private void clearRank(String rkCreate, String[] rkId) {//TODO избежать for
        Log.i("NoteDB", "clearRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " WHERE " + KEY_RK_ID + " IN ('" + TextUtils.join("', '", rkId) + "')" +
                " ORDER BY " + KEY_RK_PS + " ASC", null);

        Log.i("NoteDB, clearRank", "Cursor count: " + cursor.getCount());

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            String[] rankCrOld = cursor.getString(RK_CR).split(rkDivider);  //Получаем массив с ссылками на заметки

            List<String> rankCrNew = new ArrayList<>(Arrays.asList(rankCrOld));
            int index = rankCrNew.indexOf(rkCreate);
            rankCrNew.remove(index);

            db.execSQL("UPDATE " + RK_TB +
                    " SET " + KEY_RK_CR + "='" + TextUtils.join(rkDivider, rankCrNew) + "'" +
                    " WHERE " + KEY_RK_ID + "='" + cursor.getInt(RK_ID) + "'");
        }
        cursor.close();
    }

    //NOT NEED
    //Переводит из массива с id в строку с позициями
    private String convertRankIdToPos(String[] rkId) {
        Log.i("NoteDB", "convertRankIdToPos");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB +
                " WHERE " + KEY_RK_ID + " IN ('" + TextUtils.join("', '", rkId) + "')" +
                " ORDER BY " + KEY_RK_PS + " ASC", null);

        String rankPs[] = new String[cursor.getCount()];
        for (int i = 0; i < rankPs.length; i++) {
            cursor.moveToPosition(i);
            rankPs[i] = cursor.getString(RK_PS);
        }
        cursor.close();

        String rkPosition = KEY_NONE;
        if (rankPs.length != 0) rkPosition = TextUtils.join(", ", rankPs);

        return rkPosition;
    }
    //endregion

    //region HelpAdmin

    //Вывод всех заметок
    public void listAllNote(TextView textView) {
        Log.i("NoteDB", "listAllNote");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB + " ORDER BY " + KEY_NT_CR + " DESC", null);

        String annotation = "Note Data Base: " + version + "v, " + cursor.getCount() + " count";

        textView.setText(annotation);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            textView.append("\n\n" +
                    "ID: " + cursor.getString(NT_ID) + " | " +
                    "CR: " + cursor.getString(NT_CR) + " | " +
                    "CH: " + cursor.getString(NT_CH) + "\n");

            String noteName = cursor.getString(NT_NM);
            if (!noteName.equals(""))
                textView.append("NM: " + noteName + "\n");

            String noteText = cursor.getString(NT_TX);
            textView.append("TX: " + noteText.substring(0, Math.min(noteText.length(), 45)).replace("\n", " "));
            if (noteText.length() > 40) textView.append("...");
            textView.append("\n");

            textView.append("CL: " + cursor.getString(NT_CL) + " | " +
                    "TP: " + cursor.getString(NT_TP) + " | " +
                    "BN: " + cursor.getString(NT_BN) + "\n" +
                    "RK ID: " + cursor.getString(NT_RK_ID).replace(rkDivider, ", ") + " | " +
                    "RK PS: " + cursor.getString(NT_RK_PS).replace(rkDivider, ", ") + "\n" +
                    "ST: " + cursor.getString(NT_ST));
        }
        cursor.close();
    }

    //Количество списков
    private int noteRollCount() {
        Log.i("NoteDB", "noteRollCount");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + NT_TB + " WHERE " + KEY_NT_TP + " ='" + typeRoll + "'", null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    //Вывод всех заметок
    public void listAllRoll(TextView textView) {
        Log.i("NoteDB", "listAllRoll");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RL_TB + " ORDER BY " + KEY_RL_CR + " DESC", null);

        String annotation = "Roll Data Base: " + version + "v, " + cursor.getCount() + " count, from " + noteRollCount() + " notes";

        textView.setText(annotation);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            textView.append("\n\n" +
                    "ID: " + cursor.getString(RL_ID) + " | " +
                    "CR: " + cursor.getString(RL_CR) + " | " +
                    "PS: " + cursor.getString(RL_PS) + " | " +
                    "CH: " + cursor.getString(RL_CH) + "\n");

            String rollText = cursor.getString(RL_TX);
            textView.append("TX: " + rollText.substring(0, Math.min(rollText.length(), 45)).replace("\n", " "));
            if (rollText.length() > 40) textView.append("...");
        }
        cursor.close();
    }

    //Вывод всех категорий
    public void listAllRank(TextView textView) {
        Log.i("NoteDB", "listAllRank");

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM " + RK_TB + " ORDER BY " + KEY_RK_PS, null);
        int count = cursor.getCount();

        String annotation = "Rank Data Base: " + version + "v, " + count + " count";

        textView.setText(annotation);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);

            textView.append("\n\n" +
                    "ID: " + cursor.getString(RK_ID) + " | " +
                    "PS: " + cursor.getString(RK_PS) + "\n" +
                    "NM: " + cursor.getString(RK_NM) + "\n" +
                    "CR: " + cursor.getString(RK_CR).replace(rkDivider, ", ") + "\n" +
                    "VS: " + cursor.getString(RK_VS));
        }
        cursor.close();
    }

    //endregion
}
