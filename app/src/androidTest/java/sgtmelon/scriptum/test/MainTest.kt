package sgtmelon.scriptum.test

import android.view.View
import org.junit.Rule
import org.junit.Test

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.view.activity.SplashActivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf

@LargeTest
class MainTest : ParentTest() {

    private val firstStart = true    //Показывать обучение или нет

    @Rule
    var activityTestRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = firstStart
    }

    @Test
    @Throws(InterruptedException::class)
    fun testApp() {
        Thread.sleep(1000)

        if (firstStart) {
            val introTest = IntroTest()
            introTest.test0_screenWork()
        }

        testEmptyContent()
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