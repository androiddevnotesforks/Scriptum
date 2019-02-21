package sgtmelon.scriptum.app.ui.main.notes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.activity.NoteActivity
import sgtmelon.scriptum.office.annot.def.CheckDef
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard
import sgtmelon.scriptum.office.utils.TimeUtils

/**
 * ViewModel для [NotesFragment]
 */
class NotesViewModel(application: Application) : AndroidViewModel(application),
        LifecycleObserver {

    private val context: Context = application.applicationContext

    lateinit var callback: NotesCallback

    private val listNoteRepo: MutableList<NoteRepo> = ArrayList()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLoadData() {
        val db = RoomDb.provideDb(context)
        listNoteRepo.clear()
        listNoteRepo.addAll(db.daoNote().get(context, false))
        db.close()

        callback.notifyDataSetChanged(listNoteRepo)
        callback.bind()

        if (updateStatus) updateStatus = false
    }

    fun onClickNote(p: Int) =
            callback.startNote(NoteActivity.getIntent(context, listNoteRepo[p].noteItem.id))

    fun onShowOptionsDialog(p: Int) {
        val noteItem = listNoteRepo[p].noteItem
        val itemArray: Array<String>

        when (noteItem.type) {
            NoteType.TEXT -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_text)

                itemArray[0] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }
            }
            NoteType.ROLL -> {
                itemArray = context.resources.getStringArray(R.array.dialog_menu_roll)

                itemArray[0] = when (noteItem.isAllCheck) {
                    true -> context.getString(R.string.dialog_menu_check_zero)
                    false -> context.getString(R.string.dialog_menu_check_all)
                }

                itemArray[1] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }

            }
        }

        callback.showOptionsDialog(itemArray, p)
    }

    fun onClickOptionsDialog(p: Int, which: Int) {
        val noteItem = listNoteRepo[p].noteItem

        when (noteItem.type) {
            NoteType.TEXT -> when (which) {
                OptionsDef.Text.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Text.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Text.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Text.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
            NoteType.ROLL -> when (which) {
                OptionsDef.Roll.check -> callback.notifyItemChanged(p, onMenuCheck(p))
                OptionsDef.Roll.bind -> callback.notifyItemChanged(p, onMenuBind(p))
                OptionsDef.Roll.convert -> callback.notifyItemChanged(p, onMenuConvert(p))
                OptionsDef.Roll.copy -> context.copyToClipboard(noteItem)
                OptionsDef.Roll.delete -> callback.notifyItemRemoved(p, onMenuDelete(p))
            }
        }
    }

    private fun onMenuCheck(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        val checkText = noteItem.check
        val check = when (checkText[0] == checkText[1]) {
            true -> CheckDef.notDone
            false -> CheckDef.done
        }

        noteItem.change = TimeUtils.getTime(context)
        noteItem.setText(when (check == CheckDef.notDone) {
            true -> 0
            false -> checkText[1]
        }, checkText[1])

        val db = RoomDb.provideDb(context)
        db.daoRoll().update(noteItem.id, check)
        db.daoNote().update(noteItem)
        db.close()

        noteRepo.updateCheck(check)
        noteRepo.statusItem.updateNote(noteItem, true)

        return listNoteRepo
    }

    private fun onMenuBind(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.isStatus = !noteItem.isStatus
        noteRepo.updateStatus(noteItem.isStatus)

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, noteItem.isStatus)
        db.close()

        return listNoteRepo
    }

    private fun onMenuConvert(p: Int): MutableList<NoteRepo> {
        val noteRepo = listNoteRepo[p]
        val noteItem = noteRepo.noteItem

        noteItem.change = TimeUtils.getTime(context)

        val db = RoomDb.provideDb(context)
        when (noteItem.type) {
            NoteType.TEXT -> {
                val listRoll = db.daoRoll().insert(noteItem.id, noteItem.text)

                noteItem.type = NoteType.ROLL
                noteItem.setText(0, listRoll.size)

                db.daoNote().update(noteItem)

                noteRepo.listRoll = listRoll
            }
            NoteType.ROLL -> {
                noteItem.type = NoteType.TEXT
                noteItem.text = db.daoRoll().getText(noteItem.id)

                db.daoNote().update(noteItem)
                db.daoRoll().delete(noteItem.id)

                noteRepo.listRoll = ArrayList()
            }
        }
        db.close()

        noteRepo.statusItem.updateNote(noteItem, true)

        return listNoteRepo
    }

    private fun onMenuDelete(p: Int): MutableList<NoteRepo> {
        val noteItem = listNoteRepo[p].noteItem

        val db = RoomDb.provideDb(context)
        db.daoNote().update(noteItem.id, TimeUtils.getTime(context), true)
        if (noteItem.isStatus) db.daoNote().update(noteItem.id, false)
        db.close()

        listNoteRepo[p].updateStatus(false)
        listNoteRepo.removeAt(p)

        return listNoteRepo
    }

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

}