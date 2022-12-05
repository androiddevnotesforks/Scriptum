package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import android.content.pm.PackageManager
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [PermissionState].
 */
class PermissionStateTest : ParentTest() {

    @MockK lateinit var activity: Activity

    private val permission = nextString()
    private val state by lazy { PermissionState(permission) }

    @Test fun getResult() {
        assertNull(state.getResult(activity = null))

        every { activity.checkSelfPermission(permission) } returns PackageManager.PERMISSION_GRANTED
        assertEquals(state.getResult(activity), PermissionResult.GRANTED)

        every { activity.checkSelfPermission(permission) } returns PackageManager.PERMISSION_DENIED
        every { activity.shouldShowRequestPermissionRationale(permission) } returns false
        assertEquals(state.getResult(activity), PermissionResult.FORBIDDEN)

        every { activity.shouldShowRequestPermissionRationale(permission) } returns true
        assertEquals(state.getResult(activity), PermissionResult.ASK)

        assertNull(PermissionState(permission).getResult(activity = null))
    }

}