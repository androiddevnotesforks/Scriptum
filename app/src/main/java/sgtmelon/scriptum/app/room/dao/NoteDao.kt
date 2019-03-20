package sgtmelon.scriptum.app.room.dao

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import androidx.room.*
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.screen.main.notes.NotesViewModel
import sgtmelon.scriptum.office.utils.Preference
import java.util.*

/**
 * Класс для общения Dao заметок [RoomDb]
 */
@Dao
interface NoteDao : BaseDao {

    @Insert
    fun insert(noteItem: NoteItem): Long

    @Query(value = "SELECT * FROM NOTE_TABLE " + "WHERE NT_ID = :id")
    fun get(id: Long): NoteItem

    fun get(context: Context, id: Long): NoteModel {
        val noteItem = get(id)
        val rollList = getRoll(id)

        val statusItem = StatusItem(context, noteItem, false)

        return NoteModel(noteItem, rollList, statusItem)
    }

    fun get(context: Context, bin: Boolean): MutableList<NoteModel> {
        val noteModelList = ArrayList<NoteModel>()
        val noteList = getNote(bin, Preference(context).sortNoteOrder)

        val rankVisibleList = rankVisible

        for (i in noteList.indices) {
            val noteItem = noteList[i]
            val rollList = getRollView(noteItem.id)
            val statusItem = StatusItem(context, noteItem, false)

            val noteModel = NoteModel(noteItem, rollList, statusItem)

            val rankIdList = noteItem.rankId
            if (rankIdList.size != 0 && !rankVisibleList.contains(rankIdList[0])) {
                statusItem.cancelNote()
            } else {
                if (noteItem.isStatus && NotesViewModel.updateStatus) {
                    statusItem.notifyNote()
                }

                noteModel.statusItem = statusItem
                noteModelList.add(noteModel)
            }
        }
        return noteModelList
    }

    @Query(value = "SELECT * FROM NOTE_TABLE " +
            "WHERE NT_BIN = :bin " +
            "ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC")
    fun get(bin: Boolean): MutableList<NoteItem>

    @Update
    fun update(noteItem: NoteItem)

    /**
     * Обновление элементов списка в статус баре
     *
     * @param context - Контекст для получения сортировки
     */
    fun update(context: Context) {
        val noteList = getNote(false, Preference(context).sortNoteOrder)
        val rankVisibleList = rankVisible

        for (i in noteList.indices) {
            val noteItem = noteList[i]
            val statusItem = StatusItem(context, noteItem, false)

            val rankIdList = noteItem.rankId
            if (rankIdList.size != 0 && !rankVisibleList.contains(rankIdList[0])) {
                statusItem.cancelNote()
            } else if (noteItem.isStatus) {
                statusItem.notifyNote()
            }
        }
    }

    @Query(value = "UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :change, NT_BIN = :bin " +
            "WHERE NT_ID = :id")
    fun update(id: Long, change: String, bin: Boolean)

    @Query(value = "UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :status " +
            "WHERE NT_ID = :id")
    fun update(id: Long, status: Boolean)

    @Delete
    fun delete(noteItem: NoteItem)

    @Delete
    fun delete(lisNote: List<NoteItem>)

    /**
     * Удаление заметки с чисткой категории
     *
     * @param id - Идентификатор заметки
     */
    fun delete(id: Long) {
        val noteItem = get(id)

        val rkId = noteItem.rankId
        if (rkId.size != 0) {
            clearRank(noteItem.id, rkId)
        }

        delete(noteItem)
    }

    fun clearBin() {
        val noteList = get(true)

        for (i in noteList.indices) {
            val noteItem = noteList[i]

            val rankIdList = noteItem.rankId
            if (rankIdList.size != 0) {
                clearRank(noteItem.id, rankIdList)
            }
        }

        delete(noteList)
    }

    fun listAll(textView: TextView) {
        val noteList = get(true)
        noteList.addAll(get(false))

        val annotation = "Note Data Base:"
        textView.text = annotation

        for (i in noteList.indices) {
            val noteItem = noteList[i]

            textView.append("\n\n" +
                    "ID: " + noteItem.id + " | " +
                    "CR: " + noteItem.create + " | " +
                    "CH: " + noteItem.change + "\n")

            val name = noteItem.name
            if (!TextUtils.isEmpty(name)) {
                textView.append("NM: $name\n")
            }

            val text = noteItem.text
            textView.append("TX: " + text.substring(0, Math.min(text.length, 45))
                    .replace("\n", " "))

            if (text.length > 40) {
                textView.append("...")
            }

            textView.append("\n")

            textView.append("CL: " + noteItem.color + " | " +
                    "TP: " + noteItem.type + " | " +
                    "BN: " + noteItem.isBin + "\n" +
                    "RK ID: " + TextUtils.join(", ", noteItem.rankId) + " | " +
                    "RK PS: " + TextUtils.join(", ", noteItem.rankPs) + "\n" +
                    "ST: " + noteItem.isStatus)
        }
    }

}