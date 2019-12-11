package sgtmelon.scriptum.screen.ui.note

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment]
 */
class NoteActivity : AppActivity(), INoteActivity, INoteChild {

    private val iViewModel by lazy { ViewModelFactory.getNoteViewModel(activity = this) }

    private val fragmentFactory = FragmentFactory.Note(fm)
    private val textNoteFragment get() = fragmentFactory.getTextNoteFragment()
    private val rollNoteFragment get() = fragmentFactory.getRollNoteFragment()

    private val noteReceiver by lazy { NoteReceiver(iViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        iViewModel.apply {
            onSetup(bundle = savedInstanceState ?: intent.extras)
            onSetupFragment(checkCache = savedInstanceState != null)
        }

        registerReceiver(noteReceiver, IntentFilter(ReceiverData.Filter.NOTE))
    }

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        unregisterReceiver(noteReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply { iViewModel.onSaveData(bundle = this) })
    }

    override fun onBackPressed() {
        if (!iViewModel.onPressBack()) super.onBackPressed()
    }

    override fun showTextFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.TEXT, if (checkCache) {
            textNoteFragment ?: TextNoteFragment[id, color]
        } else {
            TextNoteFragment[id, color]
        })
    }

    override fun showRollFragment(id: Long, @Color color: Int, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.ROLL, if (checkCache) {
            rollNoteFragment ?: RollNoteFragment[id, color]
        } else {
            RollNoteFragment[id, color]
        })
    }

    override fun onPressBackText() = textNoteFragment?.onPressBack() ?: false

    override fun onPressBackRoll() = rollNoteFragment?.onPressBack() ?: false

    override fun onCancelNoteBind(type: NoteType) {
        when (type) {
            NoteType.TEXT -> textNoteFragment?.onCancelNoteBind()
            NoteType.ROLL -> rollNoteFragment?.onCancelNoteBind()
        }
    }

    override fun onUpdateNoteId(id: Long) = iViewModel.onUpdateNoteId(id)

    override fun onUpdateNoteColor(@Color color: Int) = iViewModel.onUpdateNoteColor(color)

    override fun onConvertNote() = iViewModel.onConvertNote()


    private fun showFragment(key: String, fragment: Fragment) = fm.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.note_fragment_container, fragment, key)
            .commit()

    companion object {
        operator fun get(context: Context, item: NotificationItem) =
                get(context, item.note.type, item.note.id, item.note.color)

        operator fun get(context: Context, item: NoteItem) =
                get(context, item.type, item.id, item.color)

        /**
         * If [id] and [color] isDefault - it means that note will be create, not open.
         */
        operator fun get(context: Context, type: NoteType,
                         id: Long = NoteData.Default.ID,
                         @Color color: Int = NoteData.Default.COLOR): Intent {
            return Intent(context, NoteActivity::class.java)
                    .putExtra(NoteData.Intent.ID, id)
                    .putExtra(NoteData.Intent.COLOR, color)
                    .putExtra(NoteData.Intent.TYPE, type.ordinal)
        }
    }

}