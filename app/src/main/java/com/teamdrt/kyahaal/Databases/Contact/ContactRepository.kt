package com.teamdrt.kyahaal.Databases.Contact

import android.app.Application
import androidx.lifecycle.LiveData

import com.teamdrt.kyahaal.Utils.subscribeOnBackground

class ContactRepository(application: Application) {
    
    private var ContactDao : ContactDao
    var getAllContacts:LiveData<List<Contact>>
    
    private val database=ContactDatabase.getInstance(application)
    
    init {
        ContactDao=database.ContactDao()
        getAllContacts=ContactDao.getAllData()
    }

    fun insert(contact: Contact) {
        subscribeOnBackground {
            ContactDao.insert(contact)
        }

    }

    fun update(contact: Contact) {
        subscribeOnBackground {
            ContactDao.update(contact)
        }
    }

    fun delete(contact: Contact) {
        subscribeOnBackground {
            ContactDao.delete(contact)
        }
    }

}