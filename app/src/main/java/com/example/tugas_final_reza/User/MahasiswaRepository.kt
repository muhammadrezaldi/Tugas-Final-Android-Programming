package com.example.tugas_final_reza.User

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MahasiswaRepository(application: Application) {

    private val mahasiswaDao: MahasiswaDao
    private var mahasiswas: LiveData<List<MahasiswaEntity>>? = null

    init {
        val db = UserDatabase.getDatabase(application.applicationContext)
        mahasiswaDao = db.mahasiswaDao()
        mahasiswas = mahasiswaDao.readAllMahasiswa()
    }

    fun getMahasiswas(): LiveData<List<MahasiswaEntity>>? {
        return mahasiswas
    }

    fun insert(mahasiswa: MahasiswaEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            mahasiswaDao.insertMahasiswa(mahasiswa)
        }
    }

    fun delete(mahasiswa: MahasiswaEntity) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                mahasiswaDao.deleteMahasiswa(mahasiswa)
            }
        }
    }

    fun update(mahasiswa: MahasiswaEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            mahasiswaDao.updateMahasiswa(mahasiswa)
        }
    }

}