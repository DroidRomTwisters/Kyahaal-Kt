package com.teamdrt.kyahaal.Chat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.teamdrt.kyahaal.Contact.ContactClickListener
import com.teamdrt.kyahaal.Contact.DpDialogFragment
import com.teamdrt.kyahaal.Databases.Chat.Chat
import com.teamdrt.kyahaal.R


class ChatFragment : Fragment(), ContactClickListener {
    private val cl=ArrayList<Chat>()
    companion object {
        fun newInstance() = ChatFragment()
    }


    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ChatViewModelFactory(
                requireActivity().application,
                requireActivity().supportFragmentManager
            )
        ).get(ChatViewModel::class.java)

        viewModel.receiveMessages()

        val recyclerView=view.findViewById<RecyclerView>(R.id.chat_rv)
        val adapter=ChatAdapter(requireContext(), cl, this)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter=adapter
        viewModel.receiveMessages()
        viewModel.getAllChatLIst().observe(viewLifecycleOwner, {
            cl.clear()
            cl.addAll(it)
            adapter.notifyDataSetChanged()

        })
    }

    override fun OndpClick(position: Int) {
        DpDialogFragment.newInstance(cl[position].pplink).show(
            requireActivity().supportFragmentManager,
            ""
        )
    }

}