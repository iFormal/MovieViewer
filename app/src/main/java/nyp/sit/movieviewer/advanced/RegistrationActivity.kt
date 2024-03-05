package nyp.sit.movieviewer.advanced

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.databinding.ActivityRegistrationBinding
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    var appCoroutineScope: CoroutineScope? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    }



    fun runRegister(v: View) {

        val name = binding.etUserName.text.toString()
        val password = binding.etPassword.text.toString()
        val email = binding.etEmail.text.toString()
        val admin = binding.etAdminNumber.text.toString()
        val pem = binding.etPemGrp.text.toString()

        appCoroutineScope?.launch(Dispatchers.IO) {

            var userPool = CognitoUserPool(v.context, AWSMobileClient.getInstance().configuration)

            var userAttributes = CognitoUserAttributes()
            userAttributes.addAttribute("email", email)
            userAttributes.addAttribute("custom:AdminNumber", admin)
            userAttributes.addAttribute("custom:PemGrp", pem)

            userPool.signUp(
                name,
                password,
                userAttributes,
                null, object : SignUpHandler {

                    override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                        Log.d("advanceLog", "Sign up success ${signUpResult?.userConfirmed}")
                        var myIntent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                        startActivity(myIntent)
                    }

                    override fun onFailure(exception: Exception?) {
                        Log.d("advanceLog", "Exception: ${exception?.message}")
                    }
                }
            )
        }

    }
}