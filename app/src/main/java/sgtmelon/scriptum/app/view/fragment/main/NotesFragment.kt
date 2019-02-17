package sgtmelon.scriptum.app.view.fragment.main

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.safedialog.library.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.view.activity.NoteActivity
import sgtmelon.scriptum.app.view.activity.PreferenceActivity
import sgtmelon.scriptum.app.view.callback.MainCallback
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon
import sgtmelon.scriptum.office.utils.DialogUtils.setNotesArguments
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard

class NotesFragment : Fragment(),
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener,
        DialogInterface.OnClickListener {

    companion object {
        /**
         * Для единовременного обновления статус бара
         */
        var updateStatus = true
    }

    private lateinit var activity: Activity
    private lateinit var mainCallback: MainCallback

    private lateinit var binding: FragmentNotesBinding

    private val vm: NotesViewModel by lazy {
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

    override fun onResume() {
        super.onResume()

        updateAdapter()

        if (updateStatus) updateStatus = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_notes, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
        setupRecycler()
    }

    private fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
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

        optionsDialog.onClickListener = this
    }

    private fun updateAdapter() {
        adapter.notifyDataSetChanged(vm.loadData())
        bind()
    }

    fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION) return

        startActivity(NoteActivity.getIntent(activity, vm.getNoteItem(p).id))
    }

    override fun onItemLongClick(view: View, p: Int): Boolean {
        if (p == RecyclerView.NO_POSITION) return false

        optionsDialog.setNotesArguments(activity, vm.getNoteItem(p), p)
        optionsDialog.show(fragmentManager, DialogDef.OPTIONS)

        return true
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val p = optionsDialog.position
        val noteItem = vm.getNoteItem(p)

        when (noteItem.type) {
            NoteType.TEXT -> when (which) {
                OptionsDef.Text.bind -> adapter.notifyItemChanged(vm.onMenuBind(p), p)
                OptionsDef.Text.convert -> adapter.notifyItemChanged(vm.onMenuConvert(p), p)
                OptionsDef.Text.copy -> activity.copyToClipboard(noteItem)
                OptionsDef.Text.delete -> adapter.notifyItemRemoved(vm.onMenuDelete(p), p)
            }
            NoteType.ROLL -> when (which) {
                OptionsDef.Roll.check -> adapter.notifyItemChanged(vm.onMenuCheck(p), p)
                OptionsDef.Roll.bind -> adapter.notifyItemChanged(vm.onMenuBind(p), p)
                OptionsDef.Roll.convert -> adapter.notifyItemChanged(vm.onMenuConvert(p), p)
                OptionsDef.Roll.copy -> activity.copyToClipboard(noteItem)
                OptionsDef.Roll.delete -> adapter.notifyItemRemoved(vm.onMenuDelete(p), p)
            }
        }
    }

}