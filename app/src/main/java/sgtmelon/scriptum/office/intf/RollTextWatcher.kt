package sgtmelon.scriptum.office.intf

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.model.item.CursorItem
import sgtmelon.scriptum.app.model.item.RollItem

class RollTextWatcher(private val position: Int, private val item: RollItem) : TextWatcher {

    lateinit var rollEnter: EditText
    lateinit var inputIntf: InputIntf
    lateinit var bindIntf: BindIntf
    lateinit var rollWatcher: ItemIntf.RollWatcher

    private var textFrom = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        textFrom = s.toString()
        cursorFrom = rollEnter.selectionEnd
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (position == RecyclerView.NO_POSITION) return

        val textTo = s.toString()
        val cursorTo = rollEnter.selectionEnd

        if (textFrom == textTo) return

        if (!TextUtils.isEmpty(textTo)) {
            if (!TextUtils.isEmpty(textFrom)) {
                val cursorItem = CursorItem(cursorFrom, cursorTo)
                inputIntf.onRollChange(position, textFrom, textTo, cursorItem)

                textFrom = textTo
                cursorFrom = cursorTo
            }
        } else {
            inputIntf.onRollRemove(position, item.toString())
        }

        bindIntf.bindInput()
    }

    override fun afterTextChanged(s: Editable) {
        if (position == RecyclerView.NO_POSITION) return

        if (!TextUtils.isEmpty(textFrom)) {
            rollWatcher.afterRollChanged(position, rollEnter.text.toString())
        }

        if (inputIntf.isChangeEnabled) {
            inputIntf.setEnabled(true)
        }
    }

}