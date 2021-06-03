@file:Suppress("DEPRECATION")

package com.example.tugas_final_reza

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_final_reza.User.MahasiswaEntity
import com.example.tugas_final_reza.User.UserViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.view.pasfoto
import kotlinx.android.synthetic.main.item_mahasiswa.view.*


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
                        recyclerview.adapter = Adapter(it, object : Adapter.Listener {
                            override fun onClick(mahasiswaEntity: MahasiswaEntity) {
                                tampilkanDialog(mahasiswaEntity)
                            }
                        })
                    })
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun tampilkanDialog(mahasiswaEntity: MahasiswaEntity) {
        imgurl = mahasiswaEntity.urlPasFoto!!
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val builder = this.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val mDialog = builder?.show()

        //SET VALUE
        pasfoto = dialogView.pasfoto

        if(mahasiswaEntity.urlPasFoto != "") {
            try {
                pasfoto.setImageURI(Uri.parse(mahasiswaEntity.urlPasFoto))
            } catch (e: java.lang.Exception){}
        } else {
            if(mahasiswaEntity.jenisKelamin == "Laki-laki") {
                pasfoto.setImageResource(R.drawable.ic_man)
            } else {
                pasfoto.setImageResource(R.drawable.ic_woman)
            }
        }

        with(dialogView) {
            hapus.visibility = View.VISIBLE
            namaET.setText(mahasiswaEntity.nama)
            nimET.setText(mahasiswaEntity.nim)
            angkatanET.setText(""+mahasiswaEntity.angkatan)
            umurET.setText(""+mahasiswaEntity.umur)
            when(mahasiswaEntity.jenisKelamin) {
                "Laki-laki"-> dialogView.lakilaki.isChecked = true
                else -> dialogView.perempuan.isChecked = true
            }
            sukuET.setText(mahasiswaEntity.suku)
            agamaET.setText(mahasiswaEntity.agama)
            tempatlahirET.setText(mahasiswaEntity.tempatLahir)
            tanggallahirET.setText(mahasiswaEntity.tanggalLahir)
        }

        dialogView.close_btn.setOnClickListener {
            mDialog?.dismiss()
        }

        dialogView.pasfoto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        dialogView.btn.text = "Update"
        dialogView.btn.setOnClickListener {
            try {
                with(dialogView) {
                    viewModel.updateMahasiswa(
                        MahasiswaEntity(
                            mahasiswaEntity.id,
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
        dialogView.hapus.setOnClickListener {
            viewModel.deleteMahasiswa(mahasiswaEntity)
            Toast.makeText(this,"Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            mDialog?.dismiss()
        }
    }

}