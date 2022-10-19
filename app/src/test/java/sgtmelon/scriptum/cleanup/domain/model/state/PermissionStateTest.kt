package sgtmelon.scriptum.cleanup.domain.model.state

import android.app.Activity
import android.content.pm.PackageManager
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider.Version
import sgtmelon.test.common.nextString

/**
 * Test for [PermissionState].
 */
class PermissionStateTest : ParentTest() {

    @MockK lateinit var activity: Activity

    private val permission = nextString()
    private val permissionState by lazy { PermissionState(permission, activity) }

    @Test fun getResult() {
        mockkObject(Version)

        every { Version.isMarshmallowLess() } returns true
        assertEquals(PermissionResult.LOW_API, permissionState.getResult())

        every { Version.isMarshmallowLess() } returns false
        every { activity.checkSelfPermission(permission) } returns PackageManager.PERMISSION_GRANTED
        assertEquals(PermissionResult.GRANTED, permissionState.getResult())

        every { activity.checkSelfPermission(permission) } returns PackageManager.PERMISSION_DENIED
        every { activity.shouldShowRequestPermissionRationale(permission) } returns false
        assertEquals(PermissionResult.FORBIDDEN, permissionState.getResult())

        every { activity.shouldShowRequestPermissionRationale(permission) } returns true
        assertEquals(PermissionResult.ASK, permissionState.getResult())

        assertNull(PermissionState(permission, activity = null).getResult())
    }

}