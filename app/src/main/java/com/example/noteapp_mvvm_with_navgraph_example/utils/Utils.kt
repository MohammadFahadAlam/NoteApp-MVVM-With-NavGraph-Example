package com.example.noteapp_mvvm_with_navgraph_example.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custome_time_and_date_picker_dialog.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.makeGone() {
    this.visibility = View.GONE
}

fun makeVisible(vararg views: View) {
    for (view in views) {
        view.visibility = View.VISIBLE
    }
}

fun makeInvisible(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun makeGone(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun Context.toast(message: String){
    Toast.makeText(this, message , Toast.LENGTH_SHORT).show()
}

fun String.logE(key: String = "LOG") {
    Log.e(key, this)
}

fun String.logD(key: String = "LOG") {
    Log.d(key, this)
}

fun String.logI(key: String = "LOG") {
    Log.i(key, this)
}

fun String.toast(context: Context, show: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, show).show()
}

fun String.snackBar(view: View, show: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(view, this, show).show()
}

@SuppressLint("SimpleDateFormat")
fun String.dateTimeFormat(): String{
    //"EEEE, dd-MMM-yyyy hh:mm:ss a" => Saturday, 28-Jan-2023 12:07:12 AM
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(this)
        val formatted = current.format(formatter)
        formatted
    }else{
        val simpleDateFormat = SimpleDateFormat(this)
        val date: String = simpleDateFormat.format(Date())
        date
    }
}

fun Long.getDateTimeIntoLong(context: Context): String{
    val date = Date(this)
    val dateFormat = android.text.format.DateFormat.getLongDateFormat(context).format(date)
    val timeFormat = android.text.format.DateFormat.getTimeFormat(context).format(date)
    return "$dateFormat, $timeFormat"
}

fun getTime(dialog: Dialog): Long {
    val minute = dialog.timePicker.minute
    val hour = dialog.timePicker.hour
    val day = dialog.datePicker.dayOfMonth
    val month = dialog.datePicker.month
    val year = dialog.datePicker.year

    val calendar = Calendar.getInstance()
    calendar.set(year, month, day, hour, minute)
    return calendar.timeInMillis
}

fun Int.setSortType(context: Context){
    val sp = context.getSharedPreferences("SORT_BTN_TYPE_SP", Context.MODE_PRIVATE)
    sp.edit().putInt("SORT_BTN", this).apply()
}
fun getSortType(context: Context) : Int? {
    val sp = context.getSharedPreferences("SORT_BTN_TYPE_SP", Context.MODE_PRIVATE)
    return sp.getInt("SORT_BTN", -1)?:null
}