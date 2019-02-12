package sgtmelon.scriptum.app.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.safedialog.library.SheetDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.factory.FragmentFactory
import sgtmelon.scriptum.app.view.callback.MainCallback
import sgtmelon.scriptum.app.view.fragment.main.BinFragment
import sgtmelon.scriptum.app.view.fragment.main.NotesFragment
import sgtmelon.scriptum.app.view.fragment.main.RankFragment
import sgtmelon.scriptum.app.view.parent.BaseActivityParent
import sgtmelon.scriptum.office.annot.def.*
import sgtmelon.scriptum.office.st.OpenSt
import sgtmelon.scriptum.office.st.PageSt


class MainActivity : BaseActivityParent(), MainCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {

        // TODO: 28.01.2019 перевести приложение на Kotlin + RxJava + Spek
        // TODO: 13.01.2019 Annotation NonNull/Nullable везде где только можно (для override методов добавить nullable)
        // TODO: 13.01.2019 Добавить getAdapterPosition safety - RecyclerView.NO_POSITION check
        // TODO: 16.01.2019 сделать блокировку кнопки изменить сохранить при работе анимации крестик-стрелка (если анимируется - не нажимать)
        // TODO: 19.01.2019 Добавить перескакивание курсора при старте редактирования в нужное место
        // TODO: 20.01.2019 Разобраться со стилями
        // TODO: 27.01.2019 Добавить ещё одну тему
        // TODO: 22.11.2018 аннотация профессор

        private val TAG = MainActivity::class.java.simpleName
    }

    private val pageSt = PageSt()
    private val openSt = OpenSt()

    private val fm by lazy { supportFragmentManager }

    private val rankFragment by lazy { FragmentFactory.getRankFragment(fm) }
    private val notesFragment by lazy { FragmentFactory.getNotesFragment(fm) }
    private val binFragment by lazy { FragmentFactory.getBinFragment(fm) }

    private val sheetDialog by lazy { DialogFactory.getSheetDialog(fm) }

    private val fab by lazy {findViewById<FloatingActionButton>(R.id.main_add_fab)}

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var page = PageDef.notes
        if (savedInstanceState != null) {
            pageSt.page = savedInstanceState.getInt(IntentDef.STATE_PAGE)
            openSt.isOpen = savedInstanceState.getBoolean(IntentDef.STATE_OPEN)

            page = pageSt.page
        }

        setupNavigation(page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)

        outState.putInt(IntentDef.STATE_PAGE, pageSt.page)
        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen)
    }

    private fun setupNavigation(@PageDef page: Int) {
        Log.i(TAG, "setupNavigation")

        fab.setOnClickListener {
            if (!openSt.isOpen) {
                openSt.isOpen = true

                sheetDialog.setArguments(R.layout.sheet_add, R.id.add_navigation)
                sheetDialog.show(supportFragmentManager, DialogDef.SHEET)
            }
        }

        val navigationView = findViewById<BottomNavigationView>(R.id.main_menu_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = PageDef.itemId[page]

        sheetDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            sheetDialog.dismiss()

            val type = when (it.itemId) {
                R.id.item_add_text -> TypeNoteDef.text
                else -> TypeNoteDef.roll
            }

            startActivity(NoteActivity.getIntent(this@MainActivity, type))
            true
        }
        sheetDialog.dismissListener = DialogInterface.OnDismissListener { openSt.isOpen = false }
    }

    override fun changeFabState(show: Boolean) {
        if (show) {
            fab.isEnabled = true
            fab.show()
        } else {
            fab.isEnabled = false
            fab.hide()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        Log.i(TAG, "onNavigationItemSelected")

        val pageOld = pageSt.page
        var page = pageSt.page

        when (menuItem.itemId) {
            R.id.item_page_rank -> page = PageDef.rank
            R.id.item_page_notes -> page = PageDef.notes
            R.id.item_page_bin -> page = PageDef.bin
        }

        val scrollTop = page == pageSt.page
        pageSt.page = page

        val transaction = fm.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        when (pageSt.page) {
            PageDef.rank -> {
                if (scrollTop) {
                    rankFragment.scrollTop()
                } else {
                    changeFabState(false)

                    if (fm.findFragmentByTag(FragmentDef.RANK) != null) {
                        transaction.show(rankFragment)
                        rankFragment.onResume()
                    } else {
                        transaction.add(R.id.main_fragment_container, rankFragment, FragmentDef.RANK)
                    }
                }
            }
            PageDef.notes -> {
                if (scrollTop) {
                    notesFragment.scrollTop()
                } else {
                    changeFabState(true)

                    if (fm.findFragmentByTag(FragmentDef.NOTES) != null) {
                        transaction.show(notesFragment)
                        notesFragment.onResume()
                    } else {
                        transaction.add(R.id.main_fragment_container, notesFragment, FragmentDef.NOTES)
                    }
                }
            }
            PageDef.bin -> {
                if (scrollTop) {
                    binFragment.scrollTop()
                } else {
                    changeFabState(false)

                    if (fm.findFragmentByTag(FragmentDef.BIN) != null) {
                        transaction.show(binFragment)
                        binFragment.onResume()
                    } else {
                        transaction.add(R.id.main_fragment_container, binFragment, FragmentDef.BIN)
                    }
                }
            }
        }

        if (!scrollTop) {
            when (pageOld) {
                PageDef.rank -> if (fm.findFragmentByTag(FragmentDef.RANK) != null) {
                    transaction.hide(rankFragment)
                }
                PageDef.notes -> if (fm.findFragmentByTag(FragmentDef.NOTES) != null) {
                    transaction.hide(notesFragment)
                }
                PageDef.bin -> if (fm.findFragmentByTag(FragmentDef.BIN) != null) {
                    transaction.hide(binFragment)
                }
            }
        }

        transaction.commit()
        return true
    }

}