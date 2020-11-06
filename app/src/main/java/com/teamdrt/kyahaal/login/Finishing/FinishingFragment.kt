package com.teamdrt.kyahaal.login.Finishing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.teamdrt.kyahaal.Chat.ChatActivity
import com.teamdrt.kyahaal.R
import java.util.*


private const val ARG_PARAM1 = "uid"
private const val ARG_PARAM2 = "number"
private const val ARG_PARAM3 = "name"
private const val ARG_PARAM4 = "dplink"
private const val ARG_PARAM5 = "status"

class FinishingFragment : Fragment() {

    private var uid: String? = null
    private var number: String? = null
    private var name: String? = null
    private var dplink: String? = null
    private var status: String? = null


    companion object {
        fun newInstance(uid: String, number: String, name: String, dplink: String?, status: String) =
            FinishingFragment().apply {
                arguments=Bundle().apply {
                    putString(ARG_PARAM1, uid)
                    putString(ARG_PARAM2, number)
                    putString(ARG_PARAM3, name)
                    putString(ARG_PARAM4, dplink)
                    putString(ARG_PARAM5, status)
                }
            }
    }

    private lateinit var viewModel: FinishingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.finishing_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid=it.getString(ARG_PARAM1)
            number=it.getString(ARG_PARAM2)
            name=it.getString(ARG_PARAM3)
            dplink=it.getString(ARG_PARAM4)
            status=it.getString(ARG_PARAM5)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result!!.token
                    viewModel = ViewModelProvider(
                        this,
                        FinishingViewMOdelFactory(requireActivity().application)
                    ).get(FinishingViewModel::class.java)
                    viewModel.uploadall(
                        uid!!,
                        number!!,
                        name!!,
                        dplink,
                        status!!,
                        idToken!!,
                        Date().time,
                        isonline = false,
                        istyping = false
                    )

                    viewModel.getIsUpdated().observe(viewLifecycleOwner, {
                        if (it) {
                            val sharedPref =
                                PreferenceManager.getDefaultSharedPreferences(requireContext())
                                    .edit()
                            sharedPref.putBoolean(
                                resources.getString(R.string.login_completion_state),
                                false
                            )
                            sharedPref.apply()
                            val intent = Intent(activity, ChatActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    })
                }
            }

    }




}