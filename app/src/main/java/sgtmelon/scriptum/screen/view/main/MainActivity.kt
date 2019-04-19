package sgtmelon.scriptum.screen.view.main

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
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.screen.callback.main.MainCallback
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Экран отвечающий за отображение главного меню и фрагментов
 * [RankFragment], [NotesFragment], [BinFragment]
 *
 * @author SerjantArbuz
 */
class MainActivity : AppActivity(), MainCallback {

    // TODO Добавить перескакивание курсора при старте редактирования в нужное место
    // TODO setHasFixedSize recyclerView

    // TODO если отчистить базу данных то закреплённые уведомления останутся
    // TODO Ошибка с inputControl при добавлении в список нового пункта

    // TODO Перед релизом
    /**
     * 1. Доделать [InputControl] (доработать функционал)
     * 2. Доделать [StatusItem] (добавить кнопки и пр.)
     * 2.1 Доделать диалог добавления заметки
     * 3. Выписать все сценарии в соответствующий класс (даже которые нельзя потестить в ui - поворот экрана (тестить ручками))
     * 4. Доделать ui тесты, начать писать unit, integration тесты
     * 5. Проверить всё
     * 6. Релиз
     */

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java).apply {
            callback = this@MainActivity
        }
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

        viewModel.setupData(savedInstanceState)
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

    override fun scrollTop(mainPage: MainPage) = when (mainPage) {
        MainPage.RANK -> rankFragment.scrollTop()
        MainPage.NOTES -> notesFragment.scrollTop()
        MainPage.BIN -> binFragment.scrollTop()
    }

    override fun showPage(pageFrom: MainPage, pageTo: MainPage) {
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

    private fun getFragmentByName(mainPage: MainPage): Fragment = when (mainPage) {
        MainPage.RANK -> rankFragment
        MainPage.NOTES -> notesFragment
        MainPage.BIN -> binFragment
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)

        private fun FloatingActionButton.setState(state: Boolean) {
            if (state) show() else hide()
            isEnabled = state
        }
    }

}