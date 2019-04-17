package com.example.lenovo.myproject.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lenovo.myproject.DB.Person
import com.example.lenovo.myproject.R
import com.example.lenovo.myproject.customview.UserImage

class ProgressFragment : Fragment() {
    companion object {
        fun newInstance(): ProgressFragment {
            return ProgressFragment()
        }
    }

    private lateinit var recycler: RecyclerView

    interface ProgressFragmentListener {
        fun onProgressDetailsButtonClicked()
        fun setProgressContacts(fragment: ProgressFragment)
    }

    private var listener: ProgressFragmentListener? = null
    private lateinit var adapter: ProgressFragment.Adapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ProgressFragmentListener) {
            listener = context
        } else {
            throw ClassCastException("$context is not ProgressFragmentListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailsButton = getView()?.findViewById<TextView>(R.id.tv_progress_details);
        detailsButton?.setOnClickListener {
            listener?.onProgressDetailsButtonClicked()
        }
        recycler = getView()?.findViewById(R.id.recycler_for_progress) ?: return
        adapter = Adapter(listOf(Person(0, "Гиль Валерия", "28")))
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        listener?.setProgressContacts(this)
    }

    fun updateData(contacts: List<Person>) {
        adapter.updateData(contacts)
    }

    inner class Adapter(private var contacts: List<Person>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, index: Int): Adapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val layout = R.layout.person_in_progress_list
            val view = inflater.inflate(layout, parent, false)
            return ViewHolder(view)
        }

        fun updateData(contacts: List<Person>) {
            this.contacts = contacts
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return contacts.size
        }

        override fun onBindViewHolder(holder: Adapter.ViewHolder, p: Int) {
            holder.bind(contacts[p]);
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val image: UserImage = view.findViewById(R.id.image)
            private val name: TextView = view.findViewById(R.id.name)
            private val scores: TextView = view.findViewById(R.id.badge_scores)

            fun bind(person: Person) {
                name.text = person.name
                scores.text = person.scores
                image.setInitials(UserImage.transform(person.name))
                image.setRoundColor(UserImage.getColor())
            }
        }
    }
}