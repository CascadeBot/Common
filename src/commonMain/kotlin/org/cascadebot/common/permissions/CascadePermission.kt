package org.cascadebot.common.permissions

import org.cascadebot.common.Module

class CascadePermission private constructor(
    val permissionRaw: String,
    val defaultPerm: Boolean,
    val module: Module? = null,
    vararg discordPerm: DiscordPermission = arrayOf()
) {
    val discordPerm: Set<DiscordPermission> = discordPerm.toSet()


    // TODO: Make these extension functions on the bot-side
/*    fun getPermission(locale: Locale?): String? {
        return Language.i18n(locale, "permissions.$permissionRaw.name")
    }

    fun getLabel(locale: Locale?): String? {
        return Language.i18n(locale, "permissions.$permissionRaw.label")
    }*/

    override fun toString(): String {
        return permissionRaw
    }

    companion object {
        val ALL_PERMISSIONS = CascadePermission("*", false)
    }

}