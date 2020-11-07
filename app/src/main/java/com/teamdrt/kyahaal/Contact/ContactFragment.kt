package com.teamdrt.kyahaal.Contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdrt.kyahaal.Databases.Contact.Contact
import com.teamdrt.kyahaal.R
import java.sql.Array

class ContactFragment : Fragment(),ContactClickListener {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private var contactlist=ArrayList<Contact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, ContactVIewModelFactory(requireActivity().application)).get(
                ContactViewModel::class.java
            )
        viewModel.getContacts()
        viewModel.contactslist.observe(viewLifecycleOwner, {
            viewModel.getlistofusers(it)
        })
        viewModel.contactslist2.observe(viewLifecycleOwner,{
            viewModel.removelistofUsers(it)
        })
        viewModel.getAllContacts().observe(viewLifecycleOwner, {
            contactlist.clear()
            contactlist.addAll(it)
            adapter.notifyDataSetChanged()
        })
        adapter=ContactAdapter(requireContext(),contactlist,this)
        recyclerView=view.findViewById(R.id.contact_rv)
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

    }

    override fun OndpClick(position: Int) {
        DpDialogFragment.newInstance(contactlist[position].pplink).show(requireActivity().supportFragmentManager,"")
    }

}