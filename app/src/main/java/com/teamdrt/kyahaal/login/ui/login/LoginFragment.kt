package com.teamdrt.kyahaal.login.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.login.verification.VerificationFragment


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var isdatavalid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val phoneEditText = view.findViewById<EditText>(R.id.phone)
        val loginButton = view.findViewById<FloatingActionButton>(R.id.login)

        phoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isdatavalid) {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                        ?.replace(
                            R.id.fragment,
                            VerificationFragment.newInstance("+91" + phoneEditText.text.toString()),
                            R.string.verification_tg.toString()
                        )?.commit()
                }
            }
            false
        }

        loginButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                ?.replace(
                    R.id.fragment,
                    VerificationFragment.newInstance("+91" + phoneEditText.text.toString()),
                    R.string.verification_tg.toString()
                )?.commit()
        }
        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                isdatavalid = loginFormState.isDataValid
                loginFormState.phoneError?.let {
                    phoneEditText.error = getString(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    phoneEditText.text.toString()
                )
            }
        }

        phoneEditText.addTextChangedListener(afterTextChangedListener)

    }
}