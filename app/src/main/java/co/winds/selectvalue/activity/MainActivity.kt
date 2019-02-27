package co.winds.selectvalue.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import co.winds.selectvalue.R
import co.winds.selectvalue.adapter.MultiSelectAdapter
import co.winds.selectvalue.model.Model
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MultiSelectAdapter.CallbackManager {

    private var adapter: MultiSelectAdapter? = null
    private var isAllSelectedFromAdapter: Boolean = false
    private lateinit var list:ArrayList<Model>
    var totalPrice=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list=ArrayList()
        list.add(Model("12/02/1923","INDS3432",30.00,false))
        list.add(Model("16/02/1223","INDS3462",10.50,false))
        list.add(Model("02/01/1353","INDS3992",10.00,false))


        adapter = MultiSelectAdapter(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        adapter!!.notifyAdapter(list)


        checkAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                totalPrice = 0.0
                for (i in 0 until list.size) {
                    val m: Model = list[i]
                    m.isMultiSelect = isChecked
                    totalPrice +=m.amount
                }
                if (!isAllSelectedFromAdapter) adapter!!.selectAllItems()
            } else {
                if (!isAllSelectedFromAdapter){
                    totalPrice = 0.0
                    adapter!!.deSelectAllItems()
                }
            }
            calculate()
        }

    }

    override fun onSelectOrDeSelectAll(isAllSelected: Boolean, isFromAdapter: Boolean) {
        isAllSelectedFromAdapter = isFromAdapter
        checkAll.isChecked = isAllSelected
        isAllSelectedFromAdapter = false
    }
  fun calculate(){
        total.text="Total price: $totalPrice"
        Log.d("TAGS", "Total price: $totalPrice")
    }

}
