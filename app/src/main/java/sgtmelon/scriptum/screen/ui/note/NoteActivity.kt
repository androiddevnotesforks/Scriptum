package sgtmelon.scriptum.screen.ui.note

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild

/**
 * Screen which display note - [TextNoteFragment], [RollNoteFragment]
 */
class NoteActivity : AppActivity(), INoteActivity, INoteChild {

    private val iViewModel by lazy { ViewModelFactory.getNoteViewModel(activity = this) }

    private val fragmentFactory = FragmentFactory.Note(supportFragmentManager)
    private val textNoteFragment get() = fragmentFactory.getTextNoteFragment()
    private val rollNoteFragment get() = fragmentFactory.getRollNoteFragment()

    private val noteReceiver by lazy { NoteReceiver(iViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        iViewModel.apply {
            onSetup(bundle = savedInstanceState ?: intent.extras)
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
        showFragment(FragmentFactory.Note.TEXT, if (checkCache) {
            textNoteFragment ?: TextNoteFragment[id]
        } else {
            TextNoteFragment[id]
        })
    }

    override fun showRollFragment(id: Long, checkCache: Boolean) {
        showFragment(FragmentFactory.Note.ROLL, if (checkCache) {
            rollNoteFragment ?: RollNoteFragment[id]
        } else {
            RollNoteFragment[id]
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

    override fun onConvertNote() = iViewModel.onConvertNote()


    private fun showFragment(key: String, fragment: Fragment) = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.note_fragment_container, fragment, key)
            .commit()

    companion object {
        operator fun get(context: Context, notificationItem: NotificationItem) =
                get(context, notificationItem.note.type, notificationItem.note.id)

        operator fun get(context: Context, noteEntity: NoteEntity) =
                get(context, noteEntity.type, noteEntity.id)

        operator fun get(context: Context, type: NoteType, id: Long? = NoteData.Default.ID): Intent =
                Intent(context, NoteActivity::class.java)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.TYPE, type.ordinal)
    }

}