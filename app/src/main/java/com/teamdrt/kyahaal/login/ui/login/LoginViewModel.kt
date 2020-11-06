package com.teamdrt.kyahaal.login.ui.login

import android.app.Activity
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCanceledListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.teamdrt.kyahaal.R

class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val phone = MutableLiveData<String>()

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    private var auth: FirebaseAuth? = null

    fun login(verificationcode: String, tvtext: String, activity: Activity) {
        // can be launched in a separate asynchronous job
        auth = FirebaseAuth.getInstance()
        val credential = PhoneAuthProvider.getCredential(verificationcode, tvtext)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                if (it.isSuccessful && it.isComplete) {
                    _loginResult.value =
                        LoginResult(success = true)
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
            ?.addOnCanceledListener {
                OnCanceledListener {
                }
            }
    }

    fun loginDataChanged(phone: String) {
        if (!isPhoneValid(phone)) {
            _loginForm.value = LoginFormState(phoneError = R.string.invalid_number)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun verificationCodeChnaged(verificationcode: String) {
        if (!isCodevalid(verificationcode)) {
            _loginForm.value = LoginFormState(codeError = R.string.invalid_code)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.length == 10 && Patterns.PHONE.matcher(phone).matches()
    }

    private fun isCodevalid(code: String): Boolean {
        return code.length == 6
    }

}