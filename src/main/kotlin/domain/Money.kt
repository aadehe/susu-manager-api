package com.example.domain

/**
 * Represents a monetary amount in Ghana cedis using integer pesewas.
 * For example, GHS 50.00 is represented as 5,000 pesewas.
 */

@JvmInline
value class Money private constructor(val pesewas: Long) {
    operator fun plus(other: Money): Money =
        Money(Math.addExact(pesewas, other.pesewas))

    operator fun minus(other: Money): Money {
        val result = Math.subtractExact(pesewas, other.pesewas)
        require(result >= 0){"Money can't be negative"}
        return Money(result)
    }

    fun isPositive(): Boolean = pesewas > 0

    companion object {
        fun fromPesewas(pesewas: Long): Money{
            require(pesewas >= 0) { "Money cannot be negative" }
            return Money(pesewas)
        }

        fun fromCedis(cedis: Long): Money{
            require(cedis >= 0) { "Money cannot be negative" }
            return Money(Math.multiplyExact(cedis, 100L))
        }
    }
}