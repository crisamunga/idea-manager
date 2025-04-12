package com.interview.ideamanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.interview.ideamanager.R
import com.interview.ideamanager.databinding.FragmentAddEditTaskDialogBinding

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
    private var taskId: Int? = null
    private lateinit var _binding: FragmentAddEditTaskDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskId = it.getInt(TASK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddEditTaskDialogBinding.inflate(inflater, container, false)

        return _binding.root
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
        fun newInstance(taskId: Int? = null) =
            AddEditTaskDialogFragment().apply {
                arguments = Bundle().apply {
                    if (taskId != null) {
                        putInt(TASK_ID, taskId)
                    }
                }
            }
    }
}