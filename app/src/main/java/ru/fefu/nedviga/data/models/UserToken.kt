package ru.fefu.nedviga.data.models

data class UserToken (
    val token: String,
    val user: User
)