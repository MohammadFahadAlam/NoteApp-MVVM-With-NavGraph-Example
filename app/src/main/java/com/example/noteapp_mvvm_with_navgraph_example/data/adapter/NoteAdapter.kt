package com.example.noteapp_mvvm_with_navgraph_example.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.databinding.NoteLayoutAdapterBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment.HomeFragmentDirections
import com.example.noteapp_mvvm_with_navgraph_example.utils.getDateTimeIntoLong


class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DiffutilNoteItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class NoteViewHolder(private val itemBinding: NoteLayoutAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(note: Note) {

            with(itemBinding) {

                tvNoteTitle.text = note.noteTitle
                tvNoteBody.text = note.noteBody
                dateTime.text = note.updatedAt
                cardColor.setCardBackgroundColor(note.noteColor)


                when (note.alertStatus) {
                    0 -> {
                        alertTimeDate.text = note.time?.getDateTimeIntoLong(root.context)
                        setAlert.apply { isVisible = false }
                    }
                    1 -> {
                        alertTimeDate.text = note.time?.getDateTimeIntoLong(root.context)
                        setAlert.apply {
                            isVisible = true
                            setImageResource(R.drawable.ic_alarm_set)
                            imageTintList = resources.getColorStateList(R.color.green, null)
                        }
                    }
                    2 -> {
                        alertTimeDate.text = note.time?.getDateTimeIntoLong(root.context)
                        setAlert.apply {
                            isVisible = true
                            setImageResource(R.drawable.ic_alarm_set)
                            imageTintList = resources.getColorStateList(R.color.orange, null)
                        }
                    }
                }

                itemView.setOnClickListener { view ->
                    view.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(
                            note,
                            false
                        )
                    )
                }
            }
        }
    }
}

class DiffutilNoteItemCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
        oldItem.id == newItem.id &&
                oldItem.alertStatus == newItem.alertStatus &&
                oldItem.updatedAt == newItem.updatedAt &&
                oldItem.noteTitle == newItem.noteTitle &&
                oldItem.noteBody == newItem.noteBody

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}
