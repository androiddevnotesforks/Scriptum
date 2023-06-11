package sgtmelon.scriptum.source.ui.parts.dialog

import android.view.View
import android.widget.ListView
import androidx.annotation.ArrayRes
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.excludeParent
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Parent class for UI control of all alert dialogs.
 */
abstract class ListDialogPart(
    @StringRes private val titleId: Int,
    @ArrayRes private val textArrayId: Int?,
    private val textArray: Array<String>?
) : UiPart(),
    DialogUi {

    constructor(
        @StringRes titleId: Int,
        @ArrayRes textArrayId: Int
    ) : this(titleId, textArrayId, textArray = null)

    constructor(
        @StringRes titleId: Int,
        textArray: Array<String>
    ) : this(titleId, textArrayId = null, textArray)

    //region Views

    private val listView by lazy { getView(R.id.select_dialog_listview) }

    /** Exclude this parent for prevent match error with pref summaries (similar strings). */
    private val preferenceList by lazy { getView(R.id.recycler_view) }

    private val titleText = getViewByText(titleId).excludeParent(preferenceList)

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    protected val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    protected val itemArray: Array<String> = when {
        textArrayId != null -> context.resources.getStringArray(textArrayId)
        textArray != null -> textArray
        else -> throw IllegalAccessException("Can't get itemArray")
    }

    fun getItem(p: Int): Matcher<View> {
        val text = itemArray[p]

        /** Scrolling inside [ListView]. */
        Espresso.onData(`is`(text)).inAdapterView(listView).perform(ViewActions.scrollTo())

        return getViewByText(text).excludeParent(preferenceList)
    }

    //endregion

    fun cancel() = waitClose { cancelButton.click() }

    abstract fun apply()

    @CallSuper open fun assert() {
        listView.isDisplayed()
        titleText.isDisplayed().withTextColor(R.attr.clContent)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
    }
}