package sgtmelon.scriptum.infrastructure.adapter.touch.listener

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 * Realization of Touch listener for make drag available only for [dragView].
 */
class DragTouchListener(
    private val listener: ItemDragListener,
    private val dragView: View
) : View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            listener.setDrag(v?.id == dragView.id)
        }

        return false
    }
}