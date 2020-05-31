package org.cascadebot.shared.permissions.objects

import org.cascadebot.shared.permissions.Allow
import org.cascadebot.shared.permissions.CascadePermission
import org.cascadebot.shared.permissions.Deny
import org.cascadebot.shared.permissions.Neutral
import org.cascadebot.shared.permissions.PermissionNode
import org.cascadebot.shared.permissions.PermissionsResult
import org.cascadebot.shared.permissions.ResultCause

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