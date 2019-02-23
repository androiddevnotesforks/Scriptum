package sgtmelon.scriptum.app.screen.note.text

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.AppUtils.manage
import sgtmelon.scriptum.widget.color.ColorDialog

class TextNoteFragmentNew : Fragment(), TextNoteCallback {

    private lateinit var activity: Activity

    private lateinit var binding: FragmentTextNoteBinding

    private val viewModel: TextNoteViewModel by lazy {
        ViewModelProviders.of(this).get(TextNoteViewModel::class.java)
    }

    private val convertDialog: MessageDialog by lazy {
        DialogFactory.getConvertDialog(activity, fragmentManager, NoteType.TEXT)
    }
    private val colorDialog: ColorDialog by lazy {
        DialogFactory.getColorDialog(fragmentManager)
    }
    private val rankDialog: MultiplyDialog by lazy {
        DialogFactory.getRankDialog(activity, fragmentManager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_text_note, container)

        viewModel.callback = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setupData(arguments ?: savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveData(outState)
    }

    private fun setupBinding() { // TODO
        binding.menuClick = viewModel
//        binding.rankEmpty = false
//        binding.rankSelect = false
    }

    private fun bindEdit(mode: Boolean, noteItem: NoteItem) {
        binding.keyEdit = mode
        binding.noteItem = noteItem

        binding.executePendingBindings()
    }

    companion object {
        fun getInstance(id: Long): TextNoteFragmentNew {
            val fragment = TextNoteFragmentNew()

            fragment.arguments = Bundle().manage { putLong(NoteData.Intent.ID, id) }

            return fragment
        }
    }

}