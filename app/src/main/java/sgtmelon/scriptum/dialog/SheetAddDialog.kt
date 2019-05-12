package sgtmelon.scriptum.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R


class SheetAddDialog : BottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var itemSelectedListener: NavigationView.OnNavigationItemSelectedListener
    lateinit var dismissListener: DialogInterface.OnDismissListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.view_sheet_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<NavigationView>(R.id.add_navigation)
                ?.setNavigationItemSelectedListener(this)
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