package sgtmelon.scriptum;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;

import androidx.annotation.CallSuper;
import sgtmelon.scriptum.office.utils.PrefUtils;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 */
public abstract class ParentTest {

    Context context;
    SharedPreferences preferences;

    @Before
    @CallSuper
    public void setUp() throws Exception {
        context = getInstrumentation().getTargetContext();
        preferences = PrefUtils.getInstance(context);
    }

    @After
    @CallSuper
    public void tearDown() {

    }

}
