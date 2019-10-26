package sgtmelon.scriptum.screen.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.control.clipboard.ClipboardControl
import sgtmelon.scriptum.control.clipboard.IClipboardControl
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Options
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.ParentFragment
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceActivity
import java.util.*

/**
 * Fragment which displays list of notes - [NoteEntity]
 */
class NotesFragment : ParentFragment(), INotesFragment {

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentNotesBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getNotesViewModel(fragment = this) }

    private val iAlarmControl by lazy { AlarmControl[context] }
    private val iBindControl: IBindControl by lazy { BindControl(context) }
    private val iClipboardControl: IClipboardControl by lazy { ClipboardControl(context) }

    private val openState get() = callback?.openState
    private val dialogFactory by lazy { DialogFactory.Main(context, fm) }

    private val optionsDialog by lazy { dialogFactory.getOptionsDialog() }
    private val dateDialog by lazy { dialogFactory.getDateDialog() }
    private val timeDialog by lazy { dialogFactory.getTimeDialog() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) {
                openState?.tryInvoke { iViewModel.onClickNote(p) }
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) = iViewModel.onShowOptionsDialog(p)
        })
    }

    /**
     * Setup variable that way because onRotate can happen shit and view is gone.
     */
    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_notes, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iAlarmControl.initLazy()
        iBindControl.initLazy()
        iClipboardControl.initLazy()

        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        iViewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    //region Receiver functions

    fun onCancelNoteBind(id: Long) = iViewModel.onCancelNoteBind(id)

    fun onUpdateAlarm(id: Long) = iViewModel.onUpdateAlarm(id)

    //endregion

    override fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_container)?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)

            setOnMenuItemClickListener {
                openState?.tryInvoke {
                    startActivity(when (it.itemId) {
                        R.id.item_notification -> NotificationActivity[context]
                        else -> PreferenceActivity[context]
                    })
                }

                return@setOnMenuItemClickListener true
            }

            activity?.let {
                menu?.apply {
                    findItem(R.id.item_notification)?.tintIcon(it)
                    findItem(R.id.item_preference)?.tintIcon(it)
                }
            }
        }
    }

    override fun setupRecycler(@Theme theme: Int) {
        parentContainer = view?.findViewById(R.id.notes_parent_container)
        emptyInfoView = view?.findViewById(R.id.notes_info_include)

        adapter.theme = theme

        recyclerView = view?.findViewById(R.id.notes_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bindList()
            }

            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    callback?.changeFabState(state = dy <= 0)
                }
            })
        }
    }

    override fun setupDialog() {
        optionsDialog.apply {
            itemListener = DialogInterface.OnClickListener { _, which ->
                if (which == Options.Notes.NOTIFICATION) openState?.skipClear = true

                iViewModel.onResultOptionsDialog(optionsDialog.position, which)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }

        dateDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                openState?.skipClear = true
                iViewModel.onResultDateDialog(dateDialog.calendar, dateDialog.position)
            }
            neutralListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultDateDialogClear(dateDialog.position)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }

        timeDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultTimeDialog(timeDialog.calendar, timeDialog.position)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }

    override fun setupBinding(isListHide: Boolean) {
        binding?.isListHide = isListHide
    }

    override fun bindList() {
        val isListEmpty = adapter.itemCount == 0

        /**
         * Use time equal 0
         *
         * Because you on another screen and restore item to that screen, after return you will
         * cause [bindList]. Zero time need for best performance, without freeze
         */
        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty, if (!isListEmpty) 0 else 200)

        binding?.apply { this.isListEmpty = isListEmpty }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun startNoteActivity(noteEntity: NoteEntity) {
        startActivity(NoteActivity[context ?: return, noteEntity])
    }


    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        openState?.tryInvoke {
            openState?.tag = OpenState.TAG_OPTIONS

            optionsDialog.setArguments(itemArray, p).show(fm, DialogFactory.Main.OPTIONS)
        }
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int) {
        openState?.tryInvoke(OpenState.TAG_OPTIONS) {
            dateDialog.setArguments(calendar, resetVisible, p).show(fm, DialogFactory.Main.DATE)
        }
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int) {
        openState?.tryInvoke(OpenState.TAG_OPTIONS) {
            activity?.hideKeyboard()
            timeDialog.setArguments(calendar, dateList, p).show(fm, DialogFactory.Main.TIME)
        }
    }


    override fun notifyDataSetChanged(list: MutableList<NoteModel>) {
        adapter.notifyDataSetChanged(list)
    }

    override fun notifyItemChanged(p: Int, list: MutableList<NoteModel>) {
        adapter.notifyItemChanged(p, list)
    }

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>) {
        adapter.notifyItemRemoved(p, list)
    }


    override fun cancelAlarm(id: Long) = iAlarmControl.cancel(id)

    override fun setAlarm(calendar: Calendar, id: Long) {
        iAlarmControl.set(calendar, id)
    }

    override fun notifyNoteBind(noteModel: NoteModel, rankIdVisibleList: List<Long>) {
        iBindControl.notifyNote(noteModel, rankIdVisibleList)
    }

    override fun cancelNoteBind(id: Int) = iBindControl.cancelNote(id)

    override fun notifyInfoBind(count: Int) = iBindControl.notifyInfo(count)

    override fun copyClipboard(text: String) = iClipboardControl.copy(text)

}