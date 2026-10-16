package com.example.domain

data class Payout(
    val week: Int,
    val recipient: Member,
    val amount: Money
)