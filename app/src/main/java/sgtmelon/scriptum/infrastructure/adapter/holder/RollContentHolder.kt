package sgtmelon.scriptum.infrastructure.adapter.holder

import android.annotation.SuppressLint
import android.text.InputType
import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindDrawable
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.data.noteHistory.NoteHistoryEnableControl
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.databinding.ItemRollContentBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.DragTouchListener
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.clearText
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorNextAction
import sgtmelon.scriptum.infrastructure.utils.extensions.setSelectionSafe
import sgtmelon.scriptum.infrastructure.utils.extensions.setTextIfDifferent

@SuppressLint("ClickableViewAccessibility")
class RollContentHolder(
    private val binding: ItemRollContentBinding,
    private val readCallback: ReadCallback,
    private val writeCallback: WriteCallback,
    private val dragListener: ItemDragListener,
    private val onEnterNext: () -> Unit
) : ParentHolder(binding.root),
    UnbindCallback {

    private val touchListener = DragTouchListener(dragListener, binding.clickButton)

    private val textWatcher = HistoryTextWatcher(
        binding.itemEnter, callback = object : HistoryTextWatcher.Callback {
            override fun onHistoryAdd(action: HistoryAction) {
                writeCallback.onRollHistoryAdd(action)
            }

            override fun onHistoryEnterChanged(text: String) {
                checkPosition { writeCallback.onRollEnterChanged(it, text) }
                bindWriteDescription(text)
            }
        }
    ) { value, cursor ->
        checkPosition {
            val position = writeCallback.getAbsolutePosition(it) ?: return@checkPosition
            return@HistoryTextWatcher HistoryAction.Roll.Enter(position, value, cursor)
        }

        return@HistoryTextWatcher null
    }

    fun bindSelection(cursor: Int) = binding.itemEnter.setSelectionSafe(cursor)

    fun bind(isEdit: Boolean, state: NoteState, item: RollItem) {
        /** Remove all listeners what was set before. */
        unbind()

        with(binding) {
            checkBox.isChecked = item.isCheck
            checkBox.makeVisibleIf(!isEdit) { makeInvisible() }

            bindClickButton(isEdit, state, item)

            itemText.makeVisibleIf(!isEdit)
            itemEnter.makeVisibleIf(isEdit)

            if (isEdit) {
                itemText.text = ""

                /** Important to REMOVE listener before setting text and ADD watcher after it. */
                itemEnter.removeTextChangedListener(textWatcher)
                writeCallback.disableHistoryChanges {
                    itemEnter.setTextIfDifferent(item.text)
                    itemEnter.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContentThird)
                }
                itemEnter.addTextChangedListener(textWatcher)

                with(itemEnter) {
                    setRawInputType(
                        InputType.TYPE_CLASS_TEXT
                                or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                                or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                                or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                    )
                    imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_FULLSCREEN

                    setEditorNextAction { onEnterNext() }
                    setOnTouchListener(touchListener)
                }
            } else {
                itemEnter.clearText()

                itemText.text = item.text
                itemText.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContentThird)
            }
        }
    }

    private fun bindClickButton(isEdit: Boolean, state: NoteState, item: RollItem) = with(binding) {
        clickButton.isEnabled = state != NoteState.DELETE

        if (isEdit) {
            val colorAttr = if (item.isCheck) R.attr.clAccent else R.attr.clContent
            clickButton.bindDrawable(R.drawable.ic_move, colorAttr)
            clickButton.setOnTouchListener(touchListener)

            bindWriteDescription(item.text)
        } else {
            clickButton.setImageDrawable(null)

            val description = if (item.isCheck) {
                context.getString(R.string.description_item_roll_uncheck, item.text)
            } else {
                context.getString(R.string.description_item_roll_check, item.text)
            }
            clickButton.contentDescription = description

            val animTime = context.resources.getInteger(R.integer.icon_animation_time).toLong()
            clickButton.setOnClickListener { _ ->
                checkPosition {
                    readCallback.onRollChangeCheck(it, animTime) { checkBox.toggle() }
                }
            }
        }
    }

    private fun bindWriteDescription(text: String) {
        val description = context.getString(R.string.description_item_roll_move, text)
        binding.clickButton.contentDescription = description
    }

    override fun unbind() = with(binding) {
        clickButton.setOnClickListener(null)
        clickButton.setOnTouchListener(null)

        itemEnter.setOnTouchListener(null)
        itemEnter.removeTextChangedListener(textWatcher)
        itemEnter.setOnEditorActionListener(null)
    }

    interface ReadCallback {
        fun onRollChangeCheck(p: Int, animTime: Long, action: () -> Unit)
    }

    interface WriteCallback : NoteHistoryEnableControl {
        fun getAbsolutePosition(position: Int): Int?
        fun onRollHistoryAdd(action: HistoryAction)
        fun onRollEnterChanged(position: Int, text: String)
    }
}