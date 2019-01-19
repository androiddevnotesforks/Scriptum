package sgtmelon.scriptum;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import sgtmelon.scriptum.activity.IntroActivityTest;
import sgtmelon.scriptum.app.view.activity.SplashActivity;
import sgtmelon.scriptum.office.utils.PrefUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
public final class MainTest extends ParentTest {

    private final boolean firstStart = true;    //Показывать обучение или нет

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Override
    public void setUp() throws Exception {
        super.setUp();

        PrefUtils.getInstance(context).setFirstStart(firstStart);
    }

    @Test
    public void testApp() throws InterruptedException {
        Thread.sleep(1000);

        if (firstStart) {
            IntroActivityTest introActivityTest = new IntroActivityTest();
            introActivityTest.test();
        }

        testEmptyContent();
    }

    /**
     * Проверка пустых страниц, всё ли правильно отображается
     */
    private void testEmptyContent() {
        onView(withId(R.id.page_notes_item)).check(matches(isSelected()));

        onView(withId(R.id.preference_item)).check(matches(isDisplayed()));

        onView(allOf(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()));
        onView(allOf(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_notes_title)));
        onView(allOf(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_notes_details)));

        onView(withId(R.id.page_rank_item)).perform(click());

        onView(allOf(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()));
        onView(allOf(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_rank_title)));
        onView(allOf(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_rank_details)));

        onView(withId(R.id.page_bin_item)).perform(click());

        // TODO: 28.10.2018 не в иерархии
        //onView(withId(R.id.clear_item)).check(matches(not(isDisplayed())));

        onView(allOf(isDisplayed(), withId(R.id.info_container)))
                .check(matches(isDisplayed()));
        onView(allOf(isDisplayed(), withId(R.id.info_title_text)))
                .check(matches(withText(R.string.info_bin_title)));
        onView(allOf(isDisplayed(), withId(R.id.info_details_text)))
                .check(matches(withText(R.string.info_bin_details)));
    }

}