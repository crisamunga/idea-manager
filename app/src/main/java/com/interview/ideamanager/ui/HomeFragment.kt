package com.interview.ideamanager.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                showSnackbar(getString(R.string.allow_notifications_rationale))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observers()
        listeners()
        permissions()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    private fun setupRecyclerView() {
        adapter = ListTasksAdapter(
            listOf(),
            onTaskClick = { task -> addEditTask(task.id) },
            onTaskToggle = viewModel::toggleTask
        )
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
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                viewModel.filter(checkedIds[0])
            }
        }
    }

    private fun permissions() {
        when {
            (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) -> {
                return
            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                return
            }
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.allow_notifications))
                    .setMessage(getString(R.string.allow_notifications_rationale))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.

                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

            }
        }
    }

    private fun addEditTask(taskId: Long? = null) {
        AddEditTaskDialogFragment.newInstance(taskId).show(childFragmentManager, "AddEditTask")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}