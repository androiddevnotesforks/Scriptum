package sgtmelon.scriptum.infrastructure.adapter.holder

import android.annotation.SuppressLint
import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.annotation.IntRange
import java.lang.Integer.min
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.data.noteHistory.NoteHistoryEnableControl
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.DragTouchListener
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorNextAction
import sgtmelon.scriptum.infrastructure.utils.extensions.setSelectionSafe

/**
 * Holder of roll item in edit state.
 */
@SuppressLint("ClickableViewAccessibility")
class RollWriteHolder(
    private val binding: ItemRollWriteBinding,
    dragListener: ItemDragListener,
    private val callback: Callback,
    private val onEnterNext: () -> Unit
) : ParentHolder(binding.root),
    UnbindCallback,
    HistoryTextWatcher.Callback {

    private val textWatcher = HistoryTextWatcher(
        binding.textEnter, callback = this
    ) { value, cursor ->
        checkPosition {
            val position = callback.getAbsolutePosition(it) ?: return@checkPosition
            return@HistoryTextWatcher HistoryAction.Roll.Enter(position, value, cursor)
        }

        return@HistoryTextWatcher null
    }

    init {
        val touchListener = DragTouchListener(dragListener, binding.dragButton)

        binding.textEnter.apply {
            setRawInputType(
                InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_FULLSCREEN

            setEditorNextAction { onEnterNext() }
            addTextChangedListener(textWatcher)
            setOnTouchListener(touchListener)
        }

        binding.dragButton.setOnTouchListener(touchListener)
    }

    fun bind(item: RollItem) = callback.disableHistoryChanges {
        with(binding) {
            dragButton.bindBoolTint(item.isCheck, R.attr.clAccent, R.attr.clContent)
            bindContentDescription(item.text)
            textEnter.setText(item.text)
            textEnter.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContrast)
        }
    }

    private fun bindContentDescription(text: String) {
        val description = context.getString(R.string.description_item_roll_move, text)
        binding.dragButton.contentDescription = description
    }

    fun setSelections(position: Int) = binding.textEnter.setSelectionSafe(position)

    override fun onHistoryAdd(action: HistoryAction) = callback.onRollHistoryAdd(action)

    override fun onHistoryEnterChanged(text: String) {
        checkPosition { callback.onRollEnterChanged(it, text) }
        bindContentDescription(text)
    }

    override fun unbind() {
        binding.textEnter.setOnTouchListener(null)
        binding.textEnter.removeTextChangedListener(textWatcher)
        binding.dragButton.setOnTouchListener(null)
    }

    interface Callback : NoteHistoryEnableControl {
        fun getAbsolutePosition(adapterPosition: Int): Int?
        fun onRollHistoryAdd(action: HistoryAction)
        fun onRollEnterChanged(p: Int, text: String)
    }
}