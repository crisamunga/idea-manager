package com.interview.ideamanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.interview.ideamanager.R
import com.interview.ideamanager.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels { AppViewModelProvider.Factory }
    private lateinit var adapter: ListTasksAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val activity = requireActivity() as AppCompatActivity

        activity.setSupportActionBar(binding.toolbar)

        setupMenu()
        setupRecyclerView()
        observers()
        listeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController();
    }

    private fun setupMenu() {
        binding.toolbar.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_language -> {
                        // Handle language action
                        true
                    }
                    R.id.action_settings -> {
                        // Handle settings action
                        true
                    }

                    else -> false
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = ListTasksAdapter(listOf()) { task -> addEditTask(task.id) }
        binding.recycler.adapter = adapter
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllTasks().collect { tasks ->
                adapter.tasks = tasks

                if (tasks.isEmpty()) {
                    binding.emptyState.visibility = View.VISIBLE
                    binding.recycler.visibility = View.GONE
                } else {
                    binding.emptyState.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun listeners() {
        binding.fabAdd.setOnClickListener { _ -> addEditTask() }
    }

    private fun addEditTask(taskId: Long? = null) {
//        TODO: Evaluate Bottom sheet vs separate page
//        val action = HomeFragmentDirections.actionHomeFragmentToAddEditTaskDialogFragment(taskId)
//        findNavController().navigate(action)
        AddEditTaskDialogFragment.newInstance(taskId).show(childFragmentManager, "AddEditTask")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
    }
}