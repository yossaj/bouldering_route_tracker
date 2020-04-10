package com.example.boulderjournal.routeshome

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.boulderjournal.*
import com.example.boulderjournal.addRoute.AddRouteActivity
import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteDao
import com.example.boulderjournal.databinding.FragmentHomeBinding
import com.example.boulderjournal.notifications.ScheduleReminderUtil

class HomeFragment : Fragment(), RouteAdapter.ItemClickListener {

    private var unfinishedAdapter: RouteAdapter? = null
    private var finishedAdapter: RouteAdapter? = null
    private var mDb: RouteDao? = null
    private var viewModel: HomeViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ScheduleReminderUtil.scheduleReminder(context, getString(R.string.shared_preference_key), getString(R.string.climb_day_key))
        //        mAuth = FirebaseAuth.getInstance();
        //        currentUser = mAuth.getCurrentUser();
        //        userName = currentUser.getDisplayName();
        setHasOptionsMenu(true);
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        mDb = AppDatabase.getInstance(application)?.routeDao()
        val viewModelFactory = HomeViewModelFactory(mDb, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        unfinishedAdapter = RouteAdapter(this)
        binding.recyclerRoutesToDo.adapter = unfinishedAdapter

        finishedAdapter = RouteAdapter(this)
        binding.recyclerRoutesDone.adapter = finishedAdapter

        moveToDoneWhenSwiped(binding.recyclerRoutesToDo, unfinishedAdapter)
        deleteWhenSwiped(binding.recyclerRoutesDone, finishedAdapter)
        returnToWorkingOn(binding.recyclerRoutesDone, finishedAdapter)

        retrieveUnfinishedRoutes()
        retrieveFinishedRoutes()

        return binding.root
    }

    override fun onItemClickListener(itemId: Int) {
        val intent = Intent(getActivity(), AddRouteActivity::class.java)
        intent.putExtra(AddRouteActivity.EXTRA_ROUTE_ID, itemId)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_route_menu_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            val addNewRoute = Intent(activity, AddRouteActivity::class.java)
            startActivity(addNewRoute)
            return true
        } else if (id == R.id.preferences) {
            this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAppPreferencesFragment()
            )
        } else if (id == R.id.sign_out_menu) {
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retrieveUnfinishedRoutes() {
        viewModel!!.unFinishedRoutes!!.observe(this, Observer { routeEntries -> unfinishedAdapter!!.submitList(routeEntries) })
    }


    private fun retrieveFinishedRoutes() {
        viewModel!!.finishedRoutes!!.observe(this, Observer { routeEntries -> finishedAdapter!!.submitList(routeEntries) })
    }


    fun moveToDoneWhenSwiped(recyclerView: RecyclerView?, adapter: RouteAdapter?) {

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                AppExecutors.instance!!.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val route = adapter!!.getRouteByPosition(position)
                    val status = "true"
                    route.setmComplete(status)
                    mDb!!.updateRoute(route)
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
                AppExecutors.instance!!.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val route = adapter!!.getRouteByPosition(position)
                    mDb!!.deleteRoute(route)
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
                AppExecutors.instance!!.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val route = adapter!!.getRouteByPosition(position)
                    val status = "false"
                    route.setmComplete(status)
                    mDb!!.updateRoute(route)
                }
            }
        }).attachToRecyclerView(recyclerView)
    }


}

//    fun signOut() {
//        if (mAuth!!.currentUser != null) {
//            Toast.makeText(baseContext, userName!! + " : Signed Out", Toast.LENGTH_LONG).show()
//            mAuth.signOut()
//            val returnToSignIn = Intent(this@HomeFragment, LoginActivty::class.java)
//            startActivity(returnToSignIn)
//        } else {
//            Toast.makeText(baseContext, "Already signed out", Toast.LENGTH_LONG).show()
//        }
//    }

//}