package com.example.boulderjournal.addRoute

import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.boulderjournal.AppExecutors
import com.example.boulderjournal.R
import com.example.boulderjournal.Utils.Utilities
import com.example.boulderjournal.data.AppDatabase
import com.example.boulderjournal.data.AppDatabase.Companion.getInstance
import com.example.boulderjournal.data.RouteEntry
import com.example.boulderjournal.databinding.FragmentAddRouteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddRouteFragment : Fragment() {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
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
    private var mDb: AppDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        setTheme(R.style.AppTheme)
        setHasOptionsMenu(true);
        val binding: FragmentAddRouteBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_route, container, false)
        val application = requireNotNull(this.activity).application
        val arguments = AddRouteFragmentArgs.fromBundle(arguments!!)

        mDb = getInstance(application)

        val routeId = arguments.routeEntryKey
        if (routeId != 0) {
            editMenuCheck = true
            AppExecutors.instance!!.diskIO().execute {
                route = mDb!!.routeDao().loadRouteById(routeId)
                if (Looper.myLooper() == null) { Looper.prepare() }
                populateStaticUI(binding, route)
            }
//            delaySetImage()
        }else{
            setDate(binding)
        }
//        mPhotoIntentButton!!.setOnClickListener { dispatchTakePictureIntent() }
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    //
//    fun setValues() {
//        routeName = mRouteName!!.text.toString()
//        val selectedId = mRouteColourGroup!!.checkedRadioButtonId
//        mRouteColourButtom = findViewById<View>(selectedId) as RadioButton
//        if (mRouteColourButtom != null) {
//            routeColour = mRouteColourButtom!!.text.toString()
//        }
//        room = mRoom!!.text.toString()
//        wall = mWall!!.text.toString()
//        notes = mNotes!!.text.toString()
//        val completedStr = completed.toString()
//        if (climbWallUri != null) {
//            imageURIstr = climbWallUri.toString()
//        }
//        if (date == null) {
//            date = Date()
//        }
//        if (routeName!!.length == 0 || routeColour!!.length == 0 || room!!.length == 0 || wall!!.length == 0 || notes!!.length == 0) {
//            Toast.makeText(baseContext, "Please fill in all fields", Toast.LENGTH_LONG).show()
//            return
//        } else if (route == null) {
//            route = RouteEntry(0, routeName, routeColour, room, wall, notes, imageURIstr, completedStr, date)
//            readyAddDb = true
//        } else {
//            route!!.routeName = routeName
//            route!!.routeColour = routeColour
//            route!!.room = room
//            route!!.wall = wall
//            route!!.note = notes
//            route!!.setmImageLocation(imageURIstr)
//            readyAddDb = true
//        }
//    }
//
//    fun onAddRoute() {
//        setValues()
//        if (readyAddDb) {
//            instance!!.diskIO().execute {
//                mDb!!.routeDao().insertRoute(route!!)
//                finish()
//            }
//        }
//    }
//
//    fun onUpdateRoute() {
//        setValues()
//        instance!!.diskIO().execute { mDb!!.routeDao().updateRoute(route!!) }
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        editMenuItem = menu.findItem(R.id.edit_menu_item)
//        addMenuItem = menu.findItem(R.id.add)
//        if (editMenuCheck) {
//            editMenuItem?.setVisible(editMenuCheck)
//            addMenuItem?.setVisible(false)
//        }
//        return super.onPrepareOptionsMenu(menu)
//    }
//
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_route_menu_button, menu)
        return super.onCreateOptionsMenu(menu)
    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.add) {
//            onAddRoute()
//            return true
//        } else if (id == R.id.edit_menu_item && editMenuCheck) {
//            populateEditableUI(route)
//        } else if (id == R.id.edit_menu_item && readyUpdateCheck) {
//            onUpdateRoute()
//            refactorUIonUpdateRoute()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
    fun setDate(binding: FragmentAddRouteBinding) {
        binding.dateAdded.text = dateFormat.format(Date())
    }

    //
    //
    fun populateStaticUI(binding: FragmentAddRouteBinding, routeEntry: RouteEntry?) {
        val formattedDate = dateFormat.format(routeEntry?.updatedAt)
        binding.dateAdded.text = formattedDate
        binding.viewRouteName.text = routeEntry?.routeName
        binding.viewRouteName.visibility = View.VISIBLE
        binding.inputRouteName.visibility = View.GONE
        val setRouteColor = routeEntry?.routeColour
        binding.viewRouteColour.text = setRouteColor
        binding.viewRouteColour.visibility = View.VISIBLE
        binding.routeColourGroup.visibility = View.GONE
        val getRouteColourInt = Utilities.getColor(setRouteColor, context)
        binding.colorSwatch.setBackgroundColor(getRouteColourInt)
        binding.colorSwatch.visibility = View.VISIBLE
        binding.viewRoom.text = routeEntry?.room
        binding.viewRoom.visibility = View.VISIBLE
        binding.inputRoom.visibility = View.GONE
        binding.viewWall.text = routeEntry?.wall
        binding.viewWall.visibility = View.VISIBLE
        binding.inputWall.visibility = View.GONE
        binding.viewRouteNotes.text = routeEntry?.note
        binding.viewRouteNotes.visibility = View.VISIBLE
        binding.routeNote.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
////        setSavedImageIfPresent()
    }

    //
//    fun populateEditableUI(routeEntry: RouteEntry?) {
//        mRouteName!!.setText(routeEntry!!.routeName)
//        mRouteName!!.visibility = View.VISIBLE
//        mRouteNameTV!!.visibility = View.GONE
//        mRouteColourTV!!.visibility = View.GONE
//        mRouteColourGroup!!.visibility = View.VISIBLE
//        getRadioBox(routeEntry.routeColour)
//        mColorSwatch!!.visibility = View.GONE
//        mRoom!!.setText(routeEntry.room)
//        mRoom!!.visibility = View.VISIBLE
//        mRoomTV!!.visibility = View.GONE
//        mWall!!.setText(routeEntry.wall)
//        mWall!!.visibility = View.VISIBLE
//        mWallTV!!.visibility = View.GONE
//        mNotes!!.setText(routeEntry.note)
//        mNotes!!.visibility = View.VISIBLE
//        mNotesTV!!.visibility = View.GONE
//        editMenuCheck = false
//        readyUpdateCheck = true
//        editMenuItem!!.setTitle(R.string.update)
//        mPhotoIntentButton!!.visibility = View.VISIBLE
//    }
//
//    fun refactorUIonUpdateRoute() {
//        Toast.makeText(baseContext, "Your note has been updated", Toast.LENGTH_LONG).show()
//        populateStaticUI(route)
//        editMenuItem!!.title = "Edit"
//        editMenuCheck = true
//        readyUpdateCheck = false
//    }
//
//    fun getRadioBox(color: String?) {
//        val newColourStr = color!!.toLowerCase()
//        when (newColourStr) {
//            "blue" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.blueButton)
//            "pink" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.pinkButton)
//            "orange" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.orangeButton)
//            "yellow" -> (findViewById<View>(R.id.route_colour_group) as RadioGroup).check(R.id.yellowButton)
//        }
//    }
//
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            // Create the File where the photo should go
//            var photoFile: File? = null
//            photoFile = try {
//                createImageFile()
//            } catch (ex: IOException) {
//                return
//            }
//            if (photoFile != null) {
//                val photoURI = FileProvider.getUriForFile(Objects.requireNonNull(applicationContext),
//                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile)
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//                climbWallUri = photoURI
//            }
//        }
//    }
//
//    private fun setSavedImageIfPresent() {
//        if (route!!.getmImageLocation() != null) {
//            climbWallUri = Uri.parse(route!!.getmImageLocation())
//        }
//    }
//
//    private fun setImage() {
//        if (climbWallUri != null) {
//            Picasso.get().load(climbWallUri).into(wallPhotoImageView)
//        } else {
//            val url = "https://p2.piqsels.com/preview/385/249/229/rock-climber-rope-mounatin.jpg"
//            Picasso.get().load(url).into(wallPhotoImageView)
//        }
//    }
//
//    private fun delaySetImage() {
//        try {
//            TimeUnit.SECONDS.sleep(1)
//            setImage()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            setImage()
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",  /* suffix */
//                storageDir /* directory */
//        )
//        currentPhotoPath = image.absolutePath
//        return image
//    }
//
    companion object {
        const val EXTRA_ROUTE_ID = "extraTaskId"
        const val INSTANCE_ROUTE_ID = "instanceTaskId"
        private const val DEFAULT_ROUTE_ID = -1
        private const val DATE_FORMAT = "dd/MM/yyy"
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}