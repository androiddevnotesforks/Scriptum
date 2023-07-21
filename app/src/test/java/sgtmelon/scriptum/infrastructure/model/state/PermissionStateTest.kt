package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import android.content.pm.PackageManager
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [PermissionState].
 */
class PermissionStateTest : ParentTest() {

    @MockK lateinit var activity: Activity
    @MockK lateinit var viewModel: PermissionViewModel
    @MockK lateinit var permission: Permission

    private val value = nextString()
    private val key = PermissionKey(nextString())
    private val state by lazy { PermissionState(permission) }

    @Before override fun setUp() {
        super.setUp()
        every { permission.key } returns key
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(activity, viewModel, permission)
    }

    @Test fun `getResult with null activity`() {
        assertNull(state.getResult(activity = null, viewModel))
    }

    @Test fun `getResult granted`() {
        every { activity.checkSelfPermission(value) } returns PackageManager.PERMISSION_GRANTED
        every { activity.shouldShowRequestPermissionRationale(value) } returns Random.nextBoolean()
        every { viewModel.isCalled(key) } returns Random.nextBoolean()

        assertEquals(state.getResult(activity, viewModel), PermissionResult.GRANTED)

        verifySequence {
            activity.checkSelfPermission(value)
            activity.shouldShowRequestPermissionRationale(value)
            viewModel.isCalled(key)
        }
    }

    @Test fun `getResult forbidden`() {
        every { activity.checkSelfPermission(value) } returns PackageManager.PERMISSION_DENIED
        every { activity.shouldShowRequestPermissionRationale(value) } returns false
        every { viewModel.isCalled(key) } returns true

        assertEquals(state.getResult(activity, viewModel), PermissionResult.FORBIDDEN)

        verifySequence {
            activity.checkSelfPermission(value)
            activity.shouldShowRequestPermissionRationale(value)
            viewModel.isCalled(key)
        }
    }

    @Test fun `getResult ask`() {
        every { activity.checkSelfPermission(value) } returns PackageManager.PERMISSION_DENIED

        every { activity.shouldShowRequestPermissionRationale(value) } returns false
        every { viewModel.isCalled(key) } returns false
        assertEquals(state.getResult(activity, viewModel), PermissionResult.ASK)

        every { activity.shouldShowRequestPermissionRationale(value) } returns true
        every { viewModel.isCalled(key) } returns true
        assertEquals(state.getResult(activity, viewModel), PermissionResult.ASK)

        every { activity.shouldShowRequestPermissionRationale(value) } returns true
        every { viewModel.isCalled(key) } returns false
        assertEquals(state.getResult(activity, viewModel), PermissionResult.ASK)

        verifySequence {
            repeat(times = 3) {
                activity.checkSelfPermission(value)
                activity.shouldShowRequestPermissionRationale(value)
                viewModel.isCalled(key)
            }
        }
    }
}