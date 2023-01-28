package com.example.poo

//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null;
    lateinit var textViewInfo: TextView;
    var textViewTagInfo: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewInfo = findViewById<TextView>(R.id.info);
        textViewTagInfo = findViewById<TextView>(R.id.taginfo);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this,
                "NFC NOT supported on this devices!",
            Toast.LENGTH_LONG).show();
            finish();
        } else if (!nfcAdapter!!.isEnabled()) {
            Toast.makeText(this,
                "NFC NOT Enabled!",
                Toast.LENGTH_LONG).show();
            finish();
        }





        var jota: Person = Person("Flubber", "ASDF4654D");
        println(jota.alive)
        println("Printing what is printable")
        println(jota.name + " EL NOMBRE");
        println(jota.passport + " PASAPORTE");

        var anonimo:Person = Person();
        anonimo.Person()
        println(anonimo.name + " EL NOMBRE");
        println(anonimo.passport + " PASAPORTE");

        var bicho: Pokemon = Pokemon();
        bicho.setName("Un nombre bien Chido");
        println(bicho.getName() + " GETTING THE NAME");
    }

    protected override fun onResume() {
        super.onResume()

        var intent: Intent = intent;
        var action: String? = intent.action;
        println("$action  PRINTING THE ACTION. THIS IS EQUAL TO ACTION_TECH_DISCOVERD");
        if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            Toast.makeText(this,
                "onResume() - ACTION_TECH_DISCOVERED",
            Toast.LENGTH_SHORT).show();
            println("${NfcAdapter.EXTRA_TAG} PRINTING NfcAdapter.EXTRA_TAG");
            var tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            println("PRINTING TAG");
            println(tag);
            println("PRINTING TAG");

            if (tag == null) {
                textViewInfo.text = "tag == null";
            } else {
                var tagInfo: String = tag.toString() + "\n";
                //println("$tagInfo TAG TO STRING. TAGINFO");

                tagInfo += "\nTag Id: \n";
                println("$tagInfo TAG TO STRING. TAGINFO");
                // tagId represets a byte array
                var tagId: ByteArray  = tag.id;
                println("$tagId TAGID. BYTEARRAY");
                tagInfo += "length = " + tagId.size +"\n";
                /*for (i in 0..tagId.size) {
                    // and 0x000FF000
                    // tagId[i] and(0xFF)
                    tagInfo += Integer.toHexString( tagId[i].toInt().and(0xFF) ) + " ";
                } */
                tagInfo += "\n"

                var techList = tag.techList;
                println("$techList TECHLIST FROM TAG.TECHLIST");
                tagInfo += "\nTech List\n";
                tagInfo += "length = " + techList.size +"\n";
                /*for (i in 0..techList.size) {
                    tagInfo += techList[i] + "\n ";
                }*/

                //textViewInfo.text = tagInfo;
                textViewInfo.text = tagInfo;

                readMifareClassic(tag);
            }
        } else {
            Toast.makeText(this,
                "onResume() : $action",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    private fun readMifareClassic(tag: Tag) {
        var mifareClassicTag: MifareClassic = MifareClassic.get(tag);
        mifareClassicTag.connect();
        try {
            //var value = MifareClassic.get(tag).authenticateSectorWithKeyA(12, "C9855A4DA3E0".toByteArray());

            //var myByteArray = "C9855A4DA3E0".toByteArray();
            //var myString = String(myByteArray, Charset.forName("US-ASCII"))
            //println("$myString EL STRING DE REGRESO INTENTADO CONVERTIDO CHIDO");
            /*
            *
                int len = keyString.length();
                byte[] authKeyData = new byte[len / 2];
                for (int i = 0; i < len; i += 2) {
                    authKeyData[i / 2] = (byte) ((Character.digit(keyString.charAt(i), 16) << 4)
                    + Character.digit(keyString.charAt(i+1), 16));
                }
        }*/
            var keyString = "C9855A4DA3E0";
            var length = keyString.length;
            var authKeyData = ByteArray(length / 2);
            for (i in 0 until length step 2) {
                println("$i EL ITERADOR");
                authKeyData[i / 2] = (((Character.digit(keyString[i], 16).shl(4))
                        + Character.digit(keyString[i+1], 16)).toByte());
                println("AQUI ABAJO. SIN ERROR HASTA EL FINAL DE LA ITERACION");
            }
            var authenticated = mifareClassicTag.authenticateSectorWithKeyA(3, authKeyData);
            println("$authenticated is Authenticated");
            if (authenticated) {
                println("HEREEEEEEEEEEEEEEEEE INSIDE");
                var block = mifareClassicTag.readBlock(12);
                var stringResponse = String(block, StandardCharsets.US_ASCII);
                println("$stringResponse LA RESPUESTA DEL BLOQUE EN STRING");
            }
            //var stringReceived = value.toString();
            //println("$stringReceived EL VALOR OBTENIDO DEL BLOQUE 12");
            //println("$stringReceived EL VALOR OBTENIDO DEL BLOQUE 12")
        } catch (e: Exception) {
            println(e.message)
            println(e.stackTrace.toString())
            println("ERROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOR");
        }
        var typeInfoString = "--- MifareClassic tag ---\n";
        var type: Int = mifareClassicTag.type

        typeInfoString += when(type) {
            MifareClassic.TYPE_PLUS -> "MifareClassic.TYPE_PLUS\n";
            MifareClassic.TYPE_PRO -> "MifareClassic.TYPE_PRO\n";
            MifareClassic.TYPE_CLASSIC -> "MifareClassic.TYPE_CLASSIC\n";
            MifareClassic.TYPE_UNKNOWN -> "MifareClassic.TYPE_UNKNOWN\n";
            else -> "unknown...!\n";
        }

        var size: Int = mifareClassicTag.size;
        typeInfoString += when(size) {
            MifareClassic.SIZE_1K -> "MifareClassic.SIZE_1K\n";
            MifareClassic.SIZE_2K -> "MifareClassic.SIZE_2K\n";
            MifareClassic.SIZE_4K -> "MifareClassic.SIZE_4K\n";
            MifareClassic.SIZE_MINI -> "MifareClassic.SIZE_MINI\n";
            else -> "unknown size...!\n";
        }
        var blockCount: Int = mifareClassicTag.blockCount;
        typeInfoString += "BlockCount \t= $blockCount\n";
        var sectorCount: Int = mifareClassicTag.sectorCount;
        typeInfoString += "SectorCount \t= $sectorCount\n";

        textViewInfo.text = typeInfoString;
        mifareClassicTag.close();
    }

}