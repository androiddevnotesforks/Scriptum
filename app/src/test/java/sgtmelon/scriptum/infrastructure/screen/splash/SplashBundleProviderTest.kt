package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.annotation.AppOpenFrom
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [SplashBundleProvider].
 */
class SplashBundleProviderTest : ParentTest() {

    @MockK lateinit var bundle: Bundle
    @MockK lateinit var outState: Bundle

    private val provider = SplashBundleProvider()

    @Before override fun setUp() {
        super.setUp()
        assertEquals(provider.open, SplashOpen.Main)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(bundle, outState)
    }

    @Test fun `getData with null bundle`() = testMainCase(bundle = null, key = null)

    @Test fun `getData with wrong key`() {
        val key = nextString()
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key
        testMainCase(bundle, key)
    }

    @Test fun `getData with empty key`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns ""
        testMainCase(bundle, key = null)
    }

    private fun testMainCase(bundle: Bundle?, key: String?) {
        assertGetData(SplashOpen.Main, key, bundle)

        verifySequence {
            bundle?.getString(AppOpenFrom.INTENT_KEY)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    @Test fun `getData for alarm`() {
        val key = AppOpenFrom.ALARM
        val id = Random.nextLong()

        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id

        assertGetData(SplashOpen.Alarm(id), key)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    @Test fun `getData for bind`() {
        val key = AppOpenFrom.BIND_NOTE
        val id = Random.nextLong()
        val type = Random.nextInt()
        val color = Random.nextInt()
        val name = nextString()

        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        every { bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE) } returns type
        every { bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR) } returns color

        every { bundle.getString(Note.Intent.NAME, Note.Default.NAME) } returns null
        assertGetData(SplashOpen.BindNote(id, type, color, Note.Default.NAME), key)

        every { bundle.getString(Note.Intent.NAME, Note.Default.NAME) } returns name
        assertGetData(SplashOpen.BindNote(id, type, color, name), key)

        verifySequence {
            repeat(times = 2) {
                bundle.getString(AppOpenFrom.INTENT_KEY)
                bundle.getLong(Note.Intent.ID, Note.Default.ID)
                bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)
                bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
                bundle.getString(Note.Intent.NAME, Note.Default.NAME)
                outState.putString(AppOpenFrom.INTENT_KEY, key)
            }
        }
    }

    @Test fun `getData for notifications`() {
        val key = AppOpenFrom.NOTIFICATIONS
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key

        assertGetData(SplashOpen.Notifications, key)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    @Test fun `getData for helpDisappear`() {
        val key = AppOpenFrom.HELP_DISAPPEAR
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key

        assertGetData(SplashOpen.HelpDisappear, key)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    @Test fun `getData for create textNote`() {
        val key = AppOpenFrom.CREATE_TEXT
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key

        assertGetData(SplashOpen.CreateNote(NoteType.TEXT), key)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    @Test fun `getData for create rollNote`() {
        val key = AppOpenFrom.CREATE_ROLL
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns key

        assertGetData(SplashOpen.CreateNote(NoteType.ROLL), key)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            outState.putString(AppOpenFrom.INTENT_KEY, key)
        }
    }

    private fun assertGetData(open: SplashOpen, key: String?, bundle: Bundle? = this.bundle) {
        provider.getData(bundle)
        assertEquals(provider.open, open)

        every { outState.putString(AppOpenFrom.INTENT_KEY, key) } returns Unit
        provider.saveData(outState)
    }
}