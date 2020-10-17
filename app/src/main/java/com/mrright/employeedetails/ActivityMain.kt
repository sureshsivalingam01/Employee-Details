package com.mrright.employeedetails

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrright.employeedetails.adapters.AdapterEmployees
import com.mrright.employeedetails.model.ResponseModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityMain : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkInternet()) {

            //progress
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading")
            progressDialog.setCancelable(false)
            progressDialog.show()
            recyclerViewEmployeeList.visibility = View.VISIBLE

            //getEmployeeList
            getData()

        } else {

            progressDialog.dismiss()

            recyclerViewEmployeeList.visibility = View.INVISIBLE

        }

    }

    //internet Check
    private fun checkInternet(): Boolean {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
        ) {
            Toast.makeText(
                this@ActivityMain,
                resources.getString(R.string.internetconnected),
                Toast.LENGTH_SHORT
            ).show()
            true
        } else {
            Toast.makeText(
                this@ActivityMain,
                resources.getString(R.string.nointernet),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    //getEmployeeList
    private fun getData() {

        val call: Call<List<ResponseModel>> = ApiClient.getClient.getEmployeeList()
        call.enqueue(object : Callback<List<ResponseModel>> {

            override fun onResponse(
                call: Call<List<ResponseModel>>?,
                response: Response<List<ResponseModel>>?
            ) {

                if (response != null) {

                    val adapterEmployees = AdapterEmployees(this@ActivityMain, response.body()!!)
                    recyclerViewEmployeeList?.layoutManager =
                        LinearLayoutManager(applicationContext)
                    recyclerViewEmployeeList?.adapter = adapterEmployees

                    progressDialog.dismiss()
                }

            }

            override fun onFailure(call: Call<List<ResponseModel>>?, t: Throwable?) {
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.listempty),
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }
}