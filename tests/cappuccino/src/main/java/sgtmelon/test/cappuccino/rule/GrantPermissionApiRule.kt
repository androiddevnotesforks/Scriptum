package sgtmelon.test.cappuccino.rule

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.test.annotation.ExperimentalTestApi
import androidx.test.internal.platform.ServiceLoaderWrapper
import androidx.test.internal.platform.content.PermissionGranter
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.permission.PermissionRequester
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Custom realization of [GrantPermissionRule] with ability to skip grant check.
 *
 * [isActualPermission] - pass here your api check. If FALSE - permission check will be skipped.
 */
@SuppressLint("RestrictedApi")
@ExperimentalTestApi
class GrantPermissionApiRule(
    private val isActualPermission: Boolean,
    private val permissionGranter : PermissionGranter,
    vararg permissions: String
) : TestRule {

    init {
        val permissionSet = satisfyPermissionDependencies(*permissions)
        permissionGranter.addPermissions(*permissionSet.toTypedArray())
    }

    @VisibleForTesting
    private fun satisfyPermissionDependencies(vararg permissions: String): Set<String> {
        val permissionList: MutableSet<String> = LinkedHashSet(listOf(*permissions))
        /**
         * Explicitly grant READ_EXTERNAL_STORAGE permission when WRITE_EXTERNAL_STORAGE was
         * requested.
         */
        if (permissionList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissionList
    }

    override fun apply(base: Statement, description: Description): Statement {
        return RequestPermissionStatement(base, isActualPermission, permissionGranter)
    }

    private class RequestPermissionStatement(
        private val base: Statement,
        private val isActualPermission: Boolean,
        private val permissionGranter: PermissionGranter
    ) : Statement() {

        @Throws(Throwable::class)
        override fun evaluate() {
            if (isActualPermission) {
                permissionGranter.requestPermissions()
            }
            base.evaluate()
        }
    }

    companion object {
        fun grant(
            isActualPermission: Boolean,
            vararg permissions: String
        ): GrantPermissionApiRule {
            val granter = ServiceLoaderWrapper.loadSingleService(PermissionGranter::class.java) {
                PermissionRequester()
            }
            return GrantPermissionApiRule(isActualPermission, granter, *permissions)
        }
    }
}