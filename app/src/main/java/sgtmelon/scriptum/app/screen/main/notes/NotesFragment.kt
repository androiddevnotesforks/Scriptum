package sgtmelon.scriptum.app.screen.main.notes

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
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.screen.main.MainCallback
import sgtmelon.scriptum.app.screen.pref.PrefActivity
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon

class NotesFragment : Fragment(), NotesCallback {

    private lateinit var activity: Activity
    private lateinit var mainCallback: MainCallback

    private lateinit var binding: FragmentNotesBinding

    private val viewModel: NotesViewModel by lazy {
        ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }
    private val adapter: NoteAdapter by lazy {
        NoteAdapter(activity)
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

    override fun onResume() {
        super.onResume()

        viewModel.onUpdateData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = inflater.inflateBinding(R.layout.fragment_notes, container)

        viewModel.callback = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecycler()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_container)

        toolbar?.title = getString(R.string.title_notes)
        toolbar?.inflateMenu(R.menu.fragment_notes)
        toolbar?.setOnMenuItemClickListener {
            startActivity(Intent(context, PrefActivity::class.java))
            return@setOnMenuItemClickListener true
        }

        toolbar?.menu?.findItem(R.id.item_preference)?.tintIcon(activity)
    }

    private fun setupRecycler() {
        adapter.clickListener = ItemListener.ClickListener { _, p -> viewModel.onClickNote(p) }
        adapter.longClickListener = ItemListener.LongClickListener { _, p -> viewModel.onShowOptionsDialog(p) }

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
            viewModel.onResultOptionsDialog(optionsDialog.position, which)
        }
    }

    override fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun startNote(intent: Intent) = startActivity(intent)

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        optionsDialog.setArguments(itemArray, p)
        optionsDialog.show(fragmentManager, DialogDef.OPTIONS)
    }

    override fun notifyDataSetChanged(list: MutableList<NoteModel>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemChanged(p, list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemRemoved(p, list)

}