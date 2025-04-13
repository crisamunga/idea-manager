package com.interview.ideamanager.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.interview.ideamanager.R
import com.interview.ideamanager.databinding.FragmentAddEditTaskDialogBinding
import kotlinx.coroutines.launch
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TASK_ID = "TASK_ID"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEditTaskDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEditTaskDialogFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var _taskId: Long? = null
    private lateinit var _binding: FragmentAddEditTaskDialogBinding
    private val _viewModel: AddEditTaskViewModel by viewModels { AppViewModelProvider.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(TASK_ID)) _taskId = it.getLong(TASK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditTaskDialogBinding.inflate(inflater, container, false)

        if (_taskId == null) {
            _binding.tvHeader.text = getString(R.string.add_task)
            _binding.btnDelete.visibility = View.GONE
            _binding.btnToggle.visibility = View.GONE
            _binding.btnSave.text = getString(R.string.add_task)
        } else {
            _binding.tvHeader.text = getString(R.string.edit_task)
            _binding.btnDelete.visibility = View.VISIBLE
            _binding.btnToggle.visibility = View.VISIBLE
            _binding.btnSave.text = getString(R.string.edit_task)
        }

        _viewModel.taskId = _taskId

        observers()
        listeners()

        return _binding.root
    }

    private fun observers () {
        viewLifecycleOwner.lifecycleScope.launch {
            _viewModel.formState.collect { formState ->
                _binding.tilTitle.error = formState.titleError?.let { getString(it) }
                _binding.tilDescription.error = formState.descriptionError?.let { getString(it) }
                formState.dueDateError?.let {
                    _binding.tvDateError.text = getString(it)
                    _binding.tvDateError.visibility = View.VISIBLE
                } ?: run {
                    _binding.tvDateError.visibility = View.GONE
                }
                if (formState.isValid) {
                    _binding.btnSave.isEnabled = true
                } else {
                    _binding.btnSave.isEnabled = false
                }
            }
        }

        _taskId ?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                _viewModel.getTaskById(it).collect { task ->
                    task ?.let {
                        _viewModel.title = task.title
                        _viewModel.description = task.description
                        _viewModel.dueDate = task.dueDate
                        _viewModel.isCompleted = task.isCompleted

                        _binding.tilTitle.editText?.setText(task.title)
                        _binding.tilDescription.editText?.setText(task.description)
                        _binding.btnDate.text = getString(R.string.due_date, task.dueDate.toString())
                    } ?: run {
                        _viewModel.taskId = null
                        _viewModel.title = ""
                        _viewModel.description = null
                        _viewModel.dueDate = null
                        _viewModel.isCompleted = false

                        _binding.tilTitle.editText?.setText("")
                        _binding.tilDescription.editText?.setText("")
                        _binding.btnDate.text = getString(R.string.pick_a_date)
                    }
                }
            }
        }
    }

    private fun listeners() {
        _binding.etTitle.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                _binding.tilTitle.error = null
            }
        }
        _binding.etDescription.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                _binding.tilDescription.error = null
            }
        }
        _binding.etTitle.doOnTextChanged { text, _, _, _ ->
            _viewModel.title = text.toString()
            _viewModel.validate()
        }
        _binding.etDescription.doOnTextChanged { text, _, _, _ ->
            _viewModel.description = text.toString()
            _viewModel.validate()
        }
        _binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_task))
                .setMessage(getString(R.string.delete_task_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    _viewModel.delete()
                    dismiss()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
        _binding.btnDate.setOnClickListener {
            val date = _viewModel.dueDate ?: LocalDate.now()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    _viewModel.dueDate = LocalDate.of(year, month, dayOfMonth)
                    _binding.btnDate.text = getString(R.string.due_date, _viewModel.dueDate)
                    _viewModel.validate()
                },
                date.year,
                date.monthValue,
                date.dayOfMonth
            ).show()
        }
        _binding.btnToggle.setOnClickListener {
            _viewModel.toggle()
            dismiss()
        }


        _binding.btnSave.setOnClickListener {
            _viewModel.save()
            dismiss()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param taskId Task ID of the task to be updated.
         * @return A new instance of fragment AddEditTaskDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(taskId: Long? = null) =
            AddEditTaskDialogFragment().apply {
                arguments = Bundle().apply {
                    if (taskId != null) {
                        putLong(TASK_ID, taskId)
                    }
                }
            }
    }
}