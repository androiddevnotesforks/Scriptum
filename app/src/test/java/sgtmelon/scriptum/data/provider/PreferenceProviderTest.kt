package sgtmelon.scriptum.data.provider

import android.content.Context
import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test of [PreferenceProvider].
 */
class PreferenceProviderTest : ParentTest() {

    @MockK lateinit var context: Context

    private val providerKey by lazy { PreferenceProvider.Key(context) }
    private val providerDef by lazy { PreferenceProvider.Def(context) }

    @Test fun valueKey() {
        TODO()
    }

    @Test fun valueDef() {
        TODO()
    }

}