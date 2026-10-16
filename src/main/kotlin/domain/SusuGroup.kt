package com.example.domain

class SusuGroup(val weeklyContribution: Money) {
    var currentWeek: Int = 1
        private set

    private val payouts = mutableListOf<Payout>()

    private val memberList = mutableListOf<Member>()

    private val contributionsByWeek =
        mutableMapOf<Int, MutableMap<String, Money>>()


    val members: List<Member>
        get() = memberList.toList()

    val payoutHistory: List<Payout>
        get() = payouts.toList()

    init {
        require(weeklyContribution.isPositive()){
            "Weekly contribution must be greater than zero!"
        }
    }

    fun addMember(member: Member) {
        check(
            currentWeek == 1 &&
                    contributionsByWeek[currentWeek]?.values?.none {
                        it.isPositive()
                    } != false
        ) {
            "Members cannot be added after contributions have started or the first week has closed."
        }

        require(memberList.none { it.id == member.id }) {
            "Member with id '${member.id}' already exist!"
        }

        memberList.add(member)
        contributionsByWeek
            .getOrPut(currentWeek) { mutableMapOf() }[member.id] =
            Money.fromPesewas(0)
    }

    fun recordContribution(memberId: String, amount: Money) {
        require(memberList.any { it.id == memberId }) {
            "Member with id '$memberId' does not exist!"
        }
        require(amount.isPositive()) {
            "Contribution amount must be greater than zero!"
        }

        val currentPaid = contributionPaidBy(memberId)
        val updatedPaid = currentPaid + amount

        require(updatedPaid.pesewas <= weeklyContribution.pesewas) {
            "Contribution would exceed the weekly contribution amount."
        }

        contributionsByWeek
            .getValue(currentWeek)[memberId] = updatedPaid
    }

    fun contributionPaidBy(memberId: String): Money {
        require(memberList.any { it.id == memberId }) {
            "Member with id '$memberId' does not exist!"
        }

        return contributionsByWeek
            .getValue(currentWeek)
            .getValue(memberId)
    }

    fun outstandingContributionFor(memberId: String): Money {
        return weeklyContribution - contributionPaidBy(memberId)
    }

    fun fullyPaidMembers(): List<Member> {
        return memberList.filter { member ->
            contributionPaidBy(member.id) == weeklyContribution
        }
    }

    fun closeWeek(): Payout {
        check(memberList.isNotEmpty()) {
            "Cannot close a week without any members."
        }

        val outstandingDues = memberList.mapNotNull { member ->
            val outstanding = outstandingContributionFor(member.id)

            if (outstanding.isPositive()) {
                "${member.name}: ${outstanding.pesewas} pesewas outstanding"
            } else {
                null
            }
        }

        check(outstandingDues.isEmpty()) {
            "Cannot close week $currentWeek. Outstanding dues: " +
                    outstandingDues.joinToString("; ")
        }

        val totalPot = memberList.fold(Money.fromPesewas(0)) { total, member ->
            total + contributionPaidBy(member.id)
        }

        val recipientIndex = payouts.size % memberList.size
        val payout = Payout(
            week = currentWeek,
            recipient = memberList[recipientIndex],
            amount = totalPot
        )

        payouts.add(payout)
        currentWeek++

        contributionsByWeek[currentWeek] = memberList.associate { member ->
            member.id to Money.fromPesewas(0)
        }.toMutableMap()

        return payout
    }

}