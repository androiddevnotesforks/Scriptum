package sgtmelon.scriptum.app.screen.note.roll

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.AppUtils.manage

/**
 * Фрагмент для отображения заметки списка
 */
class RollNoteFragmentNew : Fragment(), RollNoteCallback {

    private lateinit var activity: Activity
    private lateinit var noteCallback: NoteCallback

    private lateinit var binding: FragmentRollNoteBinding

    private val viewModel: RollNoteViewModel by lazy {
        ViewModelProviders.of(this).get(RollNoteViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
        noteCallback = context as NoteCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_text_note, container)

        viewModel.callback = this
        viewModel.noteCallback = noteCallback

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


    /**
     *
     */


    /**
     *
     */


    /**
     *
     */

    companion object {
        private operator fun invoke(func: RollNoteFragmentNew.() -> Unit) =
                RollNoteFragmentNew().apply { func() }

        fun getInstance(id: Long) = RollNoteFragmentNew {
            arguments = Bundle().manage { putLong(NoteData.Intent.ID, id) }
        }
    }

}