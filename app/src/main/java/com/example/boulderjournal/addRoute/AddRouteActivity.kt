package com.example.boulderjournal.addRoute

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

import com.example.boulderjournal.AppExecutors
import com.example.boulderjournal.BuildConfig
import com.example.boulderjournal.R
import com.example.boulderjournal.Utils.Utilities
import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.RouteDao
import com.example.boulderjournal.data.RouteEntry
import com.squareup.picasso.Picasso

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.concurrent.TimeUnit


class AddRouteActivity : AppCompatActivity() {
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())


    private var mRouteName: EditText? = null
    private var mRoom: EditText? = null
    private var mWall: EditText? = null
    private var mNotes: EditText? = null

    private var mRouteColourGroup: RadioGroup? = null
    private var mRouteColourButtom: RadioButton? = null
    private var mColorSwatch: View? = null

    private var mDateTV: TextView? = null
    private var mRouteNameTV: TextView? = null
    private var mRouteColourTV: TextView? = null
    private var mRoomTV: TextView? = null
    private var mWallTV: TextView? = null
    private var mNotesTV: TextView? = null
    private var wallPhotoImageView: ImageView? = null

    private var routeName: String? = null
    private var routeColour: String? = null
    private var room: String? = null
    private var wall: String? = null
    private var notes: String? = null
    private var imageURIstr: String? = null
    private var date: Date? = null
    private val completed = false

    private var editMenuCheck = false
    private var readyUpdateCheck = false

    private var readyAddDb = false

    private var editMenuItem: MenuItem? = null
    private var addMenuItem: MenuItem? = null

    private var route: RouteEntry? = null


    private var mPhotoIntentButton: Button? = null
    private var currentPhotoPath: String? = null
    private var climbWallUri: Uri? = null

    private var mRouteId = DEFAULT_ROUTE_ID

    private var mDb: AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_add_route)
        initViews()
        mDb = AppDatabase.getInstance(applicationContext)

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_ROUTE_ID)) {
            mRouteId = savedInstanceState.getInt(INSTANCE_ROUTE_ID, DEFAULT_ROUTE_ID)
        }


        val intent = intent
        if ((intent != null) and intent!!.hasExtra(EXTRA_ROUTE_ID)) {
            editMenuCheck = true
            initStaticViews()
            val routeID = intent.getIntExtra(EXTRA_ROUTE_ID, DEFAULT_ROUTE_ID)

            AppExecutors.getInstance().diskIO().execute {
                route = mDb!!.routeDao().loadRouteById(routeID)
                populateStaticUI(route!!)
            }

            delaySetImage()


        }

        mPhotoIntentButton!!.setOnClickListener { dispatchTakePictureIntent() }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun setValues() {
        routeName = mRouteName!!.text.toString()
        val selectedId = mRouteColourGroup!!.checkedRadioButtonId
        mRouteColourButtom = findViewById<View>(selectedId) as RadioButton
        if (mRouteColourButtom != null) {
            routeColour = mRouteColourButtom!!.text.toString()
        }
        room = mRoom!!.text.toString()
        wall = mWall!!.text.toString()
        notes = mNotes!!.text.toString()
        val completedStr = completed.toString()
        if (climbWallUri != null) {
            imageURIstr = climbWallUri!!.toString()
        }
        if (date == null) {
            date = Date()
        }


        if (routeName!!.length == 0 || routeColour!!.length == 0 || room!!.length == 0 || wall!!.length == 0 || notes!!.length == 0) {
            Toast.makeText(baseContext, "Please fill in all fields", Toast.LENGTH_LONG).show()
            return
        } else if (route == null) {
            route = RouteEntry(0, routeName, routeColour, room, wall, notes, imageURIstr, completedStr, date)
            readyAddDb = true
        } else {
            route!!.routeName = routeName
            route!!.routeColour = routeColour
            route!!.room = room
            route!!.wall = wall
            route!!.note = notes
            route!!.mImageLocation = imageURIstr

            readyAddDb = true
        }
    }

    fun onAddRoute() {

        setValues()
        if (readyAddDb) {
            AppExecutors.getInstance().diskIO().execute {
                mDb?.routeDao()?.insertRoute(route)
                finish()
            }
        }
    }

    fun onUpdateRoute() {
        setValues()
        AppExecutors.getInstance().diskIO().execute { mDb!!.routeDao().updateRoute(route) }

    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        editMenuItem = menu.findItem(R.id.edit_menu_item)
        addMenuItem = menu.findItem(R.id.add)

        if (editMenuCheck) {
            editMenuItem!!.isVisible = editMenuCheck
            addMenuItem!!.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_route_menu_button, menu)



        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add) {
            onAddRoute()
            return true
        } else if (id == R.id.edit_menu_item && editMenuCheck) {
            populateEditableUI(route!!)
        } else if (id == R.id.edit_menu_item && readyUpdateCheck) {
            onUpdateRoute()
            refactorUIonUpdateRoute()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initViews() {

        val newFormattedDate = dateFormat.format(Date())
        mDateTV = findViewById<View>(R.id.date_added) as TextView
        mDateTV!!.text = newFormattedDate

        mRouteName = findViewById<View>(R.id.input_route_name) as EditText
        mRouteColourGroup = findViewById<View>(R.id.route_colour_group) as RadioGroup
        mRoom = findViewById<View>(R.id.input_room) as EditText
        mWall = findViewById<View>(R.id.input_wall) as EditText
        mNotes = findViewById<View>(R.id.route_note) as EditText
        wallPhotoImageView = findViewById<View>(R.id.capturedPhoto) as ImageView
        mPhotoIntentButton = findViewById<View>(R.id.updateButton) as Button
    }

    fun initStaticViews() {

        mRouteNameTV = findViewById<View>(R.id.view_route_name) as TextView
        mRouteColourTV = findViewById<View>(R.id.view_route_colour) as TextView
        mColorSwatch = findViewById(R.id.color_swatch) as View
        mRoomTV = findViewById<View>(R.id.view_room) as TextView
        mWallTV = findViewById<View>(R.id.view_wall) as TextView
        mNotesTV = findViewById<View>(R.id.view_route_notes) as TextView


    }

    fun populateStaticUI(routeEntry: RouteEntry) {

        val formattedDate = dateFormat.format(routeEntry.updatedAt)
        mDateTV!!.text = formattedDate

        mRouteNameTV!!.text = routeEntry.routeName
        mRouteNameTV!!.visibility = View.VISIBLE
        mRouteName!!.visibility = View.GONE

        val setRouteColor = routeEntry.routeColour
        mRouteColourTV!!.visibility = View.VISIBLE
        mRouteColourTV!!.text = setRouteColor
        mRouteColourGroup!!.visibility = View.GONE
        mColorSwatch!!.visibility = View.VISIBLE
        val getRouteColourInt = Utilities.getColor(setRouteColor, this)
        mColorSwatch!!.setBackgroundColor(getRouteColourInt)

        mRoomTV!!.text = routeEntry.room
        mRoomTV!!.visibility = View.VISIBLE
        mRoom!!.visibility = View.GONE

        mWallTV!!.text = routeEntry.wall
        mWallTV!!.visibility = View.VISIBLE
        mWall!!.visibility = View.GONE

        mNotesTV!!.text = routeEntry.note
        mNotesTV!!.visibility = View.VISIBLE
        mNotes!!.visibility = View.GONE

        mPhotoIntentButton!!.visibility = View.GONE
        setSavedImageIfPresent()
    }

    fun populateEditableUI(routeEntry: RouteEntry) {

        mRouteName!!.setText(routeEntry.routeName)
        mRouteName!!.visibility = View.VISIBLE
        mRouteNameTV!!.visibility = View.GONE

        mRouteColourTV!!.visibility = View.GONE
        mRouteColourGroup!!.visibility = View.VISIBLE
        getRadioBox(routeEntry.routeColour)
        mColorSwatch!!.visibility = View.GONE


        mRoom!!.setText(routeEntry.room)
        mRoom!!.visibility = View.VISIBLE
        mRoomTV!!.visibility = View.GONE


        mWall!!.setText(routeEntry.wall)
        mWall!!.visibility = View.VISIBLE
        mWallTV!!.visibility = View.GONE


        mNotes!!.setText(routeEntry.note)
        mNotes!!.visibility = View.VISIBLE
        mNotesTV!!.visibility = View.GONE

        editMenuCheck = false
        readyUpdateCheck = true
        editMenuItem!!.setTitle(R.string.update)

        mPhotoIntentButton!!.visibility = View.VISIBLE
    }

    fun refactorUIonUpdateRoute() {
        Toast.makeText(baseContext, "Your note has been updated", Toast.LENGTH_LONG).show()
        populateStaticUI(route!!)
        editMenuItem!!.title = "Edit"
        editMenuCheck = true
        readyUpdateCheck = false
    }


    fun getRadioBox(color: String?) {
        val newColourStr = color?.toLowerCase()
        when (newColourStr) {
            "blue" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.blueButton)
            "pink" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.pinkButton)
            "orange" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.orangeButton)
            "yellow" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.yellowButton)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                return
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(Objects.requireNonNull(applicationContext),
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                climbWallUri = photoURI
            }
        }
    }

    private fun setSavedImageIfPresent() {
        if (route!!.mImageLocation != null) {
            climbWallUri = Uri.parse(route!!.mImageLocation)
        }
    }

    private fun setImage() {
        if (climbWallUri != null) {
            Picasso.get().load(climbWallUri).into(wallPhotoImageView)
        } else {
            val url = "https://p2.piqsels.com/preview/385/249/229/rock-climber-rope-mounatin.jpg"
            Picasso.get().load(url).into(wallPhotoImageView)
        }
    }

    private fun delaySetImage() {
        try {
            TimeUnit.SECONDS.sleep(1)
            setImage()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setImage()
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        currentPhotoPath = image.absolutePath
        return image
    }

    companion object {

        val EXTRA_ROUTE_ID = "extraTaskId"
        val INSTANCE_ROUTE_ID = "instanceTaskId"
        private val DEFAULT_ROUTE_ID = -1


        private val DATE_FORMAT = "dd/MM/yyy"
        internal val REQUEST_IMAGE_CAPTURE = 1
    }
}
