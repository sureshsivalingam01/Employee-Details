package com.mrright.employeedetails.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mrright.employeedetails.ActivityEmployeeDetails
import com.mrright.employeedetails.R
import com.mrright.employeedetails.model.ResponseModel
import java.util.*

class AdapterEmployees(
    private var activity: Activity,
    private var listResponseModel: List<ResponseModel>
) : RecyclerView.Adapter<AdapterEmployees.EmployeeDetailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeDetailViewHolder {
        val itemView =
            LayoutInflater.from(activity)
                .inflate(R.layout.employee_list_recyclerview, parent, false)
        return EmployeeDetailViewHolder(itemView)
    }

    class EmployeeDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewName: TextView = itemView.findViewById(R.id.textViewName)
        var textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        var cardView: CardView = itemView.findViewById(R.id.cardView)

    }

    override fun getItemCount(): Int {
        return listResponseModel.size
    }

    override fun onBindViewHolder(holder: EmployeeDetailViewHolder, position: Int) {

        holder.textViewName.text = listResponseModel[position].name
        holder.textViewEmail.text = listResponseModel[position].email.toLowerCase(Locale.ROOT)

        holder.textViewEmail.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse("Mail to:")
                type = "text/plain"
                val email = arrayOf(listResponseModel[position].email.toLowerCase(Locale.ROOT))
                putExtra(Intent.EXTRA_EMAIL, email)
                putExtra(Intent.EXTRA_SUBJECT, "Regarding Employee")
            }
            if (intent.resolveActivity(activity.packageManager) != null) {
                intent.setPackage("com.google.android.gm")
                activity.startActivity(intent)
            } else {
                Log.d("TAG", "No app available to send email.")
            }
        }

        holder.cardView.setOnClickListener {
            val intent = Intent(activity, ActivityEmployeeDetails::class.java)
            intent.putExtra("id", listResponseModel[position].id)
            activity.startActivity(intent)
        }

    }


}




