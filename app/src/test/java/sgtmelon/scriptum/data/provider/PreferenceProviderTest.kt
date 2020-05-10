package sgtmelon.scriptum.data.provider

import android.content.res.Resources
import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test of [PreferenceProvider].
 */
class PreferenceProviderTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val providerKey by lazy { PreferenceProvider.Key(resources) }
    private val providerDef by lazy { PreferenceProvider.Def(resources) }

    @Test fun valueKey() {
        TODO()
    }

    @Test fun valueDef() {
        TODO()
    }

}