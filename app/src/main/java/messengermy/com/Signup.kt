package messengermy.com

import android.app.Dialog
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Window
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.sign_in
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.edt_email
import kotlinx.android.synthetic.main.activity_signup.edt_password
import kotlinx.android.synthetic.main.custom_dialogue_box.*

class Signup : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var email :EditText
    private lateinit var password :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.edt_email)
        password = findViewById(R.id.edt_password)



        btnlogin.setOnClickListener {

            val mail = email.text.toString().trim()
            val pass = password.text.toString().trim()

            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                loginUser(mail, pass)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }

            checkFields()
            Check_internet_connectivity()




        }

        sign_in.setOnClickListener {
            val next = Intent(this,Login::class.java)
            startActivity(next)
        }

        back_arrow.setOnClickListener {
            val next = Intent(this,Login::class.java)
            startActivity(next)
        }


    }
     fun Check_internet_connectivity(){

        var context = this
        var connectivity: ConnectivityManager? = null
        var info : NetworkInfo? = null

        connectivity = context.getSystemService(Service.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        if (connectivity != null){
            info = connectivity !!.activeNetworkInfo

            if(connectivity != null){

                if (info!= null){
                    if(info!!.state == NetworkInfo.State.CONNECTED){


                                            }
                }else
                    showCustomDialogueBox()
            }

        }
    }

    fun showCustomDialogueBox(){
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

    fun checkFields() {
        var allFieldsFilled = true  // Assume all fields are filled initially



        if (isEditTextEmpty(edt_password)) {
            // EditText is empty, show an error or prevent further action
            // For example, you can set an error message to the EditText
            edt_password.error = "This field is required"
            allFieldsFilled = false
            setEditTextErrorPadding(edt_password,10)
        }

        if (isEditTextEmpty(edt_email)) {
            // EditText is empty, show an error or prevent further action
            // For example, you can set an error message to the EditText
            edt_email.error = "This field is required"
            allFieldsFilled = false
            setEditTextErrorPadding(edt_email,10)

        }

        // Check if all fields are filled properly
        if (allFieldsFilled) {
            // All fields are filled properly, execute additional code

        }

    }
    fun setEditTextErrorPadding(editText: EditText, fixedPaddingInDp: Int) {
        val scale = editText.resources.displayMetrics.density
        val paddingInPx = (fixedPaddingInDp * scale + 0.5f).toInt()

        // Set padding to the right of the EditText
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

    private fun loginUser(mail: String, pass: String) {
        auth.signInWithEmailAndPassword(mail, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User logged in successfully
                    var intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    // Add your navigation logic here
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }




    }





