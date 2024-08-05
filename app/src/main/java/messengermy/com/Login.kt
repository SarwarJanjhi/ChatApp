package messengermy.com

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var imageuri:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        profile_image.setOnClickListener {

            selectimage()

        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        mAuth = FirebaseAuth.getInstance()

        sign_in.setOnClickListener {
            val next = Intent(this, Signup::class.java)
            startActivity(next)
        }

        btnsignup.setOnClickListener {
            checkFields()
            checkInternetConnectivity()
            uploadimage()
        }
    }

    private fun firebase_Authorization() {
        val email = edt_email?.text?.toString() ?: ""
        val password = edt_password?.text?.toString() ?: ""
        val name = name?.text?.toString() ?: ""

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Handle unsuccessful registration if needed
                } else {
                    saveUserInfoToDatabase(name, email, mAuth.currentUser?.uid ?: "")

                    val intent = Intent(this, Signup::class.java)
                    finish()
                    startActivity(intent)
                }
            }
    }

    private fun saveUserInfoToDatabase(name: String, email: String, uid: String) {
        val database = FirebaseDatabase.getInstance().getReference("currentuser")
        database.child(uid).setValue(User(name, email, uid,))
    }


    private fun checkInternetConnectivity() {
        val connectivityManager =
            getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // Internet is connected, proceed with the action
        } else {
            showCustomDialogueBox()
        }
    }

    private fun showCustomDialogueBox() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialogue_box)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvmessage: TextView = dialog.findViewById(R.id.tv_notification)
        val btn: Button = dialog.findViewById(R.id.btn_ok)

        btn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun checkFields() {
        var allFieldsFilled = true

        if (isEditTextEmpty(name)) {
            name.error = "This field is required"
            allFieldsFilled = false
            setEditTextErrorPadding(name, 10)
        }

        if (isEditTextEmpty(edt_password)) {
            edt_password.error = "This field is required"
            allFieldsFilled = false
            setEditTextErrorPadding(edt_password, 10)
        }

        if (isEditTextEmpty(edt_email)) {
            edt_email.error = "This field is required"
            allFieldsFilled = false
            setEditTextErrorPadding(edt_email, 10)
        }

        if (allFieldsFilled) {
            firebase_Authorization()
        }
    }

    private fun setEditTextErrorPadding(editText: EditText, fixedPaddingInDp: Int) {
        val scale = editText.resources.displayMetrics.density
        val paddingInPx = (fixedPaddingInDp * scale + 0.5f).toInt()
        editText.setPadding(
            editText.paddingLeft,
            editText.paddingTop,
            paddingInPx,
            editText.paddingBottom
        )
    }

    private fun isEditTextEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim().isEmpty()
    }

    private fun selectimage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageuri = data.data!!
            profile_image.setImageURI(imageuri)
        }
    }

    private fun uploadimage(){
        storageReference = FirebaseStorage.getInstance().getReference("images/"+mAuth.currentUser?.uid + "jpg")
        storageReference.putFile(imageuri)
    }

}
