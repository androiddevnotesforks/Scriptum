package sgtmelon.scriptum.infrastructure.adapter.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchUIUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Parent class of [ItemTouchHelper] but without setup any elevation inside [onChildDraw]
 * and [clearView]. Check out realization of this functions inside [ItemTouchHelper] and you will
 * understand.
 */
abstract class SkipElevationTouchHelper : ParentTouchHelper() {

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) = Unit

    /**
     * Remove super of [clearView] because inside [ItemTouchUIUtil] implementation happen
     * reset of [viewHolder] elevation, and same staff inside [onChildDraw]. This may be
     * harmful for custom animation!
     *
     * But implement here another part of super function. Talking about translation
     * reset, and make it inside [setTranslation] func.
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        setTranslation(viewHolder, dX = 0f, dY = 0f)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        setTranslation(viewHolder, dX, dY)
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) = Unit

    private fun setTranslation(viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float) {
        viewHolder.itemView.apply {
            translationX = dX
            translationY = dY
        }
    }
}