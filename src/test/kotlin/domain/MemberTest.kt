package com.example.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MemberTest {
    @Test
    fun `member can be created with valid ID and name`() {
        val member = Member(id = "member-001", name = "Ama")

        assertEquals("member-001", member.id)
        assertEquals("Ama", member.name)
    }

    @Test
    fun `blank member ID is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Member(id = " ", name = "Ama")
        }
    }

    @Test
    fun `blank member name is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Member(id = "member-001", name = " ")
        }
    }
}