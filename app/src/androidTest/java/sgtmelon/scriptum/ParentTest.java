package sgtmelon.scriptum;

import android.content.Context;

import org.junit.After;
import org.junit.Before;

import androidx.annotation.CallSuper;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 */
public abstract class ParentTest {

    Context context;

    @Before
    @CallSuper
    public void setUp() throws Exception {
        context = getInstrumentation().getTargetContext();
    }

    @After
    @CallSuper
    public void tearDown() {

    }

}