package org.cascadebot.permissions

/**
 * Security levels defined by Role IDs or User IDs, A level can be defined by an unlimited amount of roles
 * and users. Security levels are declared in ascending order.
 */
enum class SecurityLevel(val level: Int) {

    CONTRIBUTOR(0),
    STAFF(1),
    DEVELOPER(2),
    OWNER(3);

    fun isAuthorised(minimumLevel: SecurityLevel): Boolean {
        return minimumLevel.level <= this.level
    }

}