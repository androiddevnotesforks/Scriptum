package sgtmelon.scriptum.app.screen.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.app.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.key.NoteType

// TODO: 11.02.2019 Передавать vm в биндинг и оттуда вызывать методы управления
// TODO: 11.02.2019 Если Id не существует то завершать активити

class NoteActivity : ParentActivity(), NoteCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    private var textNoteFragment: TextNoteFragment? = null
    private var rollNoteFragment: RollNoteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel.setValue(intent.extras ?: savedInstanceState)

        setupFragment(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IntentDef.NOTE_CREATE, viewModel.noteState.isCreate)
        outState.putInt(IntentDef.NOTE_TYPE, viewModel.noteType.ordinal)
        outState.putLong(IntentDef.NOTE_ID, viewModel.ntId)
    }

    override fun setupFragment(isSave: Boolean) {
        if (!isSave) viewModel.noteState.isFirst = true

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        when (viewModel.noteRepo.noteItem.type) {
            NoteType.TEXT -> {
                textNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(NoteType.TEXT.name) as TextNoteFragment
                    false -> TextNoteFragment.getInstance(viewModel.isRankEmpty)
                }

                transaction.replace(R.id.note_fragment_container, textNoteFragment!!, NoteType.TEXT.name)
            }
            NoteType.ROLL -> {
                rollNoteFragment = when (isSave) {
                    true -> fm.findFragmentByTag(NoteType.ROLL.name) as RollNoteFragment
                    false -> RollNoteFragment.getInstance(viewModel.isRankEmpty)
                }

                transaction.replace(R.id.note_fragment_container, rollNoteFragment!!, NoteType.ROLL.name)
            }
        }

        transaction.commit()
    }

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