package com.example.boulderjournal.routeshome

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.boulderjournal.LoginActivty
import com.example.boulderjournal.R
import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteDao
import com.example.boulderjournal.databinding.FragmentHomeBinding
import com.example.boulderjournal.notifications.ScheduleReminderUtil
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(){

    private var unfinishedAdapter: RouteAdapter? = null
    private var finishedAdapter: RouteAdapter? = null
    private var mDb: RouteDao? = null
    private var viewModel: HomeViewModel? = null
    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true);
        createChannel(
                getString(R.string.note_notification),
                getString(R.string.note_channel_id)
        )
        mAuth = FirebaseAuth.getInstance();
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false)
        val application = requireNotNull(this.activity).application
        ScheduleReminderUtil.scheduleReminder(context, getString(R.string.shared_preference_key), getString(R.string.climb_day_key))

        mDb = AppDatabase.getInstance(application)?.routeDao
        val datasource = AppDatabase.getInstance(application)
        val viewModelFactory = HomeViewModelFactory(datasource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = viewModel

        unfinishedAdapter = RouteAdapter(RouteAdapter.ItemClickListener { routeId ->
            navigateToRoute(routeId)
        })
        binding.recyclerRoutesToDo.adapter = unfinishedAdapter


        finishedAdapter = RouteAdapter(RouteAdapter.ItemClickListener { routeId ->
            navigateToRoute(routeId)
        })
        binding.recyclerRoutesDone.adapter = finishedAdapter

        viewModel!!.swipeTo("MoveToDone", binding.recyclerRoutesToDo, unfinishedAdapter)
        viewModel!!.swipeTo("Delete", binding.recyclerRoutesDone, finishedAdapter)
        viewModel!!.swipeTo("MoveToWorkingOn", binding.recyclerRoutesDone, finishedAdapter)

        retrieveRoutes()
        return binding.root
    }

    private fun navigateToRoute(routeId: Int) {
        this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddRouteFragment(routeId)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_route_menu_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddRouteFragment(0))
        } else if (id == R.id.preferences) {
            this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAppPreferencesFragment()
            )
        } else if (id == R.id.sign_out_menu) {
            signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retrieveRoutes() {
        viewModel!!.unFinishedRoutes!!.observe(this, Observer { routeEntries -> unfinishedAdapter!!.submitList(routeEntries) })
        viewModel!!.finishedRoutes!!.observe(this, Observer { routeEntries -> finishedAdapter!!.submitList(routeEntries) })
    }

    private fun signOut() {
        val currentUser = mAuth.getCurrentUser();
        val userName = currentUser!!.getDisplayName();
        if (mAuth!!.currentUser != null) {
            Toast.makeText(context, userName!! + " : Signed Out", Toast.LENGTH_LONG).show()
            mAuth.signOut()
            val returnToSignIn = Intent(context, LoginActivty::class.java)
            startActivity(returnToSignIn)
        } else {
            Toast.makeText(context, "Already signed out", Toast.LENGTH_LONG).show()
        }
    }

    private fun createChannel(channelId: String, channelName: String){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val takeNoteReminderChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH)
            takeNoteReminderChannel.enableLights(true)
            takeNoteReminderChannel.lightColor = Color.YELLOW

            val notificationManager = requireActivity().getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(takeNoteReminderChannel)
        }
    }
}