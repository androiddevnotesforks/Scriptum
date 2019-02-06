package sgtmelon.scriptum.test

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.Page

@LargeTest
class MainTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    @Test fun test0_emptyContent() {
        MainScreen {
            assert { isSelected(Page.NOTES) }

            onNavigate(Page.RANK)
            assert { isSelected(Page.RANK) }

            onNavigate(Page.NOTES)
            assert { isSelected(Page.NOTES) }

            onNavigate(Page.BIN)
            assert { isSelected(Page.BIN) }
        }
    }

    /**
     * Проверка пустых страниц, всё ли правильно отображается
     */
    private fun testEmptyContent() {
        onView(ViewMatchers.withId(R.id.page_notes_item)).check(matches(isSelected()))

        onView(withId(R.id.preference_item)).check(matches(isDisplayed()))

        onView(allOf<View>(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_notes_title)))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_notes_details)))

        onView(withId(R.id.page_rank_item)).perform(click())

        onView(allOf<View>(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_rank_title)))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_rank_details)))

        onView(withId(R.id.page_bin_item)).perform(click())

        // TODO: 28.10.2018 не в иерархии
        //onView(withId(R.id.clear_item)).check(matches(not(isDisplayed())));

        onView(allOf<View>(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_bin_title)))
        onView(allOf<View>(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_bin_details)))
    }

}