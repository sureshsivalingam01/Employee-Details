package com.mrright.employeedetails

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mrright.employeedetails.model.ResponseModel
import kotlinx.android.synthetic.main.activity_employee_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ActivityEmployeeDetails : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private var id: Int = 0
    private var employeeDetails = ResponseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        id = intent.getIntExtra("id", 0)

        getData()

        textViewEmail.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse("Mail to:")
                type = "text/plain"
                val email = arrayOf(employeeDetails.email.toLowerCase(Locale.ROOT))
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, "Regarding Employee")
            }
            if (intent.resolveActivity(this.packageManager) != null) {
                intent.setPackage("com.google.android.gm")
                startActivity(intent)
            } else {
                Log.d("TAG", "No app available to send email.")
            }
        }

        textViewPhoneNumber.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(employeeDetails.phone)))
            startActivity(intent)
        }

    }

    //getEmployeeDetails
    private fun getData() {

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val call: Call<ResponseModel> = ApiClient.getClient.getEmployeeDetail("users/$id")
        call.enqueue(object : Callback<ResponseModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseModel>?,
                response: Response<ResponseModel>?
            ) {
                if (response != null) {
                    linearLayout.visibility = View.VISIBLE
                    employeeDetails = response.body()
                    textViewEmployeeID.text = employeeDetails.id.toString()
                    textViewName.text = employeeDetails.name
                    textViewEmail.text = employeeDetails.email.toLowerCase(Locale.ROOT)
                    textViewAddress1.text =
                        employeeDetails.address.suite + ", " + employeeDetails.address.street
                    textViewAddress2.text =
                        employeeDetails.address.city + ", " + employeeDetails.address.zipcode
                    textViewPhoneNumber.text = employeeDetails.phone
                    textViewCompanyName.text = employeeDetails.company.name
                    textViewcompanyWebSite.text = employeeDetails.website
                    progressDialog.dismiss()
                }

            }

            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {

                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.employeeempty),
                    Toast.LENGTH_SHORT
                )
                    .show()

            }


        })
    }
}