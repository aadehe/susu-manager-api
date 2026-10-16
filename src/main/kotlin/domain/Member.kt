package com.example.domain

data class Member(val id: String, val name: String) {
    init {
        require(name.isNotBlank()){
            "Member name cannot be blank."
        }

        require(id.isNotBlank()){
            "Member id cannot be blank"
        }
    }
}