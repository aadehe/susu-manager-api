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

    @Test
    fun `member can make a partial contribution`() {
        val group = SusuGroup(Money.fromCedis(50))
        val ama = Member("member-001", "Ama")
        group.addMember(ama)

        group.recordContribution(ama.id, Money.fromCedis(30))

        assertEquals(
            Money.fromCedis(30),
            group.contributionPaidBy(ama.id)
        )
        assertEquals(
            Money.fromCedis(20),
            group.outstandingContributionFor(ama.id)
        )
    }

    @Test
    fun `multiple partial payments are accumulated`() {
        val group = SusuGroup(Money.fromCedis(50))
        val ama = Member("member-001", "Ama")
        group.addMember(ama)

        group.recordContribution(ama.id, Money.fromCedis(20))
        group.recordContribution(ama.id, Money.fromCedis(30))

        assertEquals(
            Money.fromCedis(50),
            group.contributionPaidBy(ama.id)
        )
        assertEquals(
            Money.fromCedis(0),
            group.outstandingContributionFor(ama.id)
        )
    }

    @Test
    fun `contribution from an unknown member is rejected`() {
        val group = SusuGroup(Money.fromCedis(50))

        assertFailsWith<IllegalArgumentException> {
            group.recordContribution(
                "unknown-member",
                Money.fromCedis(10)
            )
        }
    }

    @Test
    fun `zero contribution is rejected`() {
        val group = SusuGroup(Money.fromCedis(50))
        val ama = Member("member-001", "Ama")
        group.addMember(ama)

        assertFailsWith<IllegalArgumentException> {
            group.recordContribution(
                ama.id,
                Money.fromCedis(0)
            )
        }
    }

    @Test
    fun `overpayment is rejected`() {
        val group = SusuGroup(Money.fromCedis(50))
        val ama = Member("member-001", "Ama")
        group.addMember(ama)

        group.recordContribution(ama.id, Money.fromCedis(30))

        assertFailsWith<IllegalArgumentException> {
            group.recordContribution(ama.id, Money.fromCedis(25))
        }

        assertEquals(
            Money.fromCedis(30),
            group.contributionPaidBy(ama.id)
        )
    }



}