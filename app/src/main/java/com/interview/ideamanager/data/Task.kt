package com.interview.ideamanager.data

class Task (
    val id: Long,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val dueDate: Long? = null,
)