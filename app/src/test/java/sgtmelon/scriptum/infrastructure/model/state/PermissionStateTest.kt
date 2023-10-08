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
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for [PermissionState].
 */
class PermissionStateTest : ParentTest() {

    @MockK lateinit var activity: Activity
    @MockK lateinit var viewModel: PermissionViewModel
    @MockK lateinit var permission: Permission

    private val value = nextString()
    private val key = PermissionKey(value)
    private val state by lazy { PermissionState(permission) }

    @Before override fun setUp() {
        super.setUp()
        every { permission.key } returns key
        every { permission.value } returns value
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(activity, viewModel, permission)
    }

    @Test fun `getResult with null activity`() {
        assertNull(state.getResult(activity = null, viewModel))
    }

    @Test fun `getResult oldApi`() {
        every { permission.isOldApi } returns true

        assertEquals(state.getResult(activity, viewModel), PermissionResult.OLD_API)

        verifySequence {
            permission.isOldApi
        }
    }

    @Test fun `getResult newApi`() {
        every { permission.isOldApi } returns false
        every { permission.isNewApi } returns true

        assertEquals(state.getResult(activity, viewModel), PermissionResult.NEW_API)

        verifySequence {
            permission.isOldApi
            permission.isNewApi
        }
    }

    @Test fun `getResult granted`() {
        every { permission.isOldApi } returns false
        every { permission.isNewApi } returns false
        every { activity.checkSelfPermission(value) } returns PackageManager.PERMISSION_GRANTED
        every { activity.shouldShowRequestPermissionRationale(value) } returns Random.nextBoolean()
        every { viewModel.isCalled(key) } returns Random.nextBoolean()

        assertEquals(state.getResult(activity, viewModel), PermissionResult.GRANTED)

        verifySequence {
            permission.isOldApi
            permission.isNewApi

            permission.value
            activity.checkSelfPermission(value)
            permission.value
            activity.shouldShowRequestPermissionRationale(value)
            permission.key
            viewModel.isCalled(key)

            permission.value
        }
    }

    @Test fun `getResult forbidden`() {
        every { permission.isOldApi } returns false
        every { permission.isNewApi } returns false
        every { activity.checkSelfPermission(value) } returns PackageManager.PERMISSION_DENIED
        every { activity.shouldShowRequestPermissionRationale(value) } returns false
        every { viewModel.isCalled(key) } returns true

        assertEquals(state.getResult(activity, viewModel), PermissionResult.FORBIDDEN)

        verifySequence {
            permission.isOldApi
            permission.isNewApi

            permission.value
            activity.checkSelfPermission(value)
            permission.value
            activity.shouldShowRequestPermissionRationale(value)
            permission.key
            viewModel.isCalled(key)

            permission.value
        }
    }

    @Test fun `getResult ask`() {
        every { permission.isOldApi } returns false
        every { permission.isNewApi } returns false

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
                permission.isOldApi
                permission.isNewApi

                permission.value
                activity.checkSelfPermission(value)
                permission.value
                activity.shouldShowRequestPermissionRationale(value)
                permission.key
                viewModel.isCalled(key)

                permission.value
            }
        }
    }
}