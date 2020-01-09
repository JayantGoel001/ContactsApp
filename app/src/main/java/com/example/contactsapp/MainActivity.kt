package com.example.contactsapp

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val list=ArrayList<Contacts>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED)
        {
            loadContacts()

        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),10)
        }
        for (i in list) {
            Log.i("LIST Fuck off", i.name + "   " + i.phoneNo)
        }

    }

    private fun populateRecyclerView() {
        recycler_view.layoutManager=LinearLayoutManager(this)
        val adapter=RecyclerViewAdapter(this,list)
        recycler_view.adapter=adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==1)
        {
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                loadContacts()
            }
            else
            {
                Toast.makeText(this,"Permission Denied.Unable to Load Contacts",Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadContacts() {
        val allContacts= ContactsContract.CommonDataKinds.Phone.CONTENT_URI//Uri.parse("content://contacts/people")
        val cursorLoader=CursorLoader(this,allContacts,null,null,null,null)
        val c=cursorLoader.loadInBackground()!!
            if (c.moveToFirst()) {
                do {
                    val idIndex = c.getColumnIndex(ContactsContract.Contacts._ID)
                    val contactID = c.getString(idIndex)

                    val nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val displayName = c.getString(nameIndex)


                    val phoneIndex = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phoneNumber = c.getString(phoneIndex)


                    //val emailIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.EXTRA_ADDRESS_BOOK_INDEX)
                    //val email = c.getString(emailIndex)


                    val photoIndex =
                        c.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_FILE_ID)
                    val photo = c.getBlob(photoIndex)


                    var photoPath = ""
                    if (photo != null) {
                        val bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.size)
                        val cacheDirectory = baseContext.cacheDir
                        val tmp = File(cacheDirectory.path + "/_androhub" + contactID + ".png")
                        try {
                            val fileOutputStream = FileOutputStream(tmp)
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                            fileOutputStream.flush()
                            fileOutputStream.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        photoPath = tmp.path
                    }
                        list.add(Contacts(displayName, phoneNumber, contactID, "email", photoPath))
                } while (c.moveToNext())
            }
        populateRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
