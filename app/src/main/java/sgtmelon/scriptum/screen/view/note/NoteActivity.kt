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
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.key.ReceiverKey
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.callback.note.NoteCallback
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

/**
 * Экран для отображения заметки - [TextNoteFragment], [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class NoteActivity : AppActivity(), NoteCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java).apply {
            callback = this@NoteActivity
        }
    }

    private val noteReceiver by lazy { NoteReceiver(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel.apply {
            onSetupData(intent.extras ?: savedInstanceState)
            onSetupFragment(savedInstanceState != null)
        }

        registerReceiver(noteReceiver, IntentFilter(ReceiverKey.Filter.NOTE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { viewModel.onSaveData(bundle = this) })

    override fun onBackPressed() {
        if (!viewModel.onPressBack()) super.onBackPressed()
    }

    override fun showTextFragment(id: Long, isSave: Boolean) {
        showFragment(NoteType.TEXT.name, if (isSave) {
            findTextNoteFragment() ?: TextNoteFragment.getInstance(id)
        } else {
            TextNoteFragment.getInstance(id)
        })
    }

    override fun showRollFragment(id: Long, isSave: Boolean) {
        showFragment(NoteType.ROLL.name, if (isSave) {
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

    // TODO не вызывается сброс уведомления после конвертирования заметки (findFragment == null)

    override fun onCancelNoteBind(type: NoteType) {
        when (type) {
            NoteType.TEXT -> findTextNoteFragment()?.onCancelNoteBind()
            NoteType.ROLL -> findRollNoteFragment()?.onCancelNoteBind()
        }
    }

    companion object {
        fun Context.getNoteIntent(type: NoteType, id: Long? = NoteData.Default.ID): Intent {
            return Intent(this, NoteActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
                    .putExtra(NoteData.Intent.TYPE, type.ordinal)
        }

        private fun AppActivity.findTextNoteFragment(): TextNoteFragment? =
                supportFragmentManager.findFragmentByTag(NoteType.TEXT.name) as? TextNoteFragment

        private fun AppActivity.findRollNoteFragment(): RollNoteFragment? =
                supportFragmentManager.findFragmentByTag(NoteType.ROLL.name) as? RollNoteFragment
    }

}