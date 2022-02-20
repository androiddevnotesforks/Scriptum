package sgtmelon.safedialog.dialog.parent

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import sgtmelon.safedialog.R

/**
 * Parent dialog for sheet's with [NavigationView].
 */
abstract class BlankMenuSheetDialog : BottomSheetDialogFragment(),
        NavigationView.OnNavigationItemSelectedListener {

    @get:LayoutRes abstract val layoutId: Int
    @get:IdRes abstract val navigationId: Int

    var itemSelectedListener: NavigationView.OnNavigationItemSelectedListener? = null
    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationView = view.findViewById<NavigationView?>(navigationId)
        navigationView?.setNavigationItemSelectedListener(this)

        setPeekHeight(view)
    }

    /**
     * Function for disable half height display after screen rotation.
     */
    private fun setPeekHeight(view: View) {
        val parentContainer = view.findViewById<CoordinatorLayout>(R.id.sheet_parent_container)

        dialog?.setOnShowListener {
            val parentView = parentContainer.parent as? View ?: return@setOnShowListener
            BottomSheetBehavior.from(parentView).peekHeight = parentContainer.height
            parentView.requestLayout()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        itemSelectedListener?.onNavigationItemSelected(menuItem)
        return false
    }
}