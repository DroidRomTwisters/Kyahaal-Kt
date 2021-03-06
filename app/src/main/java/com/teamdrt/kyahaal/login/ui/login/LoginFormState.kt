package com.teamdrt.kyahaal.login.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val isDataValid: Boolean = false,
    val phoneError: Int? = null,
    val codeError: Int? = null
)