package org.cascadebot.common.permissions

import kotlin.js.JsName

/**
 * Security levels defined by Role IDs or User IDs, A level can be defined by an unlimited amount of roles
 * and users. Security levels are declared in ascending order.
 */
object SecurityHandler {

    /**
     * Returns the list of role and user IDs that will authenticate against this security level
     *
     * @param levels The list of configured security levels
     * @param level The level to get IDs for
     * @return The list of IDs authenticated against this level
     */
    @JsName("getIds")
    fun getIds(levels: Map<SecurityLevel, Set<Long>>, level: SecurityLevel): Set<Long> {
        return levels[level] ?: setOf()
    }

    /**
     * Checks if the user's level is greater than or equal to the level we are comparing against.
     *
     * @param userLevel      The security level of the user.
     * @param minimumLevel   The minimum security level the user should have.
     * @return where level >= comparing level.
     */
    @JsName("isAuthorised")
    fun isAuthorised(userLevel: SecurityLevel, minimumLevel: SecurityLevel): Boolean {
        return userLevel.isAuthorised(minimumLevel)
    }

    /**
     * Returns the highest security level a user has access to.
     *
     * @param levels  The list of configured security levels
     * @param userId  The ID of the user to check.
     * @param roleIds The list of role IDs to check against, this will almost always be the roles from the official server.
     * @return The highest security level the user has access to or `null` if they do not have access to anything.
     */
    @JsName("getLevelById")
    fun getLevelById(levels: Map<SecurityLevel, Set<Long>>, userId: Long, roleIds: Set<Long?>): SecurityLevel? {
        var highestLevel: SecurityLevel? = null
        for (securityLevel in SecurityLevel.values()) {
            val ids = getIds(levels, securityLevel)
            if (ids.contains(userId) || ids.any { roleIds.contains(it) }) {
                if (securityLevel.level > highestLevel?.level ?: Int.MIN_VALUE) {
                    highestLevel = securityLevel
                }
            }
        }
        return highestLevel
    }

}