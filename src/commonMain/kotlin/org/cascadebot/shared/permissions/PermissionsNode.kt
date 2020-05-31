package org.cascadebot.shared.permissions

// All credit to the FlareBot project for this file
// https://github.com/FlareBot/FlareBot/blob/master/src/main/java/stream/flarebot/flarebot/permissions/PermissionNode.java
class PermissionNode(val node: String) {

    operator fun invoke(permission: String): Boolean {
        if (node == "*") return true
        val textNode: String =
            node.split(Regex("""(?:^\*(\.))|(?:(?<=\.)\*(?=\.))|(?:(?<=\.)\*$)"""))
                .joinToString(".+", transform = Regex.Companion::escape) + if (node.endsWith("*")) ".+" else ""
        return permission.matches(Regex(textNode))
    }

}