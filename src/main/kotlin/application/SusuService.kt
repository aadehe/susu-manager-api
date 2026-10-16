
package com.example.application

import com.example.domain.Member
import com.example.domain.Money
import com.example.domain.Payout
import com.example.domain.SusuGroup

class SusuService(
    weeklyContribution: Money
) {
    private val group = SusuGroup(weeklyContribution)

    fun addMember(member: Member) {
        group.addMember(member)
    }

    fun members(): List<Member> =
        group.members

    fun recordContribution(memberId: String, amount: Money) {
        group.recordContribution(memberId, amount)
    }

    fun currentWeek(): Int =
        group.currentWeek

    fun fullyPaidMembers(): List<Member> =
        group.fullyPaidMembers()

    fun outstandingContributionFor(memberId: String): Money =
        group.outstandingContributionFor(memberId)

    fun closeWeek(): Payout =
        group.closeWeek()

    fun payoutHistory(): List<Payout> =
        group.payoutHistory
}
