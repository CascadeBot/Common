package org.cascadebot.common.permissions.objects

import org.cascadebot.common.permissions.Allow
import org.cascadebot.common.permissions.CascadePermission
import org.cascadebot.common.permissions.Deny
import org.cascadebot.common.permissions.Neutral
import org.cascadebot.common.permissions.PermissionNode
import org.cascadebot.common.permissions.PermissionsResult
import org.cascadebot.common.permissions.ResultCause

abstract class PermissionHolder {

    private val permissions: MutableSet<String> = mutableSetOf()
    abstract val type: HolderType?

    fun getPermissions(): Set<String> {
        return permissions.toSet()
    }

    fun addPermission(permission: String): Boolean {
        return permissions.add(permission)
    }

    fun removePermission(permission: String): Boolean {
        return permissions.remove(permission)
    }

    fun evaluatePermission(permission: CascadePermission): PermissionsResult {
        for (perm in getPermissions()) {
            if (PermissionNode(perm.substring(if (perm.startsWith("-")) 1 else 0))
                    .invoke(permission.permissionRaw)) {
                return if (perm.startsWith("-")) {
                    Deny(ResultCause.GROUP, this)
                } else {
                    Allow(ResultCause.GROUP, this)
                }
            }
        }
        return Neutral()
    }

    enum class HolderType {
        GROUP, USER
    }

}