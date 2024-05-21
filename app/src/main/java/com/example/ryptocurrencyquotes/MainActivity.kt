package com.example.ryptocurrencyquotes

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var apiUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=SOL,LTC"
    private lateinit var arrayOfCrypto: Array<String>
    private lateinit var tableLayout: TableLayout
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayOfCrypto = arrayOf("RUB", "USD", "EUR", "BTC", "TON")

        var tsyms = "&tsyms="
        for (crypto in arrayOfCrypto){
            tsyms += "$crypto,"
        }

        apiUrl += tsyms

        tableLayout = findViewById(R.id.cryptoTable)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            getCryptoQuotes()
        }
    }

    private fun getCryptoQuotes() {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonRequest = JsonObjectRequest(Request.Method.GET, apiUrl, null,
            { response ->
                showCryptoQuotes(response)
            },
            { error ->
                Toast.makeText(applicationContext, "Error in receiving data!", Toast.LENGTH_SHORT).show()
            })
        requestQueue.add(jsonRequest)
    }

    private fun showCryptoQuotes(quotes: JSONObject) {
        Log.e("me", quotes.toString())
        tableLayout.removeAllViews()

        val headerRow = TableRow(this)

        val headerCurrency = TextView(this)
        val headerSol = TextView(this)
        val headerLtc = TextView(this)

        headerCurrency.text = "Currency"
        headerSol.text = "SOL"
        headerLtc.text = "LTC"

        headerRow.addView(headerCurrency)
        headerRow.addView(headerSol)
        headerRow.addView(headerLtc)

        tableLayout.addView(headerRow)

        for (currency in arrayOfCrypto) {
            val tableRow = TableRow(this)

            val currencyTextView = TextView(this)
            val solValue = TextView(this)
            val ltcValue = TextView(this)

            currencyTextView.text = currency
            solValue.text = quotes.getJSONObject("SOL").getString(currency)
            ltcValue.text = quotes.getJSONObject("LTC").getString(currency)

            tableRow.addView(currencyTextView)
            tableRow.addView(solValue)
            tableRow.addView(ltcValue)

            tableLayout.addView(tableRow)
        }
    }
}
