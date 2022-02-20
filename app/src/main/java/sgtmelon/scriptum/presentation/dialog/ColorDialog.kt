package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.adapter.ColorAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

class ColorDialog : BlankDialog(), ItemListener.Click {

    private var checkInit: Int = DEF_CHECK
    var check: Int = DEF_CHECK
        private set

    fun setArguments(@Color check: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.Common.VALUE_INIT, check)
            putInt(SavedTag.Common.VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val context = requireContext()
        val padding = 24
        val recyclerView = RecyclerView(context).apply {
            id = R.id.color_recycler_view

            setPadding(padding, padding, padding, padding)
            overScrollMode = View.OVER_SCROLL_NEVER

            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.color_column_count))

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            adapter = ColorAdapter(clickListener = this@ColorDialog).setCheck(check)
        }

        return AlertDialog.Builder(context)
            .setTitle(title)
            .setView(recyclerView)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        checkInit = bundle?.getInt(SavedTag.Common.VALUE_INIT) ?: DEF_CHECK
        check = bundle?.getInt(SavedTag.Common.VALUE) ?: DEF_CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Common.VALUE_INIT, checkInit)
        outState.putInt(SavedTag.Common.VALUE, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled = checkInit != check
    }

    override fun onItemClick(view: View, p: Int) {
        check = p
        changeButtonEnable()
    }

    companion object {
        private const val DEF_CHECK = 0
    }
}