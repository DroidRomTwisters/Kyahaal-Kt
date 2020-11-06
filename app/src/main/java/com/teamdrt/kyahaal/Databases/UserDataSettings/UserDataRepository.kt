package com.teamdrt.kyahaal.Databases.UserDataSettings

import android.app.Application
import androidx.lifecycle.LiveData
import com.teamdrt.kyahaal.Utils.subscribeOnBackground

class UserDataRepository(application: Application) {
    private var userdao: UserDao
    var getAllData: LiveData<UserData>

    private val database = AppDatabase.getInstance(application)

    init {
        userdao = database.UserDao()
        getAllData = userdao.getAllData()
    }


    fun insert(userData: UserData) {
        subscribeOnBackground {
            userdao.insert(userData)
        }

    }

    fun update(userData: UserData) {
        subscribeOnBackground {
            userdao.update(userData)
        }
    }

    fun delete(userData: UserData) {
        subscribeOnBackground {
            userdao.delete(userData)
        }
    }

}