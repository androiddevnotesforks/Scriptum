package sgtmelon.scriptum.activity;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.view.activity.IntroActivity;
import sgtmelon.scriptum.office.annot.IntroAnn;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Тест для {@link IntroActivity}
 */
@SmallTest
public final class IntroActivityTest {

    @Rule
    public ActivityTestRule<IntroActivity> activityTestRule = new ActivityTestRule<>(IntroActivity.class);

    @Test
    public void test() {
        for (int i = 0; i < IntroAnn.count; i++) {
            onView(allOf(isDisplayed(), withId(R.id.info_title_text)))
                    .check(matches(withText(IntroAnn.title[i])));

            onView(allOf(isDisplayed(), withId(R.id.info_details_text)))
                    .check(matches(withText(IntroAnn.details[i])));

            onView(withId(R.id.intro_pager)).perform(swipeLeft());
            checkButtonEnable(i);
        }

        for (int i = IntroAnn.count - 2; i >= 0; i--) {
            onView(withId(R.id.intro_pager)).perform(swipeRight());
        }

        for (int i = 0; i < IntroAnn.count; i++) {
            onView(withId(R.id.intro_pager)).perform(swipeLeft());
            checkButtonEnable(i);
        }

        onView(withId(R.id.end_button))
                .check(matches(withText(R.string.info_intro_button)))
                .perform(click());
    }

    private void checkButtonEnable(int i) {
        if (i < IntroAnn.count - 2) {
            onView(withId(R.id.end_button)).check(matches(not(isEnabled())));
        } else {
            onView(withId(R.id.end_button)).check(matches(isEnabled()));
        }
    }

}