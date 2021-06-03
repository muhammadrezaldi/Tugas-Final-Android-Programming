package com.example.tugas_final_reza.User

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class UserViewModel(application: Application): AndroidViewModel(application) {

    private var repository = MahasiswaRepository(application)
    private var mahasiswas: LiveData<List<MahasiswaEntity>>? = repository.getMahasiswas()

    fun insertMahasiswa(mahasiswa: MahasiswaEntity) {
        repository.insert(mahasiswa)
    }

    fun getMahasiswas(): LiveData<List<MahasiswaEntity>>? {
        return mahasiswas
    }

    fun deleteMahasiswa(mahasiswa: MahasiswaEntity) {
        repository.delete(mahasiswa)
    }

    fun updateNote(mahasiswa: MahasiswaEntity) {
        repository.update(mahasiswa)
    }

}