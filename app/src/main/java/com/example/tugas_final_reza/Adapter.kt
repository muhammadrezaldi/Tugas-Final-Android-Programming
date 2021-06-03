package com.example.tugas_final_reza

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_final_reza.User.MahasiswaEntity
import kotlinx.android.synthetic.main.item_mahasiswa.view.*
import java.lang.Exception

class Adapter(private val list: List<MahasiswaEntity>, private val listener: Listener): RecyclerView.Adapter<Adapter.Holder>() {

    interface Listener {
        fun onClick(mahasiswaEntity: MahasiswaEntity)
    }
    
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswaEntity: MahasiswaEntity, listener: Listener) {
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
                itemView.setOnClickListener {
                    listener.onClick(mahasiswaEntity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}