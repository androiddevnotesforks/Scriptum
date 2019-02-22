package sgtmelon.scriptum.app.screen.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.screen.BaseActivityParent
import sgtmelon.scriptum.app.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.app.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.intf.MenuIntf
import sgtmelon.scriptum.office.utils.AppUtils.beforeFinish

// TODO: 11.02.2019 Передавать vm в биндинг и оттуда вызывать методы управления
// TODO: 11.02.2019 Если Id не существует то завершать активити

class NoteActivity : BaseActivityParent(), NoteCallback, MenuIntf.Note.DeleteMenuClick {

    private val vm by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    private val saveCtrl: SaveControl by lazy { SaveControl(this) }

    private var textNoteFragment: TextNoteFragment? = null
    private var rollNoteFragment: RollNoteFragment? = null

    override fun onPause() {
        super.onPause()

        saveCtrl.onPauseSave(vm.noteState.isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        vm.setValue(intent.extras ?: savedInstanceState)

        setupFragment(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IntentDef.NOTE_CREATE, vm.noteState.isCreate)
        outState.putInt(IntentDef.NOTE_TYPE, vm.noteType.ordinal)
        outState.putLong(IntentDef.NOTE_ID, vm.ntId)
    }

    override fun onBackPressed() { // TODO переделать
        saveCtrl.needSave = false

        val noteItem = vm.noteRepo.noteItem
        val noteSt = vm.noteState

        when (noteItem.type) {
            NoteType.TEXT -> {
                if (!textNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
                    if (noteSt.isEdit && !noteSt.isCreate) {
                        val colorFrom = noteItem.color

                        vm.resetFragmentData(noteItem.id)

                        textNoteFragment!!.viewModel.noteRepo = vm.noteRepo

                        textNoteFragment!!.startTintToolbar(colorFrom, vm.noteRepo.noteItem.color)
                        textNoteFragment!!.onMenuEditClick(false)
                    } else if (noteSt.isCreate) {
                        super.onBackPressed()
                    }
                } else {
                    super.onBackPressed()
                }
            }
            NoteType.ROLL -> {
                if (!rollNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
                    if (noteSt.isEdit && !noteSt.isCreate) {
                        val colorFrom = noteItem.color

                        vm.resetFragmentData(noteItem.id)

                        rollNoteFragment!!.viewModel.noteRepo = vm.noteRepo

                        rollNoteFragment!!.startTintToolbar(colorFrom, vm.noteRepo.noteItem.color)
                        rollNoteFragment!!.onMenuEditClick(false)
                        rollNoteFragment!!.updateAdapter()
                    } else if (noteSt.isCreate) {
                        super.onBackPressed()
                    }
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun setupFragment(isSave: Boolean) {
        if (!isSave) vm.noteState.isFirst = true

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        when (vm.noteRepo.noteItem.type) {
            NoteType.TEXT -> {
                textNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(NoteType.TEXT.name) as TextNoteFragment
                    false -> TextNoteFragment.getInstance(vm.isRankEmpty)
                }

                saveCtrl.noteMenuClick = textNoteFragment as MenuIntf.Note.NoteMenuClick

                transaction.replace(R.id.note_fragment_container, textNoteFragment!!, NoteType.TEXT.name)
            }
            NoteType.ROLL -> {
                rollNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(NoteType.ROLL.name) as RollNoteFragment
                    false -> RollNoteFragment.getInstance(vm.isRankEmpty)
                }

                saveCtrl.noteMenuClick = rollNoteFragment as MenuIntf.Note.NoteMenuClick

                transaction.replace(R.id.note_fragment_container, rollNoteFragment!!, NoteType.ROLL.name)
            }
        }

        transaction.commit()
    }

    override fun getSaveControl(): SaveControl = saveCtrl // TODO rename value

    override fun getViewModel(): NoteViewModel = vm

    override fun onMenuRestoreClick() = beforeFinish { vm.onMenuRestore() }

    override fun onMenuRestoreOpenClick() {
        vm.onMenuRestoreOpen()

        val noteRepo = vm.noteRepo
        when (noteRepo.noteItem.type) {
            NoteType.TEXT -> {
                textNoteFragment!!.viewModel.noteRepo = noteRepo
                textNoteFragment!!.onMenuEditClick(false)
            }
            NoteType.ROLL -> {
                rollNoteFragment!!.viewModel.noteRepo = noteRepo
                rollNoteFragment!!.onMenuEditClick(false)
            }
        }
    }

    override fun onMenuClearClick() = beforeFinish { vm.onMenuClear() }

    override fun onMenuDeleteClick() = beforeFinish { vm.onMenuDelete() }

    companion object {
        fun getIntent(context: Context, type: NoteType): Intent {
            val intent = Intent(context, NoteActivity::class.java)

            intent.putExtra(IntentDef.NOTE_CREATE, true)
            intent.putExtra(IntentDef.NOTE_TYPE, type.ordinal)

            return intent
        }

        fun getIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, NoteActivity::class.java)

            intent.putExtra(IntentDef.NOTE_CREATE, false)
            intent.putExtra(IntentDef.NOTE_ID, id)

            return intent
        }
    }

}