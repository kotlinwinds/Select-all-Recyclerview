package co.winds.selectvalue.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import co.winds.selectvalue.R
import co.winds.selectvalue.activity.MainActivity
import co.winds.selectvalue.adapter.callback.MultiSelectCallback
import co.winds.selectvalue.model.Model
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.*


class MultiSelectAdapter(private val callbackManager: CallbackManager?):RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>() {
    private val personItemList: MutableList<Model>?
    private val selectedItemsList: MutableSet<String>

    init {
        personItemList = ArrayList()
        selectedItemsList = HashSet()
    }

    interface CallbackManager {
        fun onSelectOrDeSelectAll(isAllSelected: Boolean, isFromAdapter: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MultiSelectViewHolder(v)
    }

    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        val item = personItemList!![position]
        holder.bindDataToView(item, position)
    }

    override fun getItemCount(): Int {
        return personItemList!!.size
    }



    fun notifyAdapter(newList: List<Model>) {
        val callback = MultiSelectCallback(this.personItemList!!, newList)
        val diffResult = DiffUtil.calculateDiff(callback)

        this.personItemList.clear()
        this.personItemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun selectAllItems() {
        if (personItemList == null) return

        var i = 0
        val personItemListSize = personItemList.size
        while (i < personItemListSize) {
            val item = personItemList[i]

            if (!item.isMultiSelect) item.isMultiSelect = true

            if (!selectedItemsList.contains(item.id)) {
                selectedItemsList.add(item.id)
            }
            i++
        }
        notifyDataSetChanged()
    }

    fun deSelectAllItems() {
        if (personItemList == null) return

        var i = 0
        val personItemListSize = personItemList.size
        while (i < personItemListSize) {
            val item = personItemList[i]

            if (item.isMultiSelect) item.isMultiSelect = false

            if (selectedItemsList.contains(item.id)) {
                selectedItemsList.remove(item.id)
            }
            i++
        }
        notifyDataSetChanged()
    }

    inner class MultiSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.checkBox4
        var context=itemView.context

        fun bindDataToView(item: Model, position: Int) {
            this.setIsRecyclable(false)
            checkBox.setOnCheckedChangeListener(null)
            itemView.textView5.text = item.date
            itemView.textView6.text =item.amount.toString()
            checkBox.isChecked = item.isMultiSelect
            checkBox.tag = position


            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    if (!selectedItemsList.contains(item.id)) {
                        selectedItemsList.add(item.id)
                        (context as MainActivity).totalPrice+=item.amount
                    }
                } else {
                    if (selectedItemsList.contains(item.id)) {
                        selectedItemsList.remove(item.id)
                        (context as MainActivity).totalPrice-=item.amount
                    }
                }
                personItemList!![adapterPosition].isMultiSelect = isChecked
                if (personItemList.size == selectedItemsList.size) {
                    callbackManager?.onSelectOrDeSelectAll(true, true)
                } else {
                    callbackManager?.onSelectOrDeSelectAll(false, true)
                }
                (context as MainActivity).calculate()
            }
        }
    }
}
