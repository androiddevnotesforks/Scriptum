package sgtmelon.scriptum.screen.view.note

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.view.callback.note.INoteActivity
import sgtmelon.scriptum.screen.view.callback.note.INoteChild
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Экран для отображения заметки - [TextNoteFragment], [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class NoteActivity : AppActivity(), INoteActivity, INoteChild {

    private val iViewModel: INoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java).apply {
            callback = this@NoteActivity
        }
    }

    private val noteReceiver by lazy { NoteReceiver(iViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        iViewModel.apply {
            onSetupData(bundle = savedInstanceState ?: intent.extras)
            onSetupFragment(isSave = savedInstanceState != null)
        }

        registerReceiver(noteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { iViewModel.onSaveData(bundle = this) })

    override fun onBackPressed() {
        if (!iViewModel.onPressBack()) super.onBackPressed()
    }

    override fun showTextFragment(id: Long, checkCache: Boolean) {
        showFragment(NoteType.TEXT.name, if (checkCache) {
            findTextNoteFragment() ?: TextNoteFragment.getInstance(id)
        } else {
            TextNoteFragment.getInstance(id)
        })
    }

    override fun showRollFragment(id: Long, checkCache: Boolean) {
        showFragment(NoteType.ROLL.name, if (checkCache) {
            findRollNoteFragment() ?: RollNoteFragment.getInstance(id)
        } else {
            RollNoteFragment.getInstance(id)
        })
    }

    private fun showFragment(key: String, fragment: Fragment) = supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.note_fragment_container, fragment, key)
            .commit()

    override fun onPressBackText() = findTextNoteFragment()?.onPressBack() ?: false

    override fun onPressBackRoll() = findRollNoteFragment()?.onPressBack() ?: false

    override fun onCancelNoteBind(type: NoteType) {
        when (type) {
            NoteType.TEXT -> findTextNoteFragment()?.onCancelNoteBind()
            NoteType.ROLL -> findRollNoteFragment()?.onCancelNoteBind()
        }
    }

    override fun onUpdateNoteId(id: Long) = iViewModel.onUpdateNoteId(id)

    override fun onConvertNote() = iViewModel.onConvertNote()

    companion object {
        fun getInstance(context: Context, type: NoteType, id: Long? = NoteData.Default.ID): Intent =
                Intent(context, NoteActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.TYPE, type.ordinal)

        fun Context.getNoteIntent(type: NoteType, id: Long? = NoteData.Default.ID): Intent =
                Intent(this, NoteActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.TYPE, type.ordinal)

        private fun AppActivity.findTextNoteFragment(): TextNoteFragment? =
                supportFragmentManager.findFragmentByTag(NoteType.TEXT.name) as? TextNoteFragment

        private fun AppActivity.findRollNoteFragment(): RollNoteFragment? =
                supportFragmentManager.findFragmentByTag(NoteType.ROLL.name) as? RollNoteFragment
    }

}