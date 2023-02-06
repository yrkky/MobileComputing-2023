package com.yrkky.mobilecomp.data.entity

data class UserData (
    val name: String = "Janne Yrjänäinen",
    val username: String = "yrkky",
    val password: String = "salasana123"
)

fun getUsername(): String {
    return UserData().username
}

fun getPassword(): String {
    return UserData().password
}

fun getName(): String {
    return UserData().name
}