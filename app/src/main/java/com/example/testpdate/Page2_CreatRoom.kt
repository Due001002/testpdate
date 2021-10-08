package com.example.testpdate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_page2_creat_room.*

class Page2_CreatRoom : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var gSizePlayer = ""
    var gGetStringEDT = ""
    var gCheckSizePlayer = "กดเพื่อเลือกจำนวนผู้เล่น"
    var gDataArrayUer = mutableListOf<Map<String, Any>>()
    var db_data_next_room = db.collection("data").document("next").collection("room")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2_creat_room)
        btnBackPage1()
        setSpinner()
        btn_creatRoom()
    }


    //TODO BTN_Creat
    fun btn_creatRoom() {
        var bundle = intent.extras
        var getCheckName = bundle!!.getString("checkName")
        var getUserMapPage1 = bundle!!.get("userHashMap") as HashMap<String, Any>
        var getCardPage1 = bundle!!.get("card") as ArrayList<String>
        logdfix("userHashMap", "$getUserMapPage1")
        btnCreat_page2_CreatRoom.setOnClickListener {  //TODO btn_OnClick
            if (gSizePlayer == gCheckSizePlayer || edt_page2_CreatRoom.text.toString() == "") {
                if (gSizePlayer == gCheckSizePlayer && edt_page2_CreatRoom.text.toString() == "") {
                    toast("โปรดตั้งชื่อห้องและเลือกจำนวนผู้เล่น")
                }else if (gSizePlayer == gCheckSizePlayer) {
                    toast("โปรดเลือกจำนวนผู้เล่น")
                }else if (edt_page2_CreatRoom.text.toString() == "") {
                    toast("โปรดตั้งชื่อห้อง")
                }
            } else {
                gDataArrayUer.add(getUserMapPage1)
                gGetStringEDT = edt_page2_CreatRoom.text.toString()
                var convertG_sizePlayers  = gSizePlayer.toInt()
                var dataRoom = mapOf<String, Any>("hostName" to getUserMapPage1["name"].toString(),
                    "roomName" to gGetStringEDT,
                    "round" to 1,
                    "state" to 0,
                    "sizeUser" to convertG_sizePlayers,
                    "card" to getCardPage1,
                    "user" to gDataArrayUer
                )
                db_data_next_room.document(gGetStringEDT).set(dataRoom)
                var intent = Intent(this, Page3_GameUi::class.java)
                intent.putExtra("roomNamePage2_CreatRoom", gGetStringEDT)
                intent.putExtra("userHashMap", getUserMapPage1)
                intent.putExtra("checkName",getCheckName)
                intent.putExtra("card",getCardPage1)
                startActivity(intent)
            }
        }
    }

    //TODO onClickInten()
    fun setSpinner() {
        var arrays = mutableListOf<String>("กดเพื่อเลือกจำนวนผู้เล่น", "2", "3", "4")
        spn_selectPlayer.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrays)
        spn_selectPlayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gSizePlayer = arrays.get(p2)
                logdfix("spn", "Selec_spn:${arrays.get(p2)}")
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    //TODO BTN_Back
    private fun btnBackPage1() {
        btnBack_page2_CreatRoom.setOnClickListener {
            finish()
        }
    }

    //TODO toast
    fun toast(a: String) {
        val text = a
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    //TODO Logdfix
    fun logdfix(a: String, b: String) {
        Log.d(a, b)
    }

    override fun onDestroy() {
        super.onDestroy()
        toast("onDestroy")
        db_data_next_room.document(gGetStringEDT).delete().addOnSuccessListener {
            toast("Destroy !!! delete")
        }
    }
}