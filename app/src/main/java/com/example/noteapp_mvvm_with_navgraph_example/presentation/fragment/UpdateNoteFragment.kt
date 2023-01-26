package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.adapter.NoteAdapter
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentUpdateNoteBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import com.example.noteapp_mvvm_with_navgraph_example.utils.toast
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateNoteFragment : BaseFragment<FragmentUpdateNoteBinding>() {

    private val notesViewModel by activityViewModels<NoteViewModel>()

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note

    private var selectedColor = 0

    override fun setBinding(): FragmentUpdateNoteBinding =
        FragmentUpdateNoteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UpdateNoteFragmentArgs

        currentNote = args.note!!

        binding?.apply {

            etNoteBodyUpdate.setText(currentNote.noteBody)
            etNoteTitleUpdate.setText(currentNote.noteTitle)
            chooseColorMcvBtn.setCardBackgroundColor(currentNote.noteColor)

            ColorPickerDialog.Builder(activity)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, fromUser ->
                        chooseColorMcvBtn.setCardBackgroundColor(envelope.color)
                        cardView.setCardBackgroundColor(envelope.color)
                        currentNote.noteColor = envelope.color
                    })
                .setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()

        }

        binding?.fabDone?.setOnClickListener {
            val title = binding?.etNoteTitleUpdate?.text.toString().trim()
            val body = binding?.etNoteBodyUpdate?.text.toString().trim()

            if (title.isNotEmpty() && body.isNotEmpty()) {
                currentNote.noteTitle = title
                currentNote.noteBody = body
                notesViewModel.updateNote(currentNote)

                findNavController().popBackStack()

            } else {
                activity?.toast("Field is empty!")
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                notesViewModel.deleteNote(currentNote)

                findNavController().popBackStack()
            }
            setNegativeButton("CANCEL", null)
        }.create().show()

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}