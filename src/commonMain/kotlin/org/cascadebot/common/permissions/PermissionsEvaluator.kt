package org.cascadebot.common.permissions

import org.cascadebot.common.permissions.objects.PermissionGroup
import org.cascadebot.common.permissions.objects.PermissionUser


class PermissionsEvaluator {

    fun evalPermission(
        context: PermissionEvalContext,
        permission: CascadePermission
    ): PermissionsResult {

        // This allows developers and owners to go into guilds and fix problems
        if (SecurityHandler.isAuthorised(
                context.userLevel,
                SecurityLevel.DEVELOPER
            )
        ) {
            return Allow(
                ResultCause.OFFICIAL,
                SecurityLevel.DEVELOPER
            )
        }
        if (SecurityHandler.isAuthorised(
                context.userLevel,
                SecurityLevel.CONTRIBUTOR
            ) && context.developmentBot) {
            return Allow(
                ResultCause.OFFICIAL,
                SecurityLevel.CONTRIBUTOR
            )
        }
        // If the user is owner then they have all perms, obsv..
        if (context.isUserOwner) return Allow(ResultCause.GUILD)
        // By default all members with the administrator perm have access to all perms; this can be turned off
        if (context.userPermissions.contains(DiscordPermission.ADMINISTRATOR) && context.adminsHaveAllPerms) {
            return Allow(ResultCause.GUILD)
        }
        // Get all user groups that are directly assigned and the groups assigned through roles
        val userGroups: List<PermissionGroup> = getUserGroups(context.user, context.groups, context.memberRoles)
        var result: PermissionsResult = getDefaultAction(permission)
        var evaluatedResult: PermissionsResult =
            Neutral()
        if (context.permissionMode === PermissionMode.MOST_RESTRICTIVE) {
            evaluatedResult = evaluateMostRestrictiveMode(context.user, userGroups, permission)
        } else if (context.permissionMode === PermissionMode.HIERARCHICAL) {
            evaluatedResult = evaluateHierarchicalMode(context.user, userGroups, permission)
        }
        if (evaluatedResult !is Neutral) {
            result = evaluatedResult
        }

        // Discord permissions will only allow a permission if is not already allowed or denied.
        // It will not override Cascade permissions!
        if (result is Neutral && hasDiscordPermissions(context.userPermissions, permission.discordPerm)) {
            result = Allow(
                ResultCause.DISCORD,
                permission.discordPerm
            )
        }
        return result
    }

    private fun hasDiscordPermissions(
        userPermissions: Set<DiscordPermission>,
        permissions: Set<DiscordPermission>
    ): Boolean {
        if (permissions.isEmpty()) return false
        return permissions.all { userPermissions.contains(it) }
    }

    private fun evaluateMostRestrictiveMode(
        user: PermissionUser,
        userGroups: List<PermissionGroup>,
        permission: CascadePermission
    ): PermissionsResult {
        var result: PermissionsResult = user.evaluatePermission(permission)
        if (result is Deny) return result
        for (group in userGroups) {
            val groupResult: PermissionsResult = group.evaluatePermission(permission)
            // If the result is neutral, it has no effect on the existing result.
            if (groupResult is Neutral) continue
            // This is most restrictive mode so if any group permissions is DENY, the evaluated result is DENY.
            if (groupResult is Deny) return groupResult
            result = groupResult
        }
        return result
    }

    private fun evaluateHierarchicalMode(
        user: PermissionUser,
        userGroups: List<PermissionGroup>,
        permission: CascadePermission
    ): PermissionsResult {
        var result: PermissionsResult =
            Neutral()
        // Loop through the groups backwards to preserve hierarchy; groups higher up override lower groups.
        for (group in userGroups.reversed()) {
            val groupResult: PermissionsResult = group.evaluatePermission(permission)
            if (groupResult is Neutral) continue
            // This overrides any previous action with no regard to what it was.
            result = groupResult
        }
        val userResult: PermissionsResult = user.evaluatePermission(permission)
        // User permissions take ultimate precedence over group permissions
        if (userResult !is Neutral) {
            result = userResult
        }
        return result
    }

    private fun getDefaultAction(permission: CascadePermission): PermissionsResult {
        // A default permission will never explicitly deny a permission.
        return if (permission.defaultPerm) Allow(ResultCause.DEFAULT) else Deny(
            ResultCause.DEFAULT
        )
    }

    private fun getUserGroups(
        user: PermissionUser,
        groups: List<PermissionGroup>,
        memberRoles: List<Long>
    ): List<PermissionGroup> {
        val userGroups: MutableList<PermissionGroup> =
            groups.filter { group -> user.groupIds.contains(group.id) }.toMutableList()

        groups.filter { group ->
            group.getRoleIds().any { id -> memberRoles.contains(id) }
        }.forEach { userGroups.add(it) }
        return userGroups
    }

}

class PermissionEvalContext(
    val developmentBot: Boolean,
    val permissionMode: PermissionMode,
    val userId: Long,
    val user: PermissionUser,
    val userLevel: SecurityLevel,
    val groups: List<PermissionGroup>,
    val userPermissions: Set<DiscordPermission>,
    val memberRoles: List<Long>,
    val isUserOwner: Boolean,
    val adminsHaveAllPerms: Boolean
)