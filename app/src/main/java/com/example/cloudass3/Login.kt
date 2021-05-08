package com.example.cloudass3

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
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class Login : AppCompatActivity() {
    var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (intent != null){
            val em = intent.getStringExtra("email").toString()
            val pass = intent.getStringExtra("password").toString()
            login!!.setOnClickListener {

                val data = "{" +
                        "\"email\"" + ":" + "\"" + email_log!!.text.toString() + "\"," +
                        "\"password\"" + ":" + "\"" + password_log!!.text.toString() + "\"" +
                        "}"
                if (email_log!!.text.toString() == em && password_log!!.text.toString() == pass){
                    submitFunc(data)}
                else{
                    Toast.makeText(this,"Not val",Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun submitFunc(data: String) {

        val urls = "https://mcc-users-api.herokuapp.com/login"
        requestQueue = Volley.newRequestQueue(applicationContext)
        Log.d("dna", "requestQueue: $requestQueue")
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, urls, Response.Listener<String?> { response ->
            try {
                val   objRes =  JSONObject(response)

                Log.d("dna", "onResponse: $objRes")
            } catch (e: JSONException) {
                Log.d("dna", "Server Error ")
            }
        }, Response.ErrorListener { error -> Log.d("dna", "onErrorResponse: $error") }) {
            override fun getBodyContentType(): String {
                //dania saqqa
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