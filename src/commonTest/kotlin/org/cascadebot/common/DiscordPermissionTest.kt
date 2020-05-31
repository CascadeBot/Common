package org.cascadebot.common

import org.cascadebot.common.permissions.DiscordPermission
import kotlin.test.Test
import kotlin.test.assertEquals

class DiscordPermissionTest {

    @Test
    fun checkBitwiseOffset() {
        var last = -1
        for (value in DiscordPermission.values()) {
            assertEquals(1, value.bitwiseOffset - last, "${value.name} has invalid bitwise offset!")
            last = value.bitwiseOffset
        }
    }

}