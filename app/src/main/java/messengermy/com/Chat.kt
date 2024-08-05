package messengermy.com

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*

class Chat : AppCompatActivity() {

    private lateinit var chatRecyclerview:RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var profileView: ShapeableImageView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var DbRef: DatabaseReference
    private lateinit var u_name : TextView
    private lateinit var messageObjects:Message

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()
        u_name = findViewById(R.id.Username)




        DbRef = FirebaseDatabase.getInstance().getReference()

        var name = intent.getStringExtra("name")
        u_name.text = name
        var recieverUid = intent.getStringExtra("uid")




        btn_backout.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid



        chatRecyclerview = findViewById(R.id.chatRecyclerView)
       messageBox =findViewById(R.id.Message_box)
        sendButton = findViewById(R.id.Send_message_btn)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)


        retrivingMessages()

        //adding the message  to database
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            messageObjects = Message(message, senderUid)

            sendingMessagesToDatabase()

            messageBox.setText("")

        }

        chatRecyclerview.layoutManager = LinearLayoutManager(this)
        chatRecyclerview.adapter = messageAdapter



    }

    fun sendingMessagesToDatabase(){


        DbRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObjects).addOnSuccessListener {

                DbRef.child("chats").child(recieverRoom!!).child("messages").push()
                    .setValue(messageObjects)

            }
    }
    //logic for adding data in recyclerview
    // Logic for adding data in recyclerview
    fun retrivingMessages() {
        DbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    // Notify the adapter that the data has changed
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed
                }
            })
    }


}