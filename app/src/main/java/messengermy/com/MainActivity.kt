package messengermy.com

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import messengermy.com.UserAdapter
import java.io.File

class MainActivity : AppCompatActivity()  {

    private lateinit var UserRecylerView: RecyclerView
    private lateinit var Userlist : ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth  :  FirebaseAuth
    private lateinit var DbRef : DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        DbRef = FirebaseDatabase.getInstance().getReference()
        mAuth = FirebaseAuth.getInstance()
        Userlist = ArrayList()
        adapter = UserAdapter(this, Userlist)



        UserRecylerView = findViewById(R.id.RecyclerView)
        UserRecylerView.layoutManager = LinearLayoutManager(this)
        UserRecylerView.adapter = adapter

        uid = mAuth.currentUser?.uid.toString()
        if(uid.isNotEmpty()){
            getUserProfile()
        }

        getUserProfile()



        DbRef.child("currentuser").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {




                Userlist.clear()
                for (postSnapshot in snapshot.children){

                    val user = postSnapshot.getValue(User::class.java)


                    if(mAuth.currentUser?.uid != user?.uid){
                        Userlist.add(user!!)

                    }
                }
                adapter.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    // For Kotlin
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout){
            //logic for logout
            mAuth.signOut()
            val intent = Intent(this, Signup::class.java)
            finish()
            startActivity(intent)
            return true
        }

        return true
    }
    private fun getUserProfile(){
        val uid  = mAuth.currentUser?.uid.toString()
        storageReference = FirebaseStorage.getInstance().getReference("image/$uid")
        val localFile = File.createTempFile("tempImage", "jpg")


        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            loged_user_image.setImageBitmap(bitmap)

        }
    }


}
