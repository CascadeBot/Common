package org.cascadebot.common.permissions.objects

import org.cascadebot.common.permissions.Allow
import org.cascadebot.common.permissions.CascadePermission
import org.cascadebot.common.permissions.Deny
import org.cascadebot.common.permissions.Neutral
import org.cascadebot.common.permissions.PermissionNode
import org.cascadebot.common.permissions.PermissionsResult
import org.cascadebot.common.permissions.ResultCause
import kotlin.js.JsName

abstract class PermissionHolder {

    private val permissions: MutableSet<String> = mutableSetOf()
    abstract val type: HolderType

    @JsName("getPermissions")
    fun getPermissions(): Set<String> {
        return permissions.toSet()
    }

    @JsName("addPermission")
    fun addPermission(permission: String): Boolean {
        return permissions.add(permission)
    }

    @JsName("removePermission")
    fun removePermission(permission: String): Boolean {
        return permissions.remove(permission)
    }

    fun evaluatePermission(permission: CascadePermission): PermissionsResult {
        for (perm in getPermissions()) {
            if (PermissionNode(perm.substring(if (perm.startsWith("-")) 1 else 0))
                    .invoke(permission.permissionRaw)) {
                val cause = when (type) {
                    HolderType.GROUP -> ResultCause.GROUP
                    HolderType.USER -> ResultCause.USER
                }
                return if (perm.startsWith("-")) {
                    Deny(cause, this)
                } else {
                    Allow(cause, this)
                }
            }
        }
        return Neutral()
    }

    enum class HolderType {
        GROUP, USER
    }

}