package sgtmelon.scriptum.app.screen.main


import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.factory.FragmentFactory
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.model.state.OpenState
import sgtmelon.scriptum.app.screen.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.utils.AppUtils.setState

class MainActivity : ParentActivity(),
        MainCallback,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private var firstStart: Boolean = true
    private var page: Int = MainPage.Name.NOTES.ordinal

    private val openState = OpenState()

    private val rankFragment by lazy {
        FragmentFactory.getRankFragment(supportFragmentManager)
    }
    private val notesFragment by lazy {
        FragmentFactory.getNotesFragment(supportFragmentManager)
    }
    private val binFragment by lazy {
        FragmentFactory.getBinFragment(supportFragmentManager)
    }

    private val sheetDialog by lazy {
        DialogFactory.getSheetDialog(supportFragmentManager)
    }

    private val fab: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.main_add_fab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(PAGE_CURRENT)
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        setupNavigation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(PAGE_CURRENT, page)
        outState.putBoolean(OpenState.KEY, openState.value)
    }

    private fun setupNavigation() {
        fab.setOnClickListener {
            openState.tryInvoke {
                sheetDialog.setArguments(R.layout.view_sheet_add, R.id.add_navigation)
                sheetDialog.show(supportFragmentManager, DialogDef.SHEET)
            }
        }

        val navigationView = findViewById<BottomNavigationView>(R.id.main_menu_navigation)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = MainPage.id[page]

        sheetDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            sheetDialog.dismiss()

            startActivity(getNoteIntent(NoteData.getTypeById(it.itemId)))
            return@OnNavigationItemSelectedListener true
        }
        sheetDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun changeFabState(state: Boolean) = fab.setState(state)

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // TODO Оптимизировать
        val pageFrom = MainPage.Name.values()[page]
        val pageTo = MainPage.getById(menuItem.itemId)

        page = pageTo.ordinal

        if (!firstStart && pageTo == pageFrom) {
            when (pageTo) {
                MainPage.Name.RANK -> rankFragment.scrollTop()
                MainPage.Name.NOTES -> notesFragment.scrollTop()
                MainPage.Name.BIN -> binFragment.scrollTop()
            }
        } else {
            if (firstStart) firstStart = false

            changeFabState(state = pageTo == MainPage.Name.NOTES)

            val transaction = supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

            if (supportFragmentManager.findFragmentByTag(pageFrom.name) != null) {
                transaction.hide(getFragmentByName(pageFrom))
            }

            val fragmentTo = getFragmentByName(pageTo)
            if (supportFragmentManager.findFragmentByTag(pageTo.name) != null) {
                transaction.show(fragmentTo)
                fragmentTo.onResume()
            } else {
                transaction.add(R.id.main_fragment_container, fragmentTo, pageTo.name)
            }

            transaction.commit()
        }

        return true
    }

    private fun getFragmentByName(name: MainPage.Name): Fragment = when(name) {
        MainPage.Name.RANK -> rankFragment
        MainPage.Name.NOTES -> notesFragment
        MainPage.Name.BIN -> binFragment
    }

    companion object {
        private const val PAGE_CURRENT = "INSTANCE_MAIN_PAGE_CURRENT"

        // TODO: 28.01.2019 перевести приложение на Kotlin + RxJava + Spek
        // TODO: 13.01.2019 Добавить getAdapterPosition safety - RecyclerView.NO_POSITION check
        // TODO: 16.01.2019 сделать блокировку кнопки изменить сохранить при работе анимации крестик-стрелка (если анимируется - не нажимать)
        // TODO: 19.01.2019 Добавить перескакивание курсора при старте редактирования в нужное место
        // TODO: 20.01.2019 Разобраться со стилями
        // TODO: 27.01.2019 Добавить ещё одну тему
        // TODO: 22.11.2018 аннотация профессор

        // TODO setHasFixedSize recyclerView
    }

}