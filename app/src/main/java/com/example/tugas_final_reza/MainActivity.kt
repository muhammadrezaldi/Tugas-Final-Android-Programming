@file:Suppress("DEPRECATION")

package com.example.tugas_final_reza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_final_reza.User.MahasiswaEntity
import com.example.tugas_final_reza.User.UserViewModel
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

        addbtn.setOnClickListener {
            addButtonAction()
        }

    }

    private fun addButtonAction() {
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

}