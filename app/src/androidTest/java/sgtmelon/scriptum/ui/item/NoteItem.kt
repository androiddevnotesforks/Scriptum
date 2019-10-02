package sgtmelon.scriptum.ui.item

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.ui.ParentRecyclerItem

class NoteItem(listMatcher: Matcher<View>, p: Int) : ParentRecyclerItem<NoteModel>(listMatcher, p) {



    override fun assert(model: NoteModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}