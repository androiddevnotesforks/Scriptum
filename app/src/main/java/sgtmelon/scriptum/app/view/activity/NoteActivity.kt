package sgtmelon.scriptum.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.control.SaveControl
import sgtmelon.scriptum.app.view.callback.NoteCallback
import sgtmelon.scriptum.app.view.fragment.RollNoteFragment
import sgtmelon.scriptum.app.view.fragment.TextNoteFragment
import sgtmelon.scriptum.app.view.parent.BaseActivityParent
import sgtmelon.scriptum.app.vm.activity.NoteActivityViewModel
import sgtmelon.scriptum.office.annot.def.FragmentDef
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.def.TypeNoteDef
import sgtmelon.scriptum.office.intf.MenuIntf

// TODO: 11.02.2019 Передавать vm в биндинг и оттуда вызывать методы управления
// TODO: 11.02.2019 Если Id не существует то завершать активити

class NoteActivity : BaseActivityParent(), NoteCallback, MenuIntf.Note.DeleteMenuClick {

    companion object {

        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, NoteActivity::class.java)

            intent.putExtra(IntentDef.NOTE_CREATE, true)
            intent.putExtra(IntentDef.NOTE_TYPE, type)

            return intent
        }

        fun getIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, NoteActivity::class.java)

            intent.putExtra(IntentDef.NOTE_CREATE, false)
            intent.putExtra(IntentDef.NOTE_ID, id)

            return intent
        }
    }

    private lateinit var vm: NoteActivityViewModel
    private lateinit var saveControl: SaveControl

    private var textNoteFragment: TextNoteFragment? = null
    private var rollNoteFragment: RollNoteFragment? = null

    override fun onPause() {
        super.onPause()

        saveControl.onPauseSave(vm.noteSt.isEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        vm = ViewModelProviders.of(this).get(NoteActivityViewModel::class.java)
        vm.setValue(intent.extras ?: savedInstanceState)

        saveControl = SaveControl(this)

        setupFragment(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IntentDef.NOTE_CREATE, vm.noteSt.isCreate)
        outState.putInt(IntentDef.NOTE_TYPE, vm.ntType)
        outState.putLong(IntentDef.NOTE_ID, vm.ntId)
    }

    override fun onBackPressed() {
        saveControl.needSave = false

        val noteItem = vm.noteRepo.noteItem
        val noteSt = vm.noteSt

        when (noteItem.type) {
            TypeNoteDef.text -> {
                if (!textNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
                    if (noteSt.isEdit && !noteSt.isCreate) {
                        val colorFrom = noteItem.color
                        val colorTo = vm.resetFragmentData(noteItem.id, textNoteFragment!!.viewModel)

                        textNoteFragment!!.startTintToolbar(colorFrom, colorTo)
                        textNoteFragment!!.onMenuEditClick(false)
                    } else if (noteSt.isCreate) {
                        super.onBackPressed()
                    }
                } else {
                    super.onBackPressed()
                }
            }
            TypeNoteDef.roll -> {
                if (!rollNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
                    if (noteSt.isEdit && !noteSt.isCreate) {
                        val colorFrom = noteItem.color
                        val colorTo = vm.resetFragmentData(noteItem.id, rollNoteFragment!!.viewModel)

                        rollNoteFragment!!.startTintToolbar(colorFrom, colorTo)
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
        if (!isSave) {
            val noteSt = vm.noteSt
            noteSt.isFirst = true
        }

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        when (vm.noteRepo.noteItem.type) {
            TypeNoteDef.text -> {
                textNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(FragmentDef.TEXT) as TextNoteFragment
                    false -> TextNoteFragment.getInstance(vm.isRankEmpty)
                }

                saveControl.noteMenuClick = textNoteFragment as MenuIntf.Note.NoteMenuClick

                transaction.replace(R.id.note_fragment_container, textNoteFragment!!, FragmentDef.TEXT)
            }
            TypeNoteDef.roll -> {
                rollNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(FragmentDef.ROLL) as RollNoteFragment
                    false -> RollNoteFragment.getInstance(vm.isRankEmpty)
                }

                saveControl.noteMenuClick = rollNoteFragment as MenuIntf.Note.NoteMenuClick

                transaction.replace(R.id.note_fragment_container, rollNoteFragment!!, FragmentDef.ROLL)
            }
        }

        transaction.commit()
    }

    override fun getSaveControl(): SaveControl = saveControl

    override fun getViewModel(): NoteActivityViewModel = vm

    override fun onMenuRestoreClick() {
        vm.onMenuRestore()
        finish()
    }

    override fun onMenuRestoreOpenClick() {
        vm.onMenuRestoreOpen()

        val noteRepo = vm.noteRepo
        when (noteRepo.noteItem.type) {
            TypeNoteDef.text -> {
                textNoteFragment!!.viewModel.noteRepo = noteRepo
                textNoteFragment!!.onMenuEditClick(false)
            }
            TypeNoteDef.roll -> {
                rollNoteFragment!!.viewModel.noteRepo = noteRepo
                rollNoteFragment!!.onMenuEditClick(false)
            }
        }
    }

    override fun onMenuClearClick() {
        vm.onMenuClear()
        finish()
    }

    override fun onMenuDeleteClick() {
        vm.onMenuDelete()
        finish()
    }

}