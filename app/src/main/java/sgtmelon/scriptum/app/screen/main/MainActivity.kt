package sgtmelon.scriptum.app.screen.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.factory.FragmentFactory
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.model.state.OpenState
import sgtmelon.scriptum.app.screen.main.bin.BinFragment
import sgtmelon.scriptum.app.screen.main.notes.NotesFragment
import sgtmelon.scriptum.app.screen.main.rank.RankFragment
import sgtmelon.scriptum.app.screen.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.app.screen.parent.ParentActivity
import sgtmelon.scriptum.office.annot.def.DialogDef

/**
 * Экран отвечающий за отображение главного меню и фрагментов
 * [RankFragment], [NotesFragment], [BinFragment]
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class MainActivity : ParentActivity(), MainCallback {

    // TODO !! План
    /**
     * 1. Покрытие приложения тестами
     * 2. Разбор с бд
     * 3. Введение многопоточности
     */

    // TODO Добавить перескакивание курсора при старте редактирования в нужное место
    // TODO setHasFixedSize recyclerView

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val rankFragment by lazy { FragmentFactory.getRankFragment(supportFragmentManager) }
    private val notesFragment by lazy { FragmentFactory.getNotesFragment(supportFragmentManager) }
    private val binFragment by lazy { FragmentFactory.getBinFragment(supportFragmentManager) }

    private val openState = OpenState()
    private val sheetDialog by lazy { DialogFactory.getSheetDialog(supportFragmentManager) }

    private val fab by lazy { findViewById<FloatingActionButton>(R.id.main_add_fab) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        viewModel.apply {
            callback = this@MainActivity
            setupData(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putBoolean(OpenState.KEY, openState.value)
                viewModel.saveData(bundle = this)
            })

    override fun setupNavigation(@IdRes itemId: Int) {
        fab.setOnClickListener {
            openState.tryInvoke {
                sheetDialog.setArguments(R.layout.view_sheet_add, R.id.add_navigation)
                sheetDialog.show(supportFragmentManager, DialogDef.SHEET)
            }
        }

        findViewById<BottomNavigationView>(R.id.main_menu_navigation).apply {
            setOnNavigationItemSelectedListener { viewModel.onSelectItem(it.itemId) }
            selectedItemId = itemId
        }

        sheetDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            sheetDialog.dismiss()

            startActivity(getNoteIntent(NoteData.getTypeById(it.itemId)))
            return@OnNavigationItemSelectedListener true
        }
        sheetDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun changeFabState(state: Boolean) = fab.setState(state)

    override fun scrollTop(name: MainPage.Name) = when (name) {
        MainPage.Name.RANK -> rankFragment.scrollTop()
        MainPage.Name.NOTES -> notesFragment.scrollTop()
        MainPage.Name.BIN -> binFragment.scrollTop()
    }

    override fun showPage(pageFrom: MainPage.Name, pageTo: MainPage.Name) {
        with(supportFragmentManager) {
            beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

                if (findFragmentByTag(pageFrom.name) != null) {
                    hide(getFragmentByName(pageFrom))
                }

                val fragmentTo = getFragmentByName(pageTo)
                if (findFragmentByTag(pageTo.name) != null) {
                    show(fragmentTo)
                    fragmentTo.onResume()
                } else {
                    add(R.id.main_fragment_container, fragmentTo, pageTo.name)
                }

                commit()
            }
        }
    }

    private fun getFragmentByName(name: MainPage.Name): Fragment = when (name) {
        MainPage.Name.RANK -> rankFragment
        MainPage.Name.NOTES -> notesFragment
        MainPage.Name.BIN -> binFragment
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)

        private fun FloatingActionButton.setState(state: Boolean) {
            if (state) show() else hide()
            isEnabled = state
        }
    }

}