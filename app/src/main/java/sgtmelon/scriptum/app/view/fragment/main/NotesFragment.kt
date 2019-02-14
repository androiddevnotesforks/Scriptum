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
import sgtmelon.scriptum.office.annot.def.BinDef
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.intf.MenuIntf
import sgtmelon.scriptum.office.utils.ColorUtils
import sgtmelon.scriptum.office.utils.DialogUtils

class NotesFragment : Fragment(),
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener,
        MenuIntf.Dialog.NoteMenuClick {

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

    private lateinit var optionsDialog: OptionsDialog

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

        optionsDialog = DialogFactory.getOptionsDialog(fragmentManager)

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
            if (it.itemId == R.id.item_preference) {
                startActivity(Intent(context, PreferenceActivity::class.java))
                return@setOnMenuItemClickListener true
            } else {
                return@setOnMenuItemClickListener false
            }
        }

        ColorUtils.tintMenuIcon(activity, toolbar.menu.findItem(R.id.item_preference))
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

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, i ->
            val p = optionsDialog.position
            val noteItem = vm.getNoteItem(p)

            when (noteItem.type) {
                NoteType.TEXT -> when (i) {
                    OptionsDef.Notes.Text.bind -> onMenuBindClick(p)
                    OptionsDef.Notes.Text.convert -> onMenuConvertClick(p)
                    OptionsDef.Notes.Text.copy -> onMenuCopyClick(p)
                    OptionsDef.Notes.Text.delete -> onMenuDeleteClick(p)
                }
                NoteType.ROLL -> when (i) {
                    OptionsDef.Notes.Roll.check -> onMenuCheckClick(p)
                    OptionsDef.Notes.Roll.bind -> onMenuBindClick(p)
                    OptionsDef.Notes.Roll.convert -> onMenuConvertClick(p)
                    OptionsDef.Notes.Roll.copy -> onMenuCopyClick(p)
                    OptionsDef.Notes.Roll.delete -> onMenuDeleteClick(p)
                }
            }
        }
    }

    private fun updateAdapter() {
        adapter.setList(vm.loadData(BinDef.out))
        adapter.notifyDataSetChanged()

        bind()
    }

    fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION) return

        startActivity(NoteActivity.getIntent(activity, vm.getId(p)))
    }

    override fun onItemLongClick(view: View, p: Int): Boolean { // TODO (вынести в dialogFacroty - fillDialog)
        if (p == RecyclerView.NO_POSITION) return false

        optionsDialog.setArguments(DialogUtils.fillOptionsDialog(activity, vm.getNoteItem(p)), p)
        optionsDialog.show(fragmentManager, DialogDef.OPTIONS)

        return true
    }

    override fun onMenuCheckClick(p: Int) {
        adapter.setList(vm.onMenuCheck(p))
        adapter.notifyItemChanged(p)
    }

    override fun onMenuBindClick(p: Int) {
        adapter.setList(vm.onMenuBind(p))
        adapter.notifyItemChanged(p)
    }

    override fun onMenuConvertClick(p: Int) {
        adapter.setList(vm.onMenuConvert(p))
        adapter.notifyItemChanged(p)
    }

    override fun onMenuCopyClick(p: Int) = vm.onMenuCopy(p)

    override fun onMenuDeleteClick(p: Int) {
        adapter.setList(vm.onMenuDelete(p))
        adapter.notifyItemRemoved(p)
    }

}