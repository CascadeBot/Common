package org.cascadebot.shared.permissions.objects

import kotlin.jvm.Synchronized

class PermissionUser : PermissionHolder() {

    private val groups: MutableSet<String> = mutableSetOf()

    @Synchronized
    fun addGroup(group: PermissionGroup): Boolean {
        return groups.add(group.id)
    }

    @Synchronized
    fun removeGroup(group: PermissionGroup): Boolean {
        return groups.remove(group.id)
    }

    val groupIds: Set<String>
        get() = groups.toSet()

    override val type: HolderType = HolderType.USER
}