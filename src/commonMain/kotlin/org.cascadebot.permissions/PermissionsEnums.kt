package org.cascadebot.permissions

enum class PermissionAction {

    /**
     * This override indicates that the permission action has no effect
     * on the permission access. If every permission action is neutral,
     * the permission will be **implicitly** denied.
     */
    NEUTRAL,

    /**
     * This override indicates that the permission action explicitly
     * allows the permission. In hierarchy mode, the permission will only
     * be subsequently allowed if all actions above the current one in the stack
     * respond with either `NEUTRAL` or `ALLOW`. In most restrictive mode, the
     * permission will be allowed only if there are no actions that deny the permission.
     */
    ALLOW,

    /**
     * This override indicates that the permission action explicitly denies
     * the permission. In hierarchy mode, the permission will only
     * be subsequently denied if all actions above the current one in the stack
     * respond with either `NEUTRAL` or `DENY`. In most restrictive mode, the
     * permission will be denied regardless of the other permission actions.
     */
    DENY

}

enum class PermissionMode {
    HIERARCHICAL, MOST_RESTRICTIVE
}