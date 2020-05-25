package org.cascadebot.permissions.objects

import kotlin.jvm.Synchronized
import kotlin.random.Random

class PermissionGroup(val name: String) : PermissionHolder() {

    // Private constructor for Mongo
    @Suppress("unused")
    private constructor() : this("")

    private val characterPool = "abcdefghijkmnopqrstuvwxyzACDEFHJKLMNPRSTUVWXYZ123467890"

    // Base 55 with 5 chars gives 503284375 combinations, we should be ok for uniqueness
    // This is normal alphanumeric with similar characters removed for less errors when inputting
    val id: String = (1..5)
        .map { Random.nextInt(0, characterPool.length) }
        .map(characterPool::get)
        .joinToString("")

    private val roleIds: MutableSet<Long> = mutableSetOf()

    @Synchronized
    fun linkRole(roleId: Long): Boolean {
        return roleIds.add(roleId)
    }

    @Synchronized
    fun unlinkRole(roleId: Long): Boolean {
        return roleIds.remove(roleId)
    }

    fun getRoleIds(): Set<Long> {
        return roleIds.toSet()
    }

    override val type: HolderType = HolderType.GROUP

}