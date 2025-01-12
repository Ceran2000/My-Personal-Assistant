package com.example.mypersonalassistant.ui.create_update_task_list

enum class ReminderTimeUnit(val labelText: String, val minuteMultipler: Int) {
    MINUTES("minut", 1),
    HOUR("godzin", 60),
    DAY("dni", 1440),
    WEEK("tygodni", 10080)
}