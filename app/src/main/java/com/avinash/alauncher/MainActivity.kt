package com.avinash.alauncher

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.avinash.alauncher.databinding.ActivityMainBinding
import com.avinash.applistsdk.AppListUtils
import com.avinash.applistsdk.AppObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var appListAdapter: AppListAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setAppListRecyclerView()
    }

    private fun bindView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAppListRecyclerView() {
        AppListUtils.getInstalledAppList(this.packageManager)?.let { itAppList ->
            appListAdapter = AppListAdapter(itAppList, object : RowClickListener {
                override fun onRowClicked(item: AppObject) {
                    val launchAppIntent =
                        applicationContext.packageManager.getLaunchIntentForPackage(item.appPackageName)
                    if (launchAppIntent != null) applicationContext.startActivity(launchAppIntent)
                }
            })
            binding.rvAppList.adapter = appListAdapter
            binding.rvAppList.layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Associate searchable configuration with the SearchView
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView?.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView?.maxWidth = Int.MAX_VALUE

        // listening to search query text change
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // filter recycler view when query submitted
                appListAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // filter recycler view when text is changed
                appListAdapter?.filter?.filter(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // close search view on back button pressed
        if (!searchView?.isIconified!!) {
            searchView?.isIconified = true
            return
        }
        super.onBackPressed()
    }


}


