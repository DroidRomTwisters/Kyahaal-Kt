package com.teamdrt.kyahaal.login.UserDataActivity

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teamdrt.kyahaal.Databases.UserDataSettings.UserData
import com.teamdrt.kyahaal.Databases.UserDataSettings.UserDataRepository
import com.teamdrt.kyahaal.ModalClass.User
import java.io.File
import java.util.*


class UserDataViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserDataRepository(application)
    private val allData = repository.getAllData
    val oldfname = MutableLiveData<String>()
    val durl=MutableLiveData<String>()
    var isNameValid=MutableLiveData<Boolean>()
    val database = FirebaseDatabase.getInstance()

    private fun insert(userData: UserData) {
        repository.insert(userData)
    }


    fun delete(userData: UserData) {
        repository.delete(userData)
    }


    private fun update(userData: UserData) {
        repository.update(userData)
    }


    fun getAllData(): LiveData<UserData> {
        return allData
    }

    private fun setoldfname(oldfname: String?) {
        this.oldfname.value = oldfname
    }

    fun setDUrl(url: String){
       durl.value=url
    }

    fun uploadbytes(@NonNull uri: Uri, oldfname: String?, fname: String, auth: FirebaseAuth) {
        val userData = auth.uid?.let { UserData(oldfname, Date().time, true, it) }
        insert(userData!!)
        val dpref = imageReference!!.child(auth.currentUser!!.uid + ".jpeg")
        dpref.putFile(uri)
            .addOnSuccessListener { it ->
                it.storage.downloadUrl.addOnSuccessListener {
                    setDUrl(it.toString())
                    setoldfname(oldfname)
                    userData.dpname = fname
                    userData.isUploading = false
                    update(userData)
                }
            }
    }


    fun NameChanged(name: String){
        isNameValid .value= name.isNotEmpty()
    }

    fun checkifdplinkexists(auth: FirebaseAuth, fname: String?,file: File,durl:String?){
        val dpref=database.reference.child("User")
        dpref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.hasChild(auth.uid!!)) {


                    val user: User = snapshot.child(auth.uid!!).getValue(User::class.java)!!
                    user.pplink?.let { Log.i("TAG", it) }
                    if (user.pplink!=null && user.pplink!="default"){
                        val dpref = imageReference!!.child(auth.currentUser!!.uid + ".jpeg")
                        dpref.getFile(file)
                            .addOnSuccessListener {
                                if (durl==null && durl!=user.pplink) {
                                    val userData = auth.uid?.let { it2 ->
                                        UserData(
                                            fname,
                                            Date().time,
                                            false,
                                            it2
                                        )
                                    }
                                    insert(userData!!)
                                    setDUrl(user.pplink!!)
                                }
                             }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}