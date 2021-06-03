package com.example.tugas_final_reza.User

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_mahasiswa")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "url_pas_foto")
    var urlPasFoto: String?,

    var nama: String,

    var nim: String,

    var angkatan: Int,

    var umur: Int,

    @ColumnInfo(name = "jenis_kelamin")
    var jenisKelamin: String,

    var suku: String,

    var agama: String,

    @ColumnInfo(name = "tempat_lahir")
    var tempatLahir: String,

    @ColumnInfo(name = "tanggal_lahir")
    var tanggalLahir: String
)