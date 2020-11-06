package com.teamdrt.kyahaal.login.UserDataActivity

import android.Manifest.permission
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mikhaellopez.circularimageview.CircularImageView
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.login.Finishing.FinishingFragment
import com.yalantis.ucrop.UCrop
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var easyImge: EasyImage? = null
var dpimage: CircularImageView? = null
const val CROP_IMAGE_REQUEST_CODE = 280
var sharedPref: SharedPreferences? = null
var fname: String? = null
var imageReference: StorageReference? = null
lateinit var auth: FirebaseAuth
var pb: ProgressBar? = null
private lateinit var viewModel: UserDataViewModel
var oldfnmae: String? = null
var isdatavalid = false
var uname: EditText? = null

class UserDataFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        fun newInstance(param1: String) =
            UserDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

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
        return inflater.inflate(R.layout.user_data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //declaration
        val phone = view.findViewById<TextView>(R.id.unumber)
        val changedp = view.findViewById<FloatingActionButton>(R.id.changedp)
        isStoragePermissionGranted()
        uname = view.findViewById(R.id.uname)
        val done = view.findViewById<FloatingActionButton>(R.id.done)

        auth = FirebaseAuth.getInstance()
        viewModel =
            ViewModelProvider(this, UserDataViewModelFactory(requireActivity().application)).get(
                UserDataViewModel::class.java
            )
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        fname = "/me" + System.currentTimeMillis() + ".jpeg"
        val file = File(requireActivity().filesDir, fname!!)
        viewModel.checkifdplinkexists(auth, fname,file, sharedPref?.getString("durl",null))
        viewModel.getAllData().observe(viewLifecycleOwner, { udata ->
            if (udata != null) {
                if (udata.isUploading) {
                    changedp.isEnabled = !udata.isUploading
                    pb?.visibility = VISIBLE
                } else {
                    pb?.visibility = GONE
                }
                changedp.isEnabled = !udata.isUploading
                if (udata.dpname != null) {
                    val file = File(requireActivity().filesDir, udata.dpname!!)
                    if (file.exists()) {
                        oldfnmae = udata.dpname
                        Glide.with(this).load(file).thumbnail(0.1f).into(dpimage!!)
                    }
                }
            }
        })

        viewModel.oldfname.observe(viewLifecycleOwner, { oldfname ->
            if (oldfname != null) {
                val file = File(requireActivity().filesDir, oldfname)
                if (file.exists()) {
                    file.delete()
                }
            }
        })

        viewModel.durl.observe(viewLifecycleOwner, { durl ->
            if (durl != null) {
                sharedPref?.edit()?.putString("durl", durl)?.apply()
            }

        })

        viewModel.isNameValid.observe(viewLifecycleOwner, {
            isdatavalid = it
            done.isEnabled = it
        })

        pb = view.findViewById(R.id.dp_uploading)
        dpimage = view.findViewById(R.id.dpimage)
        imageReference =
            FirebaseStorage.getInstance().reference.child(resources.getString(R.string.dp_ref_dir_name))

        if (param1.isNullOrEmpty()) {
            param1 = sharedPref?.getString("number", null)
            phone.text = sharedPref?.getString("number", null)
        } else {
            phone.text = param1
        }

        changedp.setOnClickListener {
            if (isStoragePermissionGranted()) {
                easyImge = EasyImage.Builder(requireContext()).allowMultiple(false).build()
                easyImge!!.openChooser(this)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Grant Camera & Storage Permission to proceed further ",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.NameChanged(s.toString())
            }

        }

        uname?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (isdatavalid) {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                        ?.replace(
                            R.id.fragment,
                            FinishingFragment.newInstance(
                                auth.uid!!, param1!!, uname?.text.toString(), sharedPref?.getString(
                                    "durl",
                                    null
                                ), resources.getString(R.string.default_status)
                            ),
                            R.string.finishingfrag_tg.toString()
                        )?.commit()
                }
            }
            false
        }

        uname?.addTextChangedListener(afterTextChangedListener)

        done.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                ?.replace(
                    R.id.fragment,
                    FinishingFragment.newInstance(
                        auth.uid!!, param1!!, uname?.text.toString(), sharedPref?.getString(
                            "durl",
                            null
                        ), resources.getString(R.string.default_status)
                    ),
                    R.string.finishingfrag_tg.toString()
                )?.commit()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImge?.handleActivityResult(
            requestCode,
            resultCode,
            data,
            requireActivity(),
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    fname = "/me" + System.currentTimeMillis() + ".jpeg"
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                    val file = File(requireActivity().filesDir, fname!!)
                    val uris = Uri.fromFile(file)
                    val uri2 = Uri.fromFile(imageFiles[0].file)
                    val options = UCrop.Options()
                    options.apply {
                        setCompressionQuality(70)
                        setCompressionFormat(Bitmap.CompressFormat.JPEG)
                    }
                    UCrop.of(uri2, uris).withAspectRatio(1F, 1F).withMaxResultSize(640, 640)
                        .withOptions(options).start(
                            requireContext(), this@UserDataFragment,
                            CROP_IMAGE_REQUEST_CODE
                        )

                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {

                }
            }
        )

        if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = data?.let { UCrop.getOutput(it) }
            if (uri != null) {
                viewModel.uploadbytes(uri, oldfnmae, fname!!, auth)
            }
        }

    }

    private fun getUsername(): String? {
        val c: Cursor? = requireActivity().contentResolver.query(
            ContactsContract.Profile.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        c?.moveToFirst()

        val name = c?.getString(c.getColumnIndex("display_name"))
        c?.close()
        return name
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return if (requireContext().checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && requireContext().checkSelfPermission(permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&requireContext().checkSelfPermission(permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission.WRITE_EXTERNAL_STORAGE,
                        permission.CAMERA,permission.READ_CONTACTS),
                    2
                )
                false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return if (requireContext().checkSelfPermission(permission.ACCESS_MEDIA_LOCATION)
                == PackageManager.PERMISSION_GRANTED && requireContext().checkSelfPermission(permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && requireContext().checkSelfPermission(permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission.ACCESS_MEDIA_LOCATION,
                        permission.CAMERA,permission.READ_CONTACTS),
                    2
                )
                false
            }
        } else {
            return true
        }
    }


}