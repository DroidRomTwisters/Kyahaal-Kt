/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Contact

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.behavior.SwipeDismissBehavior
import com.teamdrt.kyahaal.R


private var imageurl=""
class DpDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(imageurl: String) = DpDialogFragment().apply {
            arguments=Bundle().apply {
                putString("url", imageurl)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageurl= it.getString("url")!!
        }
    }
    private lateinit var viewModel: DpDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.dp_dialog_fragment, container, false)
        this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        Glide.with(this).load(imageurl).thumbnail(0.1f).into(view.findViewById(R.id.dp_view))
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DpDialogViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        val dialog=dialog
        dialog?.window?.attributes?.windowAnimations=R.style.DialogFragmentAnimation
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.dp_view).setOnClickListener(View.OnClickListener {
            val intent = Intent(requireActivity(), FullScreenDpActivity::class.java)
            intent.putExtra("dpurl", imageurl)
            startActivity(intent)
            dialog?.dismiss()
        })
    }


}