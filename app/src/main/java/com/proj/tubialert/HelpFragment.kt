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
                "\uD83D\uDFE2 Safe  – Normal condition.\n" +
                        "\uD83D\uDFE1 Monitoring Level – Rising water, stay alert.\n" +
                        "\uD83D\uDFE0 Alert Level – Possible flooding, prepare to evacuate.\n" +
                        "\uD83D\uDD34 Evacuate Level – Dangerous level, evacuate immediately."
            ),
            FAQItem(
                "How will I receive alerts?",
                "Residents who have registered will receive SMS alerts and in-app notifications during flood events."
            ),
            FAQItem(
                "How do I update my contact information?",
                "Go to Profile Settings in the menu to update your phone number, email, or password. [Note: Your Full Name and Gender cannot be edited after registration for security and verification purposes."
            ),
            FAQItem(
                "What if I don't receive SMS alerts?",
                "Go to your Profile Settings and double-check if your registered mobile number is correct.\n" +
                        "If you’re using a phone with two SIM cards, try switching to your other SIM number in the profile.\n" +
                        "Also check the Notification Settings inside your profile and make sure SMS Alerts are turned ON."
            ),
            FAQItem(
                "Where can I find emergency hotlines?",
                "Tap the Emergency Hotlines icon below (navigation bar) to see important contact numbers of Brgy. Gaya-Gaya. Including Ambulance, New Hotline - Main, and New Hotline - Annex."
            )
        )

        faqAdapter = FAQAdapter(faqList)
        faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        faqRecyclerView.adapter = faqAdapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        faqRecyclerView.addItemDecoration(divider)
    }
}