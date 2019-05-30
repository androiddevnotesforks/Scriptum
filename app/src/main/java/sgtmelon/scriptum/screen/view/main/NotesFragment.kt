package sgtmelon.scriptum.screen.view.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.createVisibleAnim
import sgtmelon.scriptum.office.utils.inflateBinding
import sgtmelon.scriptum.office.utils.tintIcon
import sgtmelon.scriptum.screen.callback.main.MainCallback
import sgtmelon.scriptum.screen.callback.main.NotesCallback
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.view.preference.PreferenceActivity
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Фрагмент для отображения списка заметок - [NoteItem]
 *
 * @author SerjantArbuz
 */
class NotesFragment : Fragment(), NotesCallback {

    private val mainCallback: MainCallback? by lazy { context as? MainCallback }

    private var binding: FragmentNotesBinding? = null

    private val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java).apply {
            callback = this@NotesFragment
        }
    }

    private val openState = OpenState()
    private val optionsDialog: OptionsDialog by lazy { DialogFactory.getOptionsDialog(fragmentManager) }

    private val adapter by lazy {
        NoteAdapter(
                ItemListener.ClickListener { _, p -> openState.tryInvoke { viewModel.onClickNote(p) } },
                ItemListener.LongClickListener { _, p -> viewModel.onShowOptionsDialog(p) }
        )
    }

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

        setupToolbar()
        setupRecycler()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        viewModel.onUpdateData()
    }

    private fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_container)?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)

            setOnMenuItemClickListener {
                openState.tryInvoke {
                    when (it.itemId) {
                        R.id.item_notification -> startActivity(NotificationActivity.getInstance(context))
                        R.id.item_preference -> startActivity(PreferenceActivity.getInstance(context))
                    }
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

    private fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.notes_parent_container)
        emptyInfoView = view?.findViewById(R.id.notes_info_include)

        recyclerView = view?.findViewById(R.id.notes_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bind()
            }

            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    mainCallback?.changeFabState(dy <= 0)
                }
            })
        }

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onResultOptionsDialog(optionsDialog.position, which)
        }
    }

    fun onCancelNoteBind(id: Long) = viewModel.onCancelNoteBind(id)

    override fun bind() {
        val empty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(empty, emptyInfoView, if (!empty) 0 else 200)

        binding?.apply { listEmpty = empty }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        fragmentManager?.let {
            optionsDialog.apply { setArguments(itemArray, p) }.show(it, DialogFactory.Key.OPTIONS)
        }
    }

    override fun notifyDataSetChanged(list: MutableList<NoteModel>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemChanged(p, list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemRemoved(p, list)

}