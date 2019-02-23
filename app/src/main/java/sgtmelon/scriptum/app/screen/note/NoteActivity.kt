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

    companion object {
        const val NOTE_ID = "INTENT_NOTE_ID"
        const val NOTE_TYPE = "INTENT_NOTE_TYPE"

        fun Context.getNoteIntent(type:NoteType, id: Long? = NoteViewModel.UNDEFINED_ID): Intent {
            return Intent(this, NoteActivity::class.java)
                    .putExtra(NOTE_ID, id)
                    .putExtra(NOTE_TYPE, type.ordinal)
        }
    }

}