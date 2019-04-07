package sgtmelon.scriptum.app.screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.screen.callback.NoteCallback
import sgtmelon.scriptum.app.screen.vm.NoteViewModel

class NoteActivity : AppActivity(), NoteCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel.apply {
            callback = this@NoteActivity
            setupData(intent.extras ?: savedInstanceState)
            setupFragment(savedInstanceState != null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { viewModel.saveData(bundle = this) })

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

    override fun onPressBackText() =
            (supportFragmentManager.findFragmentByTag(NoteType.TEXT.name) as?
                    TextNoteFragment)?.onPressBack() ?: false

    override fun onPressBackRoll() =
            (supportFragmentManager.findFragmentByTag(NoteType.ROLL.name) as?
                    RollNoteFragment)?.onPressBack() ?: false

    override fun onBackPressed() {
        if (!viewModel.onPressBack()) super.onBackPressed()
    }

    companion object {
        fun Context.getNoteIntent(type: NoteType, id: Long? = NoteData.Default.ID): Intent {
            return Intent(this, NoteActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
                    .putExtra(NoteData.Intent.TYPE, type.ordinal)
        }
    }

}