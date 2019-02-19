package sgtmelon.scriptum.app.ui.main.notes

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import sgtmelon.safedialog.library.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.ui.main.MainCallback
import sgtmelon.scriptum.app.view.activity.PreferenceActivity
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon

class NotesFragment : Fragment(),
        NotesCallback,
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener {

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

    private lateinit var activity: Activity
    private lateinit var mainCallback: MainCallback

    private lateinit var binding: FragmentNotesBinding

    private val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }
    private val adapter: NoteAdapter by lazy {
        NoteAdapter(activity, clickListener = this, longClickListener = this)
    }

    private val recyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.notes_recycler)
    }

    private val optionsDialog: OptionsDialog by lazy {
        DialogFactory.getOptionsDialog(fragmentManager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
        mainCallback = context as MainCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = inflater.inflateBinding(R.layout.fragment_notes, container)

        viewModel.callback = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
        setupRecycler()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onLoadData()

        if (updateStatus) updateStatus = false
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_container)

        toolbar.title = getString(R.string.title_notes)
        toolbar.inflateMenu(R.menu.fragment_notes)
        toolbar.setOnMenuItemClickListener {
            startActivity(Intent(context, PreferenceActivity::class.java))
            return@setOnMenuItemClickListener true
        }

        toolbar.menu.findItem(R.id.item_preference).tintIcon(activity)
    }

    private fun setupRecycler() {
        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bind()
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mainCallback.changeFabState(dy <= 0)
            }
        })

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onClickDialog(optionsDialog.position, which)
        }
    }

    override fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun notifyDataSetChanged(list: MutableList<NoteRepo>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(list: MutableList<NoteRepo>, p: Int) =
            adapter.notifyItemChanged(list, p)

    override fun notifyItemRemoved(list: MutableList<NoteRepo>, p: Int) =
            adapter.notifyItemRemoved(list, p)

    override fun onItemClick(view: View, p: Int) = startActivity(viewModel.openNote(p))

    override fun onItemLongClick(view: View, p: Int) {
        optionsDialog.setArguments(viewModel.showOptions(p), p)
        optionsDialog.show(fragmentManager, DialogDef.OPTIONS)
    }

}