package com.vlad.ege_chemistry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vlad.ege_chemistry.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {

    var recyclerViewItems: ArrayList<String> = ArrayList<String>()
    val TAG = "LogRecyclerViewActivity"
    var userSelectedMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityRecyclerviewBinding>(this, R.layout.activity_recyclerview)
        userSelectedMode = intent.getStringExtra("type").toString()

        // Сохраняем значение userSelectedMode в SharedPreferences
        val sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("userSelectedMode", userSelectedMode)
        editor.apply()

        Log.d(TAG,userSelectedMode)
        inflateRecyclerViewItems()

        val adapter = RecyclerViewAdapter(recyclerViewItems,this)
        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

    }

    private fun inflateRecyclerViewItems(){
        Log.d(TAG,userSelectedMode)
        when(userSelectedMode){
            "teory" -> {
               var count = resources.getInteger(R.integer.teory_count)
                var textResourceId: Int
                for (i in 1..count){
                    textResourceId = resources.getIdentifier("teory$i","string",packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }
            "test" -> {
                recyclerViewItems.add("1 test Item")
                recyclerViewItems.add("2 test Item")
                recyclerViewItems.add("3 test Item")
                recyclerViewItems.add("4 test Item")
                recyclerViewItems.add("5 test Item")
            }
            "trialVariants" -> {
                recyclerViewItems.add("1 trialVariants Item")
                recyclerViewItems.add("2 trialVariants Item")
                recyclerViewItems.add("3 trialVariants Item")
                recyclerViewItems.add("4 trialVariants Item")
                recyclerViewItems.add("5 trialVariants Item")
            }else -> {
            Log.e(TAG,"ERROR")
            }
        }
    }
    fun goToActivity(position: Int){
        // Получаем значение userSelectedMode из SharedPreferences
        Log.d(TAG,"goToActivity")
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        Log.d(TAG,"goToActivity2")
        val userSelectedMode = sharedPref.getString("userSelectedMode", "").toString()
        Log.d(TAG,"goToActivity3")
        Log.d(TAG, userSelectedMode)

        var intent = Intent()
        when(userSelectedMode){
            "teory"->{
                intent = Intent(this,TeoryActivity::class.java)
            }
            "test"->{
                intent = Intent(this,TestActivity::class.java)
            }
            "trialVariants"->{
                intent = Intent(this,TrialVariantsActivity::class.java)
            }else ->{
                Log.e(TAG,"ERROR!")
            }
        }
        intent.putExtra("position",position)
        startActivity(intent)
    }
    }

class RecyclerViewAdapter(
    private val recyclerViewItems: ArrayList<String>,
                          private val recyclerViewActivity: RecyclerViewActivity) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val recyclerViewActivity: RecyclerViewActivity)
        : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewActivity.goToActivity(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view,recyclerViewActivity)

    }

    override fun getItemCount(): Int {
        return recyclerViewItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = recyclerViewItems[position]
        holder.textView.text = item
    }

}

