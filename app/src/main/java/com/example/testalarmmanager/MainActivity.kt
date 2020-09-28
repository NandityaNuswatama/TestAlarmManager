package com.example.testalarmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSpinner()
        spinner.onItemSelectedListener = this

        alarmReceiver = AlarmReceiver()

        btn_alarm.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_alarm -> {
                alarmReceiver.set6Times(this)
                btn_alarm.isEnabled = false
                btn_cancel.isEnabled = true
            }
            R.id.btn_cancel -> {
                alarmReceiver.cancelAlarm(this)
                btn_alarm.isEnabled = true
                btn_cancel.isEnabled = false
            }
        }
    }

    private fun setSpinner(){
        ArrayAdapter.createFromResource(this, R.array.alarm_times, android.R.layout.simple_spinner_dropdown_item)
            .also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        parent.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        parent.resources.getString(R.string.spinner_hint)
    }
}