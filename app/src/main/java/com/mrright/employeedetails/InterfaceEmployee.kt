package com.mrright.employeedetails

import com.mrright.employeedetails.model.ResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface InterfaceEmployee {

    @GET("users")
    fun getEmployeeList(): Call<List<ResponseModel>>

    @GET
    fun getEmployeeDetail(@Url userID: String): Call<ResponseModel>

}