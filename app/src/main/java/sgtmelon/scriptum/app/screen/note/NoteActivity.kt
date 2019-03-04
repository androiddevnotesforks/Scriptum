package sgtmelon.scriptum.app.screen.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.app.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData

class NoteActivity : ParentActivity(), NoteCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel.callback = this
        viewModel.setupData(intent.extras ?: savedInstanceState)
        viewModel.setupFragment(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveData(outState)
    }

    override fun showTextFragment(id: Long, isSave: Boolean) {
        showFragment(NoteType.TEXT.name, when (isSave) {
            true -> supportFragmentManager.findFragmentByTag(NoteType.TEXT.name) as TextNoteFragment
            false -> TextNoteFragment.getInstance(id)
        })
    }

    override fun showRollFragment(id: Long, isSave: Boolean) {
        showFragment(NoteType.ROLL.name, when (isSave) {
            true -> supportFragmentManager.findFragmentByTag(NoteType.ROLL.name) as RollNoteFragment
            false -> RollNoteFragment.getInstance(id)
        })
    }

    private fun showFragment(key: String, fragment: Fragment) = supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.note_fragment_container, fragment, key)
            .commit()

    override fun trySaveTextFragment() =
            (supportFragmentManager.findFragmentByTag(NoteType.TEXT.name) as?
                    TextNoteFragment)?.trySave() ?: false

    override fun trySaveRollFragment() =
            (supportFragmentManager.findFragmentByTag(NoteType.ROLL.name) as?
                    RollNoteFragment)?.trySave() ?: false

    override fun onBackPressed() {
        if (!viewModel.trySave()) super.onBackPressed()
    }

    companion object {
        fun Context.getNoteIntent(type: NoteType, id: Long? = NoteData.Default.ID): Intent {
            return Intent(this, NoteActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
                    .putExtra(NoteData.Intent.TYPE, type.ordinal)
        }
    }

    // todo backClick

    /*
    //@Override
    //    public void onBackPressed() {
    //        Log.i(TAG, "onBackPressed");
    //
    //        saveControl.setNeedSave(false);
    //
    //        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();
    //        final NoteSt noteSt = vm.getNoteSt();
    //
    //        if (noteSt.isEdit() && !noteSt.isCreate()) {                  //Если это редактирование и не только что созданная заметка
    //            switch (noteItem.getType()) {
    //                case TypeNoteDef.text:
    //                    if (!textFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
    //                        final int colorFrom = noteItem.getColor();
    //                        final int colorTo = vm.resetFragmentData(
    //                                noteItem.getId(), textFragment.getViewModel()
    //                        );
    //
    //                        textFragment.startTintToolbar(colorFrom, colorTo);
    //                        textFragment.onMenuEditClick(false);
    //                    }
    //                    break;
    //                case TypeNoteDef.roll:
    //                    if (!rollFragment.onMenuSaveClick(true, false)) {   //Если сохранение не выполнено, возвращает старое
    //                        final int colorFrom = noteItem.getColor();
    //                        final int colorTo = vm.resetFragmentData(
    //                                noteItem.getId(), rollFragment.getViewModel()
    //                        );
    //
    //                        rollFragment.startTintToolbar(colorFrom, colorTo);
    //                        rollFragment.onMenuEditClick(false);
    //                        rollFragment.updateAdapter();
    //                    }
    //                    break;
    //            }
    //        } else if (noteSt.isCreate()) {     //Если только что создали заметку
    //            switch (noteItem.getType()) {   //Если сохранение не выполнено, выход без сохранения
    //                case TypeNoteDef.text:
    //                    if (!textFragment.onMenuSaveClick(true, false)) {
    //                        super.onBackPressed();
    //                    }
    //                    break;
    //                case TypeNoteDef.roll:
    //                    if (!rollFragment.onMenuSaveClick(true, false)) {
    //                        super.onBackPressed();
    //                    }
    //                    break;
    //            }
    //        } else {
    //            super.onBackPressed();   //Другие случаи (не редактирование)
    //        }
    //    }
    */

    // saveCtrl.needSave = false
    //
    //        val noteItem = vm.noteRepo.noteItem
    //        val noteSt = vm.noteState
    //
    //        when (noteItem.type) {
    //            NoteType.TEXT -> {
    //                if (!textNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
    //                    if (noteSt.isEdit && !noteSt.isCreate) {
    //                        val colorFrom = noteItem.color
    //                        val colorTo = vm.resetFragmentData(noteItem.id, textNoteFragment!!.viewModel)
    //
    //                        textNoteFragment!!.startTintToolbar(colorFrom, colorTo)
    //                        textNoteFragment!!.onMenuEditClick(false)
    //                    } else if (noteSt.isCreate) {
    //                        super.onBackPressed()
    //                    }
    //                } else {
    //                    super.onBackPressed()
    //                }
    //            }
    //            NoteType.ROLL -> {
    //                if (!rollNoteFragment!!.onMenuSaveClick(modeChange = true, showToast = false)) {
    //                    if (noteSt.isEdit && !noteSt.isCreate) {
    //                        val colorFrom = noteItem.color
    //                        val colorTo = vm.resetFragmentData(noteItem.id, rollNoteFragment!!.viewModel)
    //
    //                        rollNoteFragment!!.startTintToolbar(colorFrom, colorTo)
    //                        rollNoteFragment!!.onMenuEditClick(false)
    //                        rollNoteFragment!!.updateAdapter()
    //                    } else if (noteSt.isCreate) {
    //                        super.onBackPressed()
    //                    }
    //                } else {
    //                    super.onBackPressed()
    //                }
    //            }
    //        }

}