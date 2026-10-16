package com.example.domain

class SusuGroup(val weeklyContribution: Money) {
    private val memberList = mutableListOf<Member>()
    private val contributionsByMember = mutableMapOf<String, Money>()


    val members: List<Member>
        get() = memberList.toList()

    init {
        require(weeklyContribution.isPositive()){
            "Weekly contribution must be greater than zero!"
        }
    }

    fun addMember(member: Member) {
        require(memberList.none { it.id == member.id }) {
            "Member with id '${member.id}' already exist!"
        }

        memberList.add(member)
        contributionsByMember[member.id] = Money.fromPesewas(0)
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

        contributionsByMember[memberId] = updatedPaid
    }

    fun contributionPaidBy(memberId: String): Money {
        require(memberList.any { it.id == memberId }) {
            "Member with id '$memberId' does not exist!"
        }

        return contributionsByMember.getValue(memberId)
    }

    fun outstandingContributionFor(memberId: String): Money {
        return weeklyContribution - contributionPaidBy(memberId)
    }


}