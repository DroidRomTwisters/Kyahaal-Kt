/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.R
import com.vanniktech.emoji.EmojiEditText
import com.vanniktech.emoji.EmojiPopup

private const val ARG_PARAM1 = "uid"
private const val ARG_PARAM2 = "name"
private const val ARG_PARAM3 = "phone"
private const val ARG_PARAM4 = "pplink"
class MessageFragment : Fragment() {

    private var uid: String? = null
    private var name: String? = null
    private var phone: String? = null
    private var pplink: String? = null

    companion object {
        fun newInstance(uid: String,name:String?,phone:String,pplink:String) = MessageFragment().apply {
            arguments=Bundle().apply {
                putString(ARG_PARAM1, uid)
                putString(ARG_PARAM2,name)
                putString(ARG_PARAM3,phone)
                putString(ARG_PARAM4,pplink)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(ARG_PARAM1)
            name=it.getString(ARG_PARAM2)
            phone=it.getString(ARG_PARAM3)
            pplink=it.getString(ARG_PARAM4)
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.message_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,MessageViewModelFactory(requireActivity().application,FirebaseAuth.getInstance().currentUser?.uid!!,uid!!,requireActivity().supportFragmentManager)).get(MainViewModel::class.java)
        viewModel.receiveMessages()
        viewModel.clearuc()
        val mrv=view.findViewById<RecyclerView>(R.id.message_rv)
        val message=view.findViewById<EmojiEditText>(R.id.message)
        val emojiPopup=EmojiPopup.Builder.fromRootView(view).build(message)
        val send=view.findViewById<FloatingActionButton>(R.id.send)
        val ml= ArrayList<Message>()
        val adapter=MessageAdapter(requireContext(),ml)
        val emojibtn=view.findViewById<ImageView>(R.id.emoji)
        mrv.layoutManager=LinearLayoutManager(requireContext())
        mrv.adapter=adapter
        send.setOnClickListener{
            if (!message.text.isNullOrEmpty()) {
                viewModel.sendMessage(message.text.toString().trim(), uid!!,name!!,phone!!,pplink!!)
                message.text= null
            }
        }
        viewModel.getAllChats()?.observe(viewLifecycleOwner,{
            if (it!=null) {
                ml.clear()
                ml.addAll(it)
                adapter.notifyDataSetChanged()
                mrv.scrollToPosition(ml.size - 1)
            }
        })

        emojibtn.setOnClickListener{
            if (emojiPopup.isShowing){
                emojiPopup.dismiss()
            }else {
                emojiPopup.toggle()
            }
        }

        message.setOnClickListener{
            if (emojiPopup.isShowing) {
                emojiPopup.dismiss()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearuc()
    }
}

