package com.avinash.alauncher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.avinash.applistsdk.AppObject
import java.util.*

interface RowClickListener {
    fun onRowClicked(item: AppObject)
}

internal class AppListAdapter(
    private var appList: ArrayList<AppObject>,
    private val rowClickListener: RowClickListener?
) :
    RecyclerView.Adapter<AppListAdapter.MyViewHolder>(), Filterable {

    var appListFilter = appList

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_app_item, parent, false)
        return MyViewHolder(itemView)
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAppName: TextView = view.findViewById(R.id.tvAppName)
        val tvAppPackageName: TextView = view.findViewById(R.id.tvAppPackageName)
        val tvActivityName: TextView = view.findViewById(R.id.tvActivityName)
        val tvVersion: TextView = view.findViewById(R.id.tvVersion)
        val ivAppLogo: ImageView = view.findViewById(R.id.ivAppLogo)
        val clRoot: CardView = view.findViewById(R.id.clRoot)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val app = appListFilter[position]
        holder.tvAppName.text = app.appName
        holder.tvAppPackageName.text = app.appPackageName
        holder.tvActivityName.text = app.launcherActivity
        holder.tvVersion.text = "version ${app.versionName} (${app.versionCode})"
        holder.ivAppLogo.setImageDrawable(app.appImage)
        holder.clRoot.setOnClickListener {
            rowClickListener?.onRowClicked(app)
        }
    }

    override fun getItemCount(): Int {
        return appListFilter.size
    }

    // for searching
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString: String = charSequence.toString()
                if (charString.isEmpty()) {
                    appListFilter = appList
                } else {
                    val filteredList: ArrayList<AppObject> =
                        java.util.ArrayList<AppObject>()
                    for (row in appList) {
                        if (row.appName.toLowerCase(Locale.ROOT).contains(
                                charString.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    appListFilter = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = appListFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appListFilter = results?.values as ArrayList<AppObject>
                notifyDataSetChanged()
            }
        }
    }
}