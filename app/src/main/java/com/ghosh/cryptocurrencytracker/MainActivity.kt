package com.ghosh.cryptocurrencytracker

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ghosh.cryptocurrencytracker.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    var cryptoList=ArrayList<CryptoRvModel>()
    lateinit var cryptoAdapter: CryptoAdapter
    var permissionCode: Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)

        cryptoAdapter= CryptoAdapter(this,cryptoList)
        mainBinding.idRecycler.adapter=cryptoAdapter

        if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.INTERNET)
            !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.INTERNET),
                permissionCode)
        }

        getCryptoInfo()
        //val cryp:String=mainBinding.idETCurrency.text.toString()


        mainBinding.idETCurrency.addTextChangedListener(object: TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                filterCurr(s.toString())
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permissionCode && grantResults[0]==PackageManager.PERMISSION_GRANTED )
        {
            Toast.makeText(this@MainActivity,"Permission is granted.",Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this@MainActivity,"Please grant permissions.",Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun getCryptoInfo()
    {
        var url: String="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val requestQueue:RequestQueue= Volley.newRequestQueue(this@MainActivity)

        val jsonObjectRequest:JsonObjectRequest= @SuppressLint("NotifyDataSetChanged")
        object: JsonObjectRequest(Request.Method.GET,url,null,
            {response->

                cryptoList.clear()

                try{

                    var crypArray:JSONArray= response.getJSONArray("data")
                    for(i in 0..crypArray.length())
                    {
                        val crypObject:JSONObject= crypArray.getJSONObject(i)
                        var name:String=crypObject.getString("name")
                        var symbol:String=crypObject.getString("symbol")
                        var price:String="$ "+crypObject.getJSONObject("quote").
                        getJSONObject("USD").getDouble("price").toString()

                        cryptoList.add(CryptoRvModel(symbol, name, price))
                    }
                    cryptoAdapter.notifyDataSetChanged()

                }
                catch(e:JSONException)
                {
                    e.printStackTrace()
                }
            },{
                Toast.makeText(this@MainActivity,"Please correct the codes.",Toast.LENGTH_LONG).show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers=HashMap<String, String>()
                headers["X-CMC_PRO_API_KEY"]="70ceb578-1c46-4163-8ad4-a7bc3422ac0c"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    fun filterCurr(cryp: String)
    {
        var filtList=ArrayList<CryptoRvModel>()
        for(item in cryptoList)
        {
            if(item.name.toLowerCase().contains(cryp.toLowerCase()))
            {
                filtList.add(item)
            }
        }

        if(filtList.isEmpty())
        {
            Toast.makeText(this@MainActivity,"No Crypto Found....", Toast.LENGTH_LONG).show()
        }
        else
        {
            cryptoAdapter.filterList(filtList)
        }
    }
}