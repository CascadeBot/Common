package org.cascadebot.permissions

expect ty

class CascadePermission private constructor(
    val permissionRaw: String?,
    val defaultPerm: Boolean,
    module: java.lang.Module?,
    vararg discordPerm: DiscordPermission
) {
    val discordPerm: Set<DiscordPermission> = discordPerm.toSet()
    private val module: java.lang.Module?


    // TODO: Make these extension functions on the bot-side
/*    fun getPermission(locale: Locale?): String? {
        return Language.i18n(locale, "permissions.$permissionRaw.name")
    }

    fun getLabel(locale: Locale?): String? {
        return Language.i18n(locale, "permissions.$permissionRaw.label")
    }*/

    override fun toString(): String {
        return permissionRaw!!
    }

    companion object {
        val ALL_PERMISSIONS = of("*", false)
        fun of(permission: String?, defaultPerm: Boolean): CascadePermission? {
            return CascadePermission(permission, defaultPerm, null)
        }

        fun of(permission: String?, defaultPerm: Boolean, module: java.lang.Module?): CascadePermission? {
            return CascadePermission(permission, defaultPerm, module)
        }

        fun of(permission: String?, defaultPerm: Boolean, vararg discordPerm: Permission?): CascadePermission? {
            return CascadePermission(permission, defaultPerm, null, *discordPerm)
        }

        fun of(
            permission: String?,
            defaultPerm: Boolean,
            module: java.lang.Module?,
            vararg discordPerm: Permission?
        ): CascadePermission? {
            return CascadePermission(permission, defaultPerm, module, *discordPerm)
        }
    }

    init {
        this.module = module
    }
}