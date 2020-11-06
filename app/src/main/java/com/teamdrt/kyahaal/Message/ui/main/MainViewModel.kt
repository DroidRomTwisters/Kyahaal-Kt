/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.teamdrt.kyahaal.Databases.Chat.Chat
import com.teamdrt.kyahaal.Databases.Chat.ChatRepository
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.Databases.Message.MessageRepository
import com.teamdrt.kyahaal.R
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(
    application: Application,
    val uid1: String,
    val uid2: String,
    val fm: FragmentManager
) :
    AndroidViewModel(application) {

    private val repository = MessageRepository(application, uid1, uid2)
    private val repository2 = ChatRepository(application, null)


    fun insert(Message: Message) {
        repository.insert(Message)
    }

    fun update(Message: Message) {
        repository.update(Message)
    }

    fun delete(Message: Message) {
        repository.delete(Message)
    }

    fun getAllChats(): LiveData<List<Message>>? {
        return repository.getAllMessages
    }

    fun insert2(chat: Chat) {
        repository2.insert(chat)
    }

    fun update2(chat: Chat) {
        repository2.update(chat)
    }

    fun delete2(chat: Chat) {
        repository2.delete(chat)
    }


    fun clearuc() {
        repository2.clearuc(uid2)
    }

    fun sendMessage(message: String, cuid: String, name: String?, phone: String, pplink: String) {
        val timestaamp = Date().time
        val mchatRef =
            FirebaseDatabase.getInstance().getReference("User").child(cuid).child("messages")
        val usermessagepush: DatabaseReference = mchatRef.child("messages").push()
        val push_id = usermessagepush.key
        val map = HashMap<String, Any?>()
        map["msg_type"] = "text"
        map["msg"] = message
        map["from_uid"] = FirebaseAuth.getInstance().currentUser?.uid!!
        map["to_uid"] = cuid
        map["sent_timestamp"] = timestaamp
        map["received_timestamp"] = null
        map["push_id"] = push_id
        mchatRef.child(push_id!!).updateChildren(map).addOnSuccessListener {
            insert(
                Message(
                    push_id,
                    "text",
                    message,
                    FirebaseAuth.getInstance().currentUser?.uid!!,
                    cuid,
                    null,
                    timestaamp,
                    null
                )
            )
            insert2(Chat(uid2, name!!, phone, pplink, message, true, 0, timestaamp))
        }
    }

    fun receiveMessages(name: String?, phone: String, pplink: String) {
        val timestaamp = Date().time
        val mchatRef =
            FirebaseDatabase.getInstance().getReference("User").child(uid1).child("messages")
        mchatRef.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message = snapshot.getValue(Message::class.java)!!
                message.received_timestamp = timestaamp
                insert(message)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (isvisibleornot()) {
                    clearuc()
                }

            }


            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    fun isvisibleornot(): Boolean {
        val fragment: MessageFragment? =
            fm.findFragmentById(R.id.fragment_container) as MessageFragment?
        return fragment != null && fragment.isVisible
    }
}