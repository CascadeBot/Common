package org.cascadebot.common.permissions.objects

import kotlin.js.JsName
import kotlin.jvm.Synchronized

class PermissionUser : PermissionHolder() {

    private val groups: MutableSet<String> = mutableSetOf()

    @Synchronized
    @JsName("addGroup")
    fun addGroup(group: PermissionGroup): Boolean {
        return groups.add(group.id)
    }

    @Synchronized
    @JsName("removeGroup")
    fun removeGroup(group: PermissionGroup): Boolean {
        return groups.remove(group.id)
    }

    val groupIds: Set<String>
        get() = groups.toSet()

    override val type: HolderType = HolderType.USER
}