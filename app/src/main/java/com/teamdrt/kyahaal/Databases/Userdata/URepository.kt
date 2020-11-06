package com.teamdrt.kyahaal.Databases.Userdata

import android.app.Application
import androidx.lifecycle.LiveData
import com.teamdrt.kyahaal.Utils.subscribeOnBackground

class URepository(application: Application) {
    
    private var UDao: UDao
    var getAllData: LiveData<UData>

    private val database = UserDatabase.getInstance(application)

    init {
        UDao = database.UDao()
        getAllData = UDao.getAllData()
    }


    fun insert(userData: UData) {
        subscribeOnBackground {
            UDao.insert(userData)
        }
    }

    fun update(userData: UData) {
        subscribeOnBackground {
            UDao.update(userData)
        }
    }

    fun delete(userData: UData) {
        subscribeOnBackground {
            UDao.delete(userData)
        }
    }

}