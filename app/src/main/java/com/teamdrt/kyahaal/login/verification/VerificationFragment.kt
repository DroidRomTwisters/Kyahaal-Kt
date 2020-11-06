package com.teamdrt.kyahaal.login.verification

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.login.UserDataActivity.UserDataFragment
import com.teamdrt.kyahaal.login.ui.login.LoginViewModel
import com.teamdrt.kyahaal.login.ui.login.LoginViewModelFactory
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var loginViewModel: LoginViewModel
private lateinit var auth: FirebaseAuth
const val TIME_OUT = 60
var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
var token: PhoneAuthProvider.ForceResendingToken? = null
var isdatavalid = false


class VerificationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    companion object {
        fun newInstance(param1: String) =
            VerificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val codetv = view.findViewById<EditText>(R.id.tv)
        val verify = view.findViewById<FloatingActionButton>(R.id.verify)
        var verificationCode: String? = null
        val pb = view.findViewById<ProgressBar>(R.id.progressBar)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity).edit()
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)


        codetv.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isdatavalid) {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                        ?.replace(
                            R.id.fragment,
                            UserDataFragment.newInstance(param1!!),
                            R.string.udata_tg.toString()
                        )?.commit()
                }
            }
            false
        }
        loginViewModel.loginResult.observe(viewLifecycleOwner, {
            if (it.success) {
                pb.visibility = GONE
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                sharedPref.putBoolean(resources.getString(R.string.login_completion_state), true)
                sharedPref.putBoolean(resources.getString(R.string.isDataUpdated), false)
                sharedPref.putString("number", param1)
                sharedPref.apply()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                    ?.replace(
                        R.id.fragment,
                        UserDataFragment.newInstance(param1!!),
                        R.string.udata_tg.toString()
                    )?.commit()
            }

            if (it.error != null) {
                pb.visibility = GONE
                Toast.makeText(activity, it.error, Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { it ->
                if (it == null) {
                    return@Observer
                }

                verify.isEnabled = it.isDataValid
                isdatavalid = it.isDataValid
                it.codeError?.let {
                    codetv.error = getString(it)
                }
            })
        auth = FirebaseAuth.getInstance()
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                loginViewModel.login(
                    p0.smsCode!!,
                    codetv.text.toString(),
                    requireActivity()
                )
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(activity!!, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationCode = p0
                token = p1
                Toast.makeText(activity, "Code Sent", Toast.LENGTH_SHORT).show()
            }

        }

        if (param1?.isNotEmpty()!!) {
            val option = PhoneAuthOptions.newBuilder()
            option.apply {
                setPhoneNumber(param1!!)
                setTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                activity?.let { setActivity(it) }
                setCallbacks(mCallBack!!)
            }
            val options = option.build()
            param1?.let {
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.verificationCodeChnaged(
                    codetv.text.toString()
                )
            }
        }

        codetv.addTextChangedListener(afterTextChangedListener)

        verify.setOnClickListener {
            pb.visibility = VISIBLE
            verificationCode?.let { it1 ->
                activity?.let { it2 ->
                    loginViewModel.login(
                        it1,
                        codetv.text.toString(),
                        it2
                    )
                }
            }
        }

    }


}