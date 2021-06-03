@file:Suppress("DEPRECATION")

package com.example.tugas_final_reza

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_final_reza.User.Adapter
import com.example.tugas_final_reza.User.MahasiswaEntity
import com.example.tugas_final_reza.User.UserViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.view.*


class MainActivity : AppCompatActivity() {

    private val pickImage = 100
    lateinit var viewModel: UserViewModel
    lateinit var pasfoto: ImageView
    var imgurl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        checkPermission()
        addbtn.setOnClickListener {
            addButtonAction()
        }
    }

    private fun addButtonAction() {
        imgurl = ""
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val builder = this.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val mDialog = builder?.show()
        pasfoto = dialogView.pasfoto
        dialogView.lakilaki.isChecked = true
        dialogView.close_btn.setOnClickListener {
            mDialog?.dismiss()
        }
        dialogView.pasfoto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        dialogView.btn.setOnClickListener {
            try {
                with(dialogView) {
                    viewModel.insertMahasiswa(
                        MahasiswaEntity(
                            0,
                            imgurl,
                            namaET.text.toString(),
                            nimET.text.toString(),
                            angkatanET.text.toString().toInt(),
                            umurET.text.toString().toInt(),
                            jeniskelamin(this),
                            sukuET.text.toString(),
                            agamaET.text.toString(),
                            tempatlahirET.text.toString(),
                            tanggallahirET.text.toString()
                        )
                    )
                }
                mDialog?.dismiss()
                Toast.makeText(this,"Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                Log.e("MainActivity", "Error : $e")
            }
        }
    }

    private fun jeniskelamin(view: View): String {
        return when(view.radiogroup.checkedRadioButtonId) {
            R.id.lakilaki -> "Laki-laki"
            R.id.perempuan -> "Perempuan"
            else -> ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data
            val url = imageUri.toString()
            imgurl = url
            Log.d("img url", url)
            pasfoto.setImageURI(imageUri)
        }
    }


    private fun checkPermission() {
        Dexter
            .withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    viewModel.getMahasiswas()?.observe(this@MainActivity, Observer {
                        recyclerview.adapter = Adapter(it)
                    })
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

}