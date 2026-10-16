package com.example.domain

import kotlin.test.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MoneyTest {
    @Test
    fun `whole cedis are converted pesewas`() {
        val money = Money.fromCedis(50)
        assertEquals(5_000L, money.pesewas)
    }

    @Test
    fun `money can be created from pesewas`() {
        val money = Money.fromPesewas(2_500)
        assertEquals(2_500L, money.pesewas)
    }

    @Test
    fun `negative pesewas are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Money.fromPesewas(-1)
        }
    }

    @Test
    fun `negative cedis are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            Money.fromCedis(-1)
        }
    }

    @Test
    fun `money can be added exactly`() {
        val first = Money.fromCedis(50)
        val second = Money.fromCedis(100)
        assertEquals(15_000L, (first + second).pesewas)
    }

    @Test
    fun `money can be subtracted without becoming negative`() {
        val first = Money.fromCedis(50)
        val second = Money.fromCedis(20)
        assertEquals(3_000L, (first - second).pesewas)
    }

    @Test
    fun `subtracting more than the available amount is rejected`() {
        val first = Money.fromCedis(20)
        val second = Money.fromCedis(50)
        assertFailsWith<IllegalArgumentException> {
            first - second
        }
    }

    @Test
    fun `money addition rejects arithmetic overflow`() {
        val first = Money.fromPesewas(Long.MAX_VALUE)
        val second = Money.fromPesewas(1)

        assertFailsWith<ArithmeticException> {
            first + second
        }
    }

    @Test
    fun `money reports whether it is positive`() {
        assertTrue(Money.fromCedis(1).isPositive())
    }

    @Test
    fun `factory conversion produces expected pesewas`() {
        assertEquals(5_000L, Money.fromCedis(50).pesewas)
        assertEquals(10_000L, Money.fromCedis(100).pesewas)
    }
}
