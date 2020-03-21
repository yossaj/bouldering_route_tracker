package com.example.boulderjournal.routesHome

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boulderjournal.*
import com.example.boulderjournal.addRoute.AddRouteActivity
import com.example.boulderjournal.data.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity :  AppCompatActivity(), RouteAdapter.ItemClickListener{

    private var mUnfinishedAdapter: RouteAdapter? = null
    private var mRecycleViewToDo: RecyclerView? = null

    private var mFinishedAdapter: RouteAdapter? = null
    private var mRecycleViewDone: RecyclerView? = null
    private var mDb: AppDatabase? = null

    private var viewModel: MainViewModel? = null

    private val mAuth: FirebaseAuth? = null
    private val currentUser: FirebaseUser? = null
    private val userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        ScheduleReminderUtil.scheduleReminder(this, getString(R.string.shared_preference_key), getString(R.string.climb_day_key))

        //        mAuth = FirebaseAuth.getInstance();
        //        currentUser = mAuth.getCurrentUser();
        //        userName = currentUser.getDisplayName();

        mRecycleViewToDo = findViewById(R.id.recyclerRoutesToDo)
        mRecycleViewToDo!!.layoutManager = LinearLayoutManager(this)
        mUnfinishedAdapter = RouteAdapter(this, this)
        mRecycleViewToDo!!.adapter = mUnfinishedAdapter

        mRecycleViewDone = findViewById(R.id.recyclerRoutesDone)
        mFinishedAdapter = RouteAdapter(this, this)
        mRecycleViewDone!!.layoutManager = LinearLayoutManager(this)
        mRecycleViewDone!!.adapter = mFinishedAdapter

        moveToDoneWhenSwiped(mRecycleViewToDo, mUnfinishedAdapter)
        deleteWhenSwiped(mRecycleViewDone, mFinishedAdapter)
        returnToWorkingOn(mRecycleViewDone, mFinishedAdapter)

        mDb = AppDatabase.getInstance(applicationContext)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        retrieveUnfinishedRoutes()
        retrieveFinishedRoutes()

    }


    private fun retrieveUnfinishedRoutes() {
        viewModel!!.unFinishedRoutes!!.observe(this, Observer { routeEntries -> mUnfinishedAdapter!!.routes = routeEntries })

    }

    private fun retrieveFinishedRoutes() {
        viewModel!!.finishedRoutes!!.observe(this, Observer { routeEntries -> mFinishedAdapter!!.routes = routeEntries })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_route_menu_button, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            val addNewRoute = Intent(this@HomeActivity, AddRouteActivity::class.java)
            startActivity(addNewRoute)
            return true
        } else if (id == R.id.preferences) {
            //            Intent launchPreferences = new Intent(MainActivity.this, AppPreferencesFragment.class);
            //            startActivity(launchPreferences);
        } else if (id == R.id.sign_out_menu) {
            signOut()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(this@HomeActivity, AddRouteActivity::class.java)
        intent.putExtra(AddRouteActivity.EXTRA_ROUTE_ID, itemId)
        startActivity(intent)

    }

    fun moveToDoneWhenSwiped(recyclerView: RecyclerView?, adapter: RouteAdapter?) {


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                AppExecutors.getInstance().diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val routeEntries = adapter!!.routes
                    val route = routeEntries[position]
                    val status = "true"
                    route.setmComplete(status)
                    mDb!!.routeDao().updateRoute(route)
                }

            }
        }).attachToRecyclerView(recyclerView)
    }

    fun deleteWhenSwiped(recyclerView: RecyclerView?, adapter: RouteAdapter?) {


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {


                AppExecutors.getInstance().diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val routeEntries = adapter!!.routes
                    val route = routeEntries[position]
                    mDb!!.routeDao().deleteRoute(route)
                }

            }
        }).attachToRecyclerView(recyclerView)
    }


    fun returnToWorkingOn(recyclerView: RecyclerView?, adapter: RouteAdapter?) {

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.UP) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                AppExecutors.getInstance().diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val routeEntries = adapter!!.routes
                    val route = routeEntries[position]
                    val status = "false"
                    route.setmComplete(status)
                    mDb!!.routeDao().updateRoute(route)
                }

            }
        }).attachToRecyclerView(recyclerView)

    }

    fun signOut() {
        if (mAuth!!.currentUser != null) {
            Toast.makeText(baseContext, userName!! + " : Signed Out", Toast.LENGTH_LONG).show()
            mAuth.signOut()
            val returnToSignIn = Intent(this@HomeActivity, LoginActivty::class.java)
            startActivity(returnToSignIn)
        } else {
            Toast.makeText(baseContext, "Already signed out", Toast.LENGTH_LONG).show()
        }

    }

    companion object {

        private val ROUTE_LOADER_ID = 3
    }


}