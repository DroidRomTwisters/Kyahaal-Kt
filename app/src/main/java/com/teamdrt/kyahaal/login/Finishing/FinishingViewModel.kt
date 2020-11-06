package com.teamdrt.kyahaal.login.Finishing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.teamdrt.kyahaal.Databases.Userdata.UData
import com.teamdrt.kyahaal.Databases.Userdata.URepository

class FinishingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = URepository(application)
    private val allData = repository.getAllData
    val database = FirebaseDatabase.getInstance()
    private val isuploaded = MutableLiveData<Boolean>()

    fun getAllData(): LiveData<UData> {
        return allData
    }

    private fun insert(userData: UData) {
        repository.insert(userData)
    }

    fun delete(userData: UData) {
        repository.delete(userData)
    }

    fun getIsUpdated(): LiveData<Boolean> {
        return isuploaded
    }

    fun uploadall(
        uid: String,
        phone: String,
        uname: String,
        pplink: String?,
        about: String,
        tid: String,
        lastseen: Long,
        isonline: Boolean,
        istyping: Boolean
    ) {
        val comref = database.reference.child("User").child(uid)
        val map = HashMap<String, Any>()
        map["phone"] = phone
        map["uname"] = uname
        if (pplink == null) {
            map["pplink"] = "default"
        } else {
            map["pplink"] = pplink
        }
        map["about"] = about
        map["tid"] = tid
        map["lastseen"] = lastseen
        map["isonline"] = isonline
        map["istyping"] = istyping
        comref.updateChildren(map).addOnSuccessListener {
            val uData = UData(uid, phone, uname, pplink, about, tid, lastseen, isonline, istyping)
            insert(uData)
            isuploaded.value = true
        }


    }

}