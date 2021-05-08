package com.example.cloudass3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class MainActivity : AppCompatActivity() {
    var requestQueue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstName = firstName.text
        val secondName = secondName.text
        val email = email.text
        val password = password.text


            signup.setOnClickListener {
                if (firstName.isNotEmpty() && secondName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val data = "{" +
                        "\"firstName\"" + ":" + "\"" + firstName.toString() + "\"," +
                        "\"secondName\"" + ":" + "\"" + secondName.toString() + "\"," +
                        "\"email\"" + ":" + "\"" + email.toString() + "\"," +
                        "\"password\"" + ":" + "\"" + password.toString() + "\"" +
                        "}"
                submitFunc(data)
                getRegToken()
                    //dania saqqa
                val i = Intent(this, Login::class.java)
                i.putExtra("email", email.toString())
                i.putExtra("password", password.toString())
                startActivity(i)
                } else {
                    Toast.makeText(this, "Please fill the field", Toast.LENGTH_LONG).show()
                }
            }

    }


    private fun getRegToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("dna", "Failed to get token ${task.exception}")

                return@addOnCompleteListener
            }
            val token = task.result
            val data = "{" +

                    "\"email\"" + ":" + "\"" + email!!.text.toString() + "\"," +
                    "\"password\"" + ":" + "\"" + password!!.text.toString() + "\"" +
                    "\"reg_token\"" + ":" + "\"" + token!! + "\"," +
                    "}"
            val urls = "https://mcc-users-api.herokuapp.com/add_reg_token"
            requestQueue = Volley.newRequestQueue(applicationContext)
            Log.d("dna", "requestQueue: $requestQueue")
            //dania saqqa
            val stringRequest: StringRequest =
                object : StringRequest(Method.PUT, urls, Response.Listener<String?> { response ->
                    try {
                        val objRes = JSONObject(response)
                        Log.d("dna", "onResponse: $objRes")
                    } catch (e: JSONException) {
                        Log.d("dna", "Server Error ")
                    }
                }, Response.ErrorListener { error -> Log.d("TAG", "onErrorResponse: $error") }) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        return try {
                            Log.d("dna", "savedata: $data")
                            if (data == null) null else data.toByteArray(charset("utf-8"))
                        } catch (uee: UnsupportedEncodingException) {
                            null
                        }!!
                    }
                }
            requestQueue!!.add(stringRequest)
            //dania saqqa
            //    Log.e("dnaTok",token!!)

        }
    }

    private fun submitFunc(data: String) {
        val urls = "https://mcc-users-api.herokuapp.com/add_new_user"
        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("dna", "requestQueue: $requestQueue")
        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.POST, urls, Response.Listener<String?> { response ->
                try {
                    val objRes = JSONObject(response)
                    Log.d("dna", "onResponse: $objRes")
                    //dania saqqa
                } catch (e: JSONException) {
                    Log.d("dna", "Server Error ")
                }
            }, Response.ErrorListener { error -> Log.d("dna", "onErrorResponse: $error") }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return try {
                    Log.d("dna", "saveData: $data")
                    if (data == null) null else data.toByteArray(charset("utf-8"))
                } catch (uee: UnsupportedEncodingException) {
                    null
                }!!
            }
        }
        //dania saqqa
        requestQueue!!.add(stringRequest)
    }
}