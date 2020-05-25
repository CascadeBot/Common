package org.cascadebot.permissions

import jdk.internal.module.Checks
import sun.tools.java.Environment
import java.lang.reflect.Member
import java.security.Security


class PermissionsEvaluator {

    fun evalPermission(
        member: java.lang.reflect.Member,
        channel: GuildChannel?,
        permission: CascadePermission,
        settings: GuildSettingsCore
    ): Result? {
        jdk.internal.module.Checks.notNull(member, "member")
        jdk.internal.module.Checks.notNull(permission, "permission")

        // This allows developers and owners to go into guilds and fix problems
        if (Security.isAuthorised(member.getIdLong(), SecurityLevel.DEVELOPER)) {
            return Result.of(PermissionAction.ALLOW, Result.ResultCause.OFFICIAL, SecurityLevel.DEVELOPER)
        }
        if (Security.isAuthorised(member.getIdLong(), SecurityLevel.CONTRIBUTOR) && Environment.isDevelopment()) {
            return Result.of(PermissionAction.ALLOW, Result.ResultCause.OFFICIAL, SecurityLevel.CONTRIBUTOR)
        }
        // If the user is owner then they have all perms, obsv..
        if (member.isOwner()) return Result.of(PermissionAction.ALLOW, Result.ResultCause.GUILD)
        // By default all members with the administrator perm have access to all perms; this can be turned off
        if (member.hasPermission(Permission.ADMINISTRATOR) && settings.getAdminsHaveAllPerms()) {
            return Result.of(PermissionAction.ALLOW, Result.ResultCause.GUILD)
        }
        val user: User = users.computeIfAbsent(member.getIdLong(), { id -> User() })
        // Get all user groups that are directly assigned and the groups assigned through roles
        val userGroups: List<Group> = getUserGroups(member)
        var result: Result = getDefaultAction(permission)
        var evaluatedResult: Result = Result.of(PermissionAction.NEUTRAL)
        if (mode === PermissionMode.MOST_RESTRICTIVE) {
            evaluatedResult = evaluateMostRestrictiveMode(user, userGroups, permission)
        } else if (mode === PermissionMode.HIERARCHICAL) {
            evaluatedResult = evaluateHierarchicalMode(user, userGroups, permission)
        }
        if (!evaluatedResult.isNeutral()) {
            result = evaluatedResult
        }

        // Discord permissions will only allow a permission if is not already allowed or denied.
        // It will not override Cascade permissions!
        if (result.isNeutral() && hasDiscordPermissions(member, channel, permission.getDiscordPerms())) {
            result = Result.of(PermissionAction.ALLOW, Result.ResultCause.DISCORD, permission.getDiscordPerms())
        }
        return result
    }


}