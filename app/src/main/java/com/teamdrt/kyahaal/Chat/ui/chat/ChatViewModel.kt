package com.teamdrt.kyahaal.Chat.ui.chat

import android.app.Application
import android.media.MediaPlayer
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.teamdrt.kyahaal.Databases.Chat.Chat
import com.teamdrt.kyahaal.Databases.Chat.ChatDao
import com.teamdrt.kyahaal.Databases.Chat.ChatDatabase
import com.teamdrt.kyahaal.Databases.Chat.ChatRepository
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.Databases.Message.MessageRepository
import com.teamdrt.kyahaal.Message.ui.main.MessageFragment
import com.teamdrt.kyahaal.ModalClass.User
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.Utils.subscribeOnBackground
import kotlinx.coroutines.launch
import java.util.*


class ChatViewModel(application: Application,val fm: FragmentManager) : AndroidViewModel(application) {

    private val repository = MessageRepository(application, null, null)
    private val repository2 = ChatRepository(application, null)
    private var chatDao: ChatDao = ChatDatabase.getInstance(application).chatDao()
    private var push_id: String? = null


    fun insert(Message: Message) {
        repository.insert(Message)
    }

    fun update(Message: Message) {
        repository.update(Message)
    }

    fun delete(Message: Message) {
        repository.delete(Message)
    }

    fun insert2(chat: Chat) {
        repository2.insert(chat)
    }

    fun increment(ui: String) {
        repository2.incrementuc(ui)
    }

    fun update2(chat: Chat) {
        repository2.update(chat)
    }

    fun delete2(chat: Chat) {
        repository2.delete(chat)
    }

    fun getAllChatLIst(): LiveData<List<Chat>> {
        return repository2.getAllChat
    }

    fun getUc(chat: Chat) {
        subscribeOnBackground {
            viewModelScope.launch {
                var uc = 0
                if (chatDao.getuc(chat.uid) != null) {
                    uc = chatDao.getuc(chat.uid)!! + 1
                }
                chat.unreadcount = uc
                insert2(chat)
                if (isvisibleornot()) {

                }

            }

        }

    }


    fun receiveMessages() {
        val timestaamp = Date().time
        val mchatRef = FirebaseDatabase.getInstance().getReference("User")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).child("messages")
        mchatRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message: Message = snapshot.getValue(Message::class.java)!!
                if (push_id != null) {
                    if (message.push_id != push_id) {
                        push_id = message.push_id
                        message.received_timestamp = timestaamp
                        insert(message)
                        val mchatRef2 =
                            FirebaseDatabase.getInstance().getReference("User")
                                .child(message.from_uid)
                        mchatRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user: User = snapshot.getValue(User::class.java)!!
                                getUc(
                                    Chat(
                                        snapshot.key!!,
                                        user.uname!!,
                                        user.phone!!,
                                        user.pplink!!,
                                        message.msg,
                                        false,
                                        0,
                                        timestaamp
                                    )
                                )
                                mchatRef.child(message.push_id).removeValue()

                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }
                } else {
                    push_id = message.push_id
                    message.received_timestamp = timestaamp
                    insert(message)
                    val mchatRef2 =
                        FirebaseDatabase.getInstance().getReference("User").child(message.from_uid)
                    mchatRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user: User = snapshot.getValue(User::class.java)!!
                            getUc(
                                Chat(
                                    snapshot.key!!,
                                    user.uname!!,
                                    user.phone!!,
                                    user.pplink!!,
                                    message.msg,
                                    false,
                                    0,
                                    timestaamp
                                )
                            )
                            mchatRef.child(message.push_id).removeValue()

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }


            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }


            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun isvisibleornot(): Boolean {
        val fragment: MessageFragment? =
            fm.findFragmentById(R.id.fragment_container) as MessageFragment?
        return fragment != null && fragment.isVisible
    }
}