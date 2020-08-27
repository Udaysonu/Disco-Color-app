package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorSpace
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ALL
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Proxy
import java.util.*
import kotlin.collections.RandomAccess
import kotlin.random.Random

class MainActivity : Activity(){
    lateinit  var sensorManager: SensorManager
    lateinit var proxSensor:Sensor
    lateinit var accSensor:Sensor
    lateinit var proxySensorListener:SensorEventListener
    lateinit var accSensorListener:SensorEventListener
    val colors=arrayOf(Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.MAGENTA,Color.TRANSPARENT,Color.WHITE,Color.YELLOW,Color.GRAY)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maing


        if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),1212);
        }
//
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
       proxSensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        accSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        proxySensorListener=object:SensorEventListener{
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                Log.d("SENSOR","ACCURACYCHANGED")
            }

            override fun onSensorChanged(p0: SensorEvent?) {

                if(p0?.values?.get(0)!! >0)
                {
                    top.setBackgroundColor(colors[Random.nextInt(9)])
                }
            }

        }

        accSensorListener=object:SensorEventListener{
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                        Log.d("SENSOR","ACCURACYCHANGED")
              }

            override fun onSensorChanged(p0: SensorEvent?) {
                val setcolor=Color.rgb(conv(p0?.values?.get(0)!!),conv(p0?.values[1]),conv(p0?.values[2]))
                bottom.setBackgroundColor(setcolor)
            }

        }


     }

    private fun conv(fl: Float): Int {
            return (((fl+12)/24)*255).toInt()
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(   proxySensorListener,proxSensor!!,1000*1000)
        sensorManager.registerListener(accSensorListener,accSensor,1000*1000)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(proxySensorListener)
    }

}
