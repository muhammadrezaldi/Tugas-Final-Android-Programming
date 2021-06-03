package com.example.tugas_final_reza.User

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_final_reza.R
import kotlinx.android.synthetic.main.item_mahasiswa.view.*
import java.lang.Exception

class Adapter(private val list: List<MahasiswaEntity>): RecyclerView.Adapter<Adapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswaEntity: MahasiswaEntity) {
            with(itemView) {
                if(mahasiswaEntity.urlPasFoto != "") {
                    try {
                        pasfoto.setImageURI(Uri.parse(mahasiswaEntity.urlPasFoto))
                    } catch (e: Exception){}
                } else {
                    if(mahasiswaEntity.jenisKelamin == "Laki-laki") {
                        pasfoto.setImageResource(R.drawable.ic_man)
                    } else {
                        pasfoto.setImageResource(R.drawable.ic_woman)
                    }
                }
                nama.text = mahasiswaEntity.nama
                nim.text = mahasiswaEntity.nim
                angkatan.text = mahasiswaEntity.angkatan.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Adapter.Holder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}