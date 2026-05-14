package org.homesharing.cashbackhome.domain.model

data class AuthenticatedUser(
    val username: String,
    val email: String,
    val birthDate: String,
    val phone: String,
)

fun mockAuthenticatedUser(): AuthenticatedUser =
    AuthenticatedUser(
        username = "krispimil",
        email = "user@example.com",
        birthDate = "8 декабря, 2003",
        phone = "+7 967 933 73 97",
    )
