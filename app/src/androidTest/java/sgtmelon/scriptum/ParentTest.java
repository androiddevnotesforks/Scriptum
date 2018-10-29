package sgtmelon.scriptum;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.junit.After;
import org.junit.Before;

import androidx.annotation.CallSuper;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 */
public abstract class ParentTest {

    Context context;
    SharedPreferences pref;

    @Before
    @CallSuper
    public void setUp() throws Exception {
        context = getInstrumentation().getTargetContext();
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @After
    @CallSuper
    public void tearDown() {

    }

}
