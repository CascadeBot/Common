package org.cascadebot.permissions

enum class DiscordPermission(val bitwiseOffset: Int) {

    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    ADMINISTRATOR(3),
    MANAGE_CHANNELS(4),
    MANAGE_GUILD(5),
    ADD_REACTIONS(6),
    VIEW_AUDIT_LOG(7),
    PRIORITY_SPEAKER(8),
    STREAM(9),
    VIEW_CHANNEL(10),
    SEND_MESSAGES(11),
    SEND_TTS_MESSAGES(12),
    MANAGE_MESSAGES(13),
    EMBED_LINKS(14),
    ATTACH_FILES(15),
    READ_MESSAGE_HISTORY(16),
    MENTION_EVERYONE(17),
    USE_EXTERNAL_EMOJIS(18),
    VIEW_GUILD_INSIGHTS(19),
    CONNECT(20),
    SPEAK(21),
    MUTE_MEMBERS(22),
    DEAFEN_MEMBERS(23),
    MOVE_MEMBERS(24),
    USE_VAD(25),
    CHANGE_NICKNAME(26),
    MANAGE_NICKNAMES(27),
    MANAGE_ROLES(28),
    MANAGE_WEBHOOKS(29),
    MANAGE_EMOJIS(30);

    val raw
        get() = 1 shl bitwiseOffset

    companion object {
        fun fromRaw(bitwiseFlags: Int): Set<DiscordPermission> {
            val permissionSet: MutableSet<DiscordPermission> = mutableSetOf()
            for (permission in values()) {
                if (bitwiseFlags and permission.raw != 0) {
                    permissionSet += permission
                }
            }
            return permissionSet.toSet()
        }
    }

}

fun Set<DiscordPermission>.toRaw(): Int {
    var bitwiseFlags = 0
    for (permission in this) {
        bitwiseFlags = bitwiseFlags or permission.raw
    }
    return bitwiseFlags
}