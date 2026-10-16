package com.example.domain

class SusuGroup(val weeklyContribution: Money) {
    private val memberList = mutableListOf<Member>()

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
    }


}