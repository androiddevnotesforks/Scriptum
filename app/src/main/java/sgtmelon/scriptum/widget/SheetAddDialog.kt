package sgtmelon.scriptum.widget

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import sgtmelon.safedialog.DialogBlank


class SheetAddDialog : BottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {

    @LayoutRes private var layout: Int = 0
    @IdRes private var navigation: Int = 0

    lateinit var itemSelectedListener: NavigationView.OnNavigationItemSelectedListener
    lateinit var dismissListener: DialogInterface.OnDismissListener

    fun setArguments(@LayoutRes layout: Int, @IdRes navigation: Int) {
        val bundle = Bundle()

        bundle.putInt(DialogBlank.INIT, layout)
        bundle.putInt(DialogBlank.VALUE, navigation)

        arguments = bundle
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        layout = savedInstanceState?.getInt(DialogBlank.INIT)
                ?: arguments?.getInt(DialogBlank.INIT)
                ?: 0

        navigation = savedInstanceState?.getInt(DialogBlank.VALUE)
                ?: arguments?.getInt(DialogBlank.VALUE)
                ?: 0

        val view = inflater.inflate(layout, container, false)
                ?: throw NullPointerException("Id error, layout view is null")

        val navigationView = view.findViewById<NavigationView>(navigation)
        navigationView?.setNavigationItemSelectedListener(this)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(DialogBlank.INIT, layout)
        outState.putInt(DialogBlank.VALUE, navigation)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        dismissListener.onDismiss(dialog)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        itemSelectedListener.onNavigationItemSelected(menuItem)
        return false
    }

}