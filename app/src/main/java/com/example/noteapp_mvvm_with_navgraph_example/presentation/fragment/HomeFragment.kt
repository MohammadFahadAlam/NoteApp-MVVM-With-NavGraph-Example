package com.example.noteapp_mvvm_with_navgraph_example.presentation.fragment


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp_mvvm_with_navgraph_example.R
import com.example.noteapp_mvvm_with_navgraph_example.data.adapter.NoteAdapter
import com.example.noteapp_mvvm_with_navgraph_example.data.local.entities.Note
import com.example.noteapp_mvvm_with_navgraph_example.data.viewmodel.NoteViewModel
import com.example.noteapp_mvvm_with_navgraph_example.databinding.FragmentHomeBinding
import com.example.noteapp_mvvm_with_navgraph_example.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), SearchView.OnQueryTextListener {

    private val notesViewModel by activityViewModels<NoteViewModel>()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var notes: List<Note>
    private lateinit var staggerGridlayoutManager: StaggeredGridLayoutManager

    override fun setBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setUpRecyclerView()

        binding?.fabAddNote?.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()
        staggerGridlayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        binding?.recyclerView?.apply {
             layoutManager = staggerGridlayoutManager
            //layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }

        activity?.let {
            notesViewModel.allNotes().observe(it) { noteList ->
                notes = noteList
                noteAdapter.submitList(noteList)
                updateUI(noteList)
            }
        }
    }

    private fun updateUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding?.cardView?.visibility = View.GONE
            binding?.recyclerView?.visibility = View.VISIBLE
        } else {
            binding?.cardView?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.GONE
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
                mMenuSearch.isSubmitButtonEnabled = false
                mMenuSearch.setOnQueryTextListener(this@HomeFragment)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                menuItem.isChecked = true
                when (menuItem.itemId) {
                    R.id.sort_default -> {
                        sortNote(notes)
                    }
                    R.id.sort_ascending -> {
                        sortNote(notes.sortedBy { it.noteTitle })
                    }
                    R.id.sort_descending -> {
                        sortNote(notes.sortedByDescending { it.noteTitle })
                    }
                    R.id.sort_date_ascending -> {
                        sortNote(notes.sortedBy { it.updatedAt })
                    }
                    R.id.sort_date_descending -> {
                        sortNote(notes.sortedByDescending { it.updatedAt })
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun sortNote(notes: List<Note>) {
        //noteAdapter.setData(ArrayList(notes))
        noteAdapter.submitList(notes)
        lifecycleScope.launch {
            delay(500)
            binding?.recyclerView?.smoothScrollToPosition(0)
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNote(newText)
        }
        return true
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query%"
        notesViewModel.searchNote(searchQuery).observe(this) { notes ->
            noteAdapter.submitList(notes as ArrayList<Note>)
        }
    }
}