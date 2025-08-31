package com.proj.tubialert

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration

class HelpFragment : Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private val viewModel: HelpViewModel by viewModels()
    private lateinit var faqRecyclerView: RecyclerView
    private lateinit var faqAdapter: FAQAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        // Initialize RecyclerView
        faqRecyclerView = view.findViewById(R.id.faqRecyclerView)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        val faqList = listOf(
            FAQItem(
                "How to View Flood Alerts?",
                "Tap the Home Icon to view real-time water level, rain intensity, and temperature updates."
            ),
            FAQItem(
                "What do the Alert Colors Mean?",
                "Alert colors indicate different severity levels of flooding. Red indicates the most severe conditions, while yellow indicates caution."
            ),
            FAQItem(
                "How will I receive alerts?",
                "You will receive alerts via push notifications, SMS messages, and email based on your preferences."
            ),
            FAQItem(
                "How do I update my contact information?",
                "Go to Settings > Profile > Contact Information to update your phone number and email address."
            ),
            FAQItem(
                "What if I don't receive SMS alerts?",
                "Check your phone's signal, ensure notifications are enabled, and verify your contact number in the app settings."
            ),
            FAQItem(
                "Where can I find emergency hotlines?",
                "Emergency hotlines are listed in the Emergency section of the app, accessible from the main menu."
            )
        )

        faqAdapter = FAQAdapter(faqList)
        faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        faqRecyclerView.adapter = faqAdapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        faqRecyclerView.addItemDecoration(divider)
    }
}