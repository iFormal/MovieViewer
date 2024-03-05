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
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    var appCoroutineScope: CoroutineScope? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        AWSMobileClient.getInstance().initialize(this, object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("advanceLog", result?.userState?.name.toString())
                if (result?.userState?.name.toString() == "SIGNED_IN")
                {
                    var i = Intent(this@LoginActivity, ViewListOfMoviesActivity::class.java)
                    startActivity(i)
                }
            }

            override fun onError(e: Exception?) {
                Log.d("advanceLog", "There is an error - ${e.toString()}")
            }
        })

        btnRegister.setOnClickListener {
            val myIntent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(myIntent)
        }
    }
    fun runLogin(v: View) {

        appCoroutineScope?.launch {

            AWSMobileClient.getInstance().signIn(
                binding.etUserName.text.toString(),
                binding.etPassword.text.toString(),
                null,
                object : Callback<SignInResult> {
                    override fun onResult(result: SignInResult?) {
                        Log.d("advanceLog", "Sign in result : ${result.toString()}")
                        if (result?.signInState == SignInState.DONE) {

                            var i = Intent(v.context, ViewListOfMoviesActivity::class.java)
                            startActivity(i)
                        }
                    }

                    override fun onError(e: Exception?) {
                        Log.d("advanceLog", "Sign in error : ${e.toString()}")
                    }
                }
            )
        }
    }
}