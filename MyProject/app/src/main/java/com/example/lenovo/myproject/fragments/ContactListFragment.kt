package com.example.lenovo.myproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.customview.UserImage
import java.lang.Math.min
import kotlin.random.Random

class ContactListFragment : Fragment() {
    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = getView()?.findViewById(R.id.contacts_recycler_view) ?: return
        adapter = Adapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
    }

    fun updateData(contacts: List<String>) {
        adapter.updateData(contacts)
    }

    fun changeElementsLocation() {
        if (recycler.layoutManager !is GridLayoutManager) {
            recycler.layoutManager = GridLayoutManager(context, 3)
        } else {
            recycler.layoutManager = LinearLayoutManager(context)
        }
        adapter = Adapter(adapter.getContacts())
        recycler.adapter = adapter
    }

    inner class Adapter(private var contacts: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): Adapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            var layout = R.layout.contact_info_grid
            if (recycler.layoutManager !is GridLayoutManager) {
                layout = R.layout.contact_info
            }
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(contacts: List<String>) {
            this.contacts = contacts
            notifyDataSetChanged()
        }

        fun getContacts() : List<String> {
            return contacts
        }

        override fun getItemCount(): Int {
            return contacts.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, p: Int) {
            holder.bind(contacts[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val image: UserImage = view.findViewById(R.id.user_image)
            private val name: TextView = view.findViewById(R.id.name)
            private val scores: TextView = view.findViewById(R.id.scores)

            fun bind(s: String) {
                name.text = s
                scores.text = Random.nextInt(0, 500).toString()
                image.setInitials(transform(s))
                image.setRoundColor(Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
            }

            private fun transform(s: String): String {
                val separate = s.split(" ")
                var res = ""
                for (i in 0 until min(separate.size, 2)) {
                    res += separate[i][0]
                }
                return res
            }
        }
    }
}