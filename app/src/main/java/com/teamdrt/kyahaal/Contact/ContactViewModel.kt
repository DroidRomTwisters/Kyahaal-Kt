/*
 * Copyright (c) 2020.
 * All rights Reserved
 */
package com.teamdrt.kyahaal.Contact

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.teamdrt.kyahaal.Databases.Contact.Contact
import com.teamdrt.kyahaal.Databases.Contact.ContactRepository
import com.teamdrt.kyahaal.ModalClass.User

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ContactRepository(application)
    private val allContacts = repository.getAllContacts
    val context: Context = getApplication<Application>().applicationContext
    var contactslist = MutableLiveData<ArrayList<ContactModal>>()
    var contactslist2 = MutableLiveData<ArrayList<ContactModal>>()
    val database = FirebaseDatabase.getInstance().reference.child("User")

    fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun update(contact: Contact) {
        repository.update(contact)
    }

    fun delete(contact: Contact) {
        repository.delete(contact)
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        return allContacts
    }

    fun getContactlist(): LiveData<ArrayList<ContactModal>> {
        return contactslist
    }

    private fun setContactlist(contactModal: ArrayList<ContactModal>) {
        contactslist.postValue(contactModal)
    }

    private fun setContactlist2(contactModal: ArrayList<ContactModal>) {
        contactslist2.postValue(contactModal)
    }

    @SuppressLint("Recycle")
    fun getContacts() {
        val contactModal = ArrayList<ContactModal>()
        val contacts: Cursor = context.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )!!

        if (contacts.count > 0) {
            while (contacts.moveToNext()) {
                val hasPhone = Integer.parseInt(
                    contacts.getString(
                        contacts.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    )
                )
                if (hasPhone > 0) {
                    var number =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    number = number.replace("[()\\s-]+".toRegex(), "")
                    if (number.startsWith("0") && number.length==11){
                        number=number.substring(1, 10)
                    }
                    if (number.startsWith("0")) {
                        number = number.substring(1, 10)
                    }
                    val name =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    if (!contactModal.contains(ContactModal(name = name, phone = number))) {
                        contactModal.add(ContactModal(name = name, phone = number))
                    }
                }
            }
        }
        if (!contacts.isClosed) {
            contacts.close()
        }
        setContactlist(contactModal)
    }

    fun getlistofusers(contactModal: ArrayList<ContactModal>) {
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            insert(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            update(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            delete(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun removelistofUsers(contactModal: ArrayList<ContactModal>){
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            delete(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            delete(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uid: String = snapshot.key!!
                    val user: User = snapshot.getValue(User::class.java)!!
                    val pplink: String = user.pplink!!
                    val status: String = user.about!!
                    val mobile: String = user.phone!!
                    for (i in contactModal) {
                        if (i.phone == mobile || "+91" + i.phone == mobile) {
                            val phone: String = mobile
                            val name: String = i.name
                            delete(Contact(uid, phone, name, pplink, status))
                        }
                    }
                }
            }


            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun removeDeletedContacts(){
        val contactModal = ArrayList<ContactModal>()
        val contacts: Cursor = context.contentResolver.query(
            ContactsContract.DeletedContacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )!!


        if (contacts.count > 0) {
            while (contacts.moveToNext()) {
                val hasPhone = Integer.parseInt(
                    contacts.getString(
                        contacts.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    )
                )
                if (hasPhone > 0) {
                    var number =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    number = number.replace("[()\\s-]+".toRegex(), "")
                    if (number.startsWith("0") && number.length==11){
                        number=number.substring(1, 10)
                    }
                    if (number.startsWith("0")) {
                        number = number.substring(1, 10)
                    }
                    val name =
                        contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    if (!contactModal.contains(ContactModal(name = name, phone = number))) {
                        contactModal.add(ContactModal(name = name, phone = number))
                    }
                }
            }
        }
        if (!contacts.isClosed) {
            contacts.close()
        }

        setContactlist2(contactModal)

    }


}