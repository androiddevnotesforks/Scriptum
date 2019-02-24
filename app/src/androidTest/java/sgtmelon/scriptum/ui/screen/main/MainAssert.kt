package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.key.MainPage
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen
import sgtmelon.scriptum.ui.screen.main.rank.RankScreen

class MainAssert : BasicMatch() {

    fun onDisplayContent(page: MainPage.Name = MainPage.Name.NOTES) {
        onDisplay(R.id.main_parent_container)
        onDisplay(R.id.main_toolbar_holder)
        onDisplay(R.id.main_menu_navigation)

        when (page) {
            MainPage.Name.RANK -> {
                isSelected(R.id.item_page_rank)
                doesNotDisplay(R.id.main_add_fab)

                RankScreen { assert { onDisplayContent(count) } }
            }
            MainPage.Name.NOTES -> {
                isSelected(R.id.item_page_notes)
                onDisplay(R.id.main_add_fab)

                NotesScreen { assert { onDisplayContent(count) } }
            }
            MainPage.Name.BIN -> {
                isSelected(R.id.item_page_bin)
                doesNotDisplay(R.id.main_add_fab)

                BinScreen { assert { onDisplayContent(count) } }
            }
        }
    }

}