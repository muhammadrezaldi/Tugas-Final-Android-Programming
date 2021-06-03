package com.example.tugas_final_reza.User

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MahasiswaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMahasiswa(mahasiswa: MahasiswaEntity)

    @Query("SELECT * FROM table_mahasiswa")
    fun readAllMahasiswa(): LiveData<List<MahasiswaEntity>>

    @Delete
    suspend fun deleteMahasiswa(note: MahasiswaEntity)

    @Update
    suspend fun updateMahasiswa(note: MahasiswaEntity)

}