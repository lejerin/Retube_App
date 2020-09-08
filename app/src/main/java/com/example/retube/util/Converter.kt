package com.example.retube.util

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

object Converter {
    fun formatTimeString(str: String) : String{

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val dateTime = ZonedDateTime.parse(str + "[Europe/London]")
            val a =  "" + dateTime.year + "-" + dateTime.monthValue + "-" + dateTime.dayOfMonth +
                    " " + dateTime.hour + ":" + dateTime.minute + ":" + dateTime.second
            val transFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val to = transFormat.parse(a)
                val regTime = to.time
                val curTime = Date().time - 9*60*60*1000

                var diffTime = (curTime - regTime) / 1000

                var msg: String? = null
                if (diffTime < 60) {
                    return "방금 전"
                }
                diffTime /= 60
                if(diffTime < 60){
                    return "" + diffTime + "분 전";
                }
                diffTime /= 60
                if(diffTime < 24){
                    return "" + diffTime + "시간 전";
                }
                diffTime /= 24
                if(diffTime < 30){
                    return "" + diffTime + "일 전";
                }
                diffTime /= 30
                if(diffTime < 12){
                    return "" + diffTime + "달 전";
                }
                diffTime /= 12
                return "" + diffTime + "년 전";


            }catch (e: ParseException){
                e.printStackTrace();
            }
        }

        return "error"
        }


    fun getNumlength(value: String): String {
        val num  = value.toInt()

        var length = (Math.log10(num.toDouble()) + 1).toInt()
        if (length == 4) {
            val a = num / 1000.0
            return (Math.floor(a * 10) / 10.0).toString() + "천"
        } else if (length == 5) {
            val a = num / 10000.0
            return (Math.floor(a * 10) / 10.0).toString() + "만"
        } else if (length > 5) {
            length = num / 10000
            return length.toString() + "만"
        }
        return num.toString()
    }

}