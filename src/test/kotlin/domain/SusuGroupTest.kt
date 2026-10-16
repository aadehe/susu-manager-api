package com.example.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SusuGroupTest {
    @Test
    fun `group is created with a fixed weekly contribution`() {
        val group = SusuGroup(
            weeklyContribution = Money.fromCedis(50)
        )

        assertEquals(
            Money.fromCedis(50),
            group.weeklyContribution
        )
    }

    @Test
    fun `members are retained in join order`() {
        val group = SusuGroup(
            weeklyContribution = Money.fromCedis(50)
        )
        val ama = Member("member-001", "Ama")
        val kofi = Member("member-002", "Kofi")
        val esi = Member("member-003", "Esi")

        group.addMember(ama)
        group.addMember(kofi)
        group.addMember(esi)

        assertEquals(
            listOf(ama, kofi, esi),
            group.members
        )
    }

    @Test
    fun `duplicate member ID is rejected`() {
        val group = SusuGroup(
            weeklyContribution = Money.fromCedis(50)
        )
        group.addMember(Member("member-001", "Ama"))

        assertFailsWith<IllegalArgumentException> {
            group.addMember(Member("member-001", "Kofi"))
        }
    }

    @Test
    fun `group does not expose a mutable membership list`() {
        val group = SusuGroup(
            weeklyContribution = Money.fromCedis(50)
        )
        group.addMember(Member("member-001", "Ama"))

        val members = group.members

        assertFailsWith<UnsupportedOperationException> {
            (members as MutableList<Member>).add(
                Member("member-002", "Kofi")
            )
        }

        assertEquals(1, group.members.size)
    }

    @Test
    fun `changing a returned membership snapshot does not change the group`() {
        val group = SusuGroup(
            weeklyContribution = Money.fromCedis(50)
        )
        group.addMember(Member("member-001", "Ama"))

        val members = group.members
        val mutableSnapshot = members.toMutableList()
        mutableSnapshot.add(Member("member-002", "Kofi"))

        assertEquals(1, group.members.size)
    }

    @Test
    fun `group rejects a zero weekly contribution`() {
        assertFailsWith<IllegalArgumentException> {
            SusuGroup(
                weeklyContribution = Money.fromPesewas(0)
            )
        }
    }


}