package org.cascadebot.common.permissions.objects

import kotlin.js.JsName
import kotlin.jvm.Synchronized
import kotlin.random.Random

class PermissionGroup(val name: String) : PermissionHolder() {

    // Private constructor for Mongo
    @Suppress("unused")
    private constructor() : this("")

    // Base 55 with 5 chars gives 503284375 combinations, we should be ok for uniqueness
    // This is normal alphanumeric with similar characters removed for less errors when inputting
    var id: String = generateRandomId()
    private set

    private val roleIds: MutableSet<Long> = mutableSetOf()

    @Synchronized
    @JsName("linkRole")
    fun linkRole(roleId: Long): Boolean {
        return roleIds.add(roleId)
    }

    @Synchronized
    @JsName("unlinkRole")
    fun unlinkRole(roleId: Long): Boolean {
        return roleIds.remove(roleId)
    }

    @JsName("getRoleIds")
    fun getRoleIds(): Set<Long> {
        return roleIds.toSet()
    }

    override val type: HolderType = HolderType.GROUP

    companion object {

        private const val characterPool = "abcdefghijkmnopqrstuvwxyzACDEFHJKLMNPRSTUVWXYZ123467890"

        private fun generateRandomId() = (1..5)
            .map { Random.nextInt(0, characterPool.length) }
            .map(characterPool::get)
            .joinToString("")

        @JsName("createPermissionGroup")
        fun createPermissionGroup(name: String, id: String): PermissionGroup {
            return PermissionGroup(name).apply { this.id = id }
        }

    }

}