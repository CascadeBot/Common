package org.cascadebot.common

import org.cascadebot.common.permissions.PermissionNode
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PermissionNodeTest {

    @Test
    fun testWildcardNodes() {
        assertTrue(PermissionNode("*")("hello.hi"))
        assertTrue(PermissionNode("hello.*")("hello.hi"))
        assertTrue(PermissionNode("*")("hi"))

        assertFalse(PermissionNode("hello.*")("hello"))
    }

    @Test
    fun testNormalNodes() {
        assertTrue(PermissionNode("hi.there")("hi.there"))
        assertTrue(PermissionNode("hi")("hi"))

        assertFalse(PermissionNode("somethingelse")("notsomethingelse"))
    }

}