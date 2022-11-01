package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
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

    private val provider = SplashBundleProvider()

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(bundle)
    }

    @Test fun `getData with null bundle`() = testIntroMainCase(bundle = null)

    @Test fun `getData with wrong key`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns nextString()
        testIntroMainCase(bundle)
    }

    @Test fun `getData without key`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns ""
        testIntroMainCase(bundle)
    }

    private fun testIntroMainCase(bundle: Bundle?) {
        assertGetData(SplashOpen.Intro, bundle, isFirstStart = true)
        assertGetData(SplashOpen.Main, bundle, isFirstStart = false)

        if (bundle == null) return

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            bundle.getString(AppOpenFrom.INTENT_KEY)
        }
    }

    @Test fun `getData for alarm`() {
        val id = Random.nextLong()

        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.ALARM
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id

        assertGetData(SplashOpen.Alarm(id))

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
        }
    }

    @Test fun `getData for bind`() {
        val id = Random.nextLong()
        val color = Random.nextInt()
        val type = Random.nextInt()

        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.BIND_NOTE
        every { bundle.getLong(Note.Intent.ID, Note.Default.ID) } returns id
        every { bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR) } returns color
        every { bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE) } returns type

        assertGetData(SplashOpen.BindNote(id, color, type))

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
            bundle.getLong(Note.Intent.ID, Note.Default.ID)
            bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
            bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)
        }
    }

    @Test fun `getData for notifications`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.NOTIFICATIONS

        assertGetData(SplashOpen.Notifications)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
        }
    }

    @Test fun `getData for helpDisappear`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.HELP_DISAPPEAR

        assertGetData(SplashOpen.HelpDisappear)

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
        }
    }

    @Test fun `getData for create textNote`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.CREATE_TEXT

        assertGetData(SplashOpen.CreateNote(NoteType.TEXT))

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
        }
    }

    @Test fun `getData for create rollNote`() {
        every { bundle.getString(AppOpenFrom.INTENT_KEY) } returns AppOpenFrom.CREATE_ROLL

        assertGetData(SplashOpen.CreateNote(NoteType.ROLL))

        verifySequence {
            bundle.getString(AppOpenFrom.INTENT_KEY)
        }
    }

    private fun assertGetData(
        open: SplashOpen,
        bundle: Bundle? = this.bundle,
        isFirstStart: Boolean = Random.nextBoolean()
    ) {
        assertEquals(provider.getData(bundle, isFirstStart), open)
    }
}