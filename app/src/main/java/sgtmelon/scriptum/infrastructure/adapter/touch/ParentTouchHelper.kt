package sgtmelon.scriptum.infrastructure.adapter.touch

import androidx.recyclerview.widget.ItemTouchHelper

abstract class ParentTouchHelper : ItemTouchHelper.Callback() {

    protected fun Int.isDrag() = this == ItemTouchHelper.ACTION_STATE_DRAG

    protected fun Int.isSwipe() = this == ItemTouchHelper.ACTION_STATE_SWIPE

    protected fun getDrag(isEnabled: Boolean) = if (isEnabled) FULL_DRAG else 0

    protected fun getSwipe(isEnabled: Boolean) = if (isEnabled) FULL_SWIPE else 0

    companion object {
        private const val FULL_DRAG = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        private const val FULL_SWIPE = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}