package com.example.firebaselogin.fragments

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList


class view_by_graph : Fragment() {

    lateinit var barListIncome: ArrayList<BarEntry>
    lateinit var barListOutcome: ArrayList<BarEntry>
    lateinit var incomeBarDataSet: BarDataSet
    lateinit var expenseBarDataSet: BarDataSet
    lateinit var barData: BarData
    lateinit var barChart: BarChart
    lateinit var listOfExpenses: Array<Int>
    lateinit var listOfIncomes: Array<Int>
    lateinit var sp: SharedPreferences
    var typeOfGraph = false // if false then month if true then year

    // ProgreDialog
    private lateinit var progressBar: ProgressDialog
    lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_graph_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressDialog(context)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Fetching Bar... Press the screen after the message disappear.")
        progressBar.setCanceledOnTouchOutside(false)
        barChart = view.findViewById(R.id.barChart) as BarChart
        fireBaseAuth = FirebaseAuth.getInstance()

        sp = PreferenceManager.getDefaultSharedPreferences(context)
        typeOfGraph = sp.getBoolean("GraphPerYear", false)
        val s = fireBaseAuth.currentUser?.email.toString()
        val beforeTheMark = s.split("@")
        val afterTheMark = beforeTheMark[1].split(".")
        dbRef = FirebaseDatabase.getInstance().getReference()
            .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")


        // set the expens array and the income array to display later on the bar chart
        if (!typeOfGraph) {
            listOfIncomes = arrayOf<Int>(0, 0, 0, 0)
            listOfExpenses = arrayOf<Int>(0, 0, 0, 0)
        } else {
            listOfIncomes = arrayOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            listOfExpenses = arrayOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }

    }


    // will set the graph to be displayed per month - 4  weeks
    private fun setGraphPerMonth(
        barChart: BarChart,
        listOfIncome: Array<Int>,
        listOfExpense: Array<Int>
    ) {
        resetChart()
        barListIncome = ArrayList<BarEntry>()
        barListIncome.add(BarEntry(1f, listOfIncome[0].toFloat()))
        barListIncome.add(BarEntry(2f, listOfIncome[1].toFloat()))
        barListIncome.add(BarEntry(3f, listOfIncome[2].toFloat()))
        barListIncome.add(BarEntry(4f, listOfIncome[3].toFloat()))

        barListOutcome = ArrayList<BarEntry>()
        barListOutcome.add(BarEntry(1f, listOfExpense[0].toFloat()))
        barListOutcome.add(BarEntry(2f, listOfExpense[1].toFloat()))
        barListOutcome.add(BarEntry(3f, listOfExpense[2].toFloat()))
        barListOutcome.add(BarEntry(4f, listOfExpense[3].toFloat()))

        incomeBarDataSet = BarDataSet(barListIncome, "Incomes")
        incomeBarDataSet.formSize
        incomeBarDataSet.setColor(Color.GREEN)
        expenseBarDataSet = BarDataSet(barListOutcome, "Expenses")
        expenseBarDataSet.setColor(Color.RED)
        barData = BarData(incomeBarDataSet, expenseBarDataSet)
        barChart.data = barData
        barChart.description.text = " "

        barChart.setFitBars(true)
        incomeBarDataSet.valueTextColor = Color.BLACK
        expenseBarDataSet.valueTextColor = Color.BLACK
        incomeBarDataSet.valueTextSize = 15f
        expenseBarDataSet.valueTextSize = 15f
        val xAxis: XAxis = barChart.xAxis
        xAxis.setLabelCount(4)
        var weeks = arrayOf<String>("Week1", "Week2", "Week3", "Week4")
        xAxis.setValueFormatter(IndexAxisValueFormatter(weeks))
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        barChart.isDragEnabled = true
        var barSpace = 0.1f
        var groupSpace = 0.5f
        barData.barWidth = 0.15f
        barChart.xAxis.axisMinimum = 0f
        barChart.animateXY(0, 3000)
        barChart.groupBars(0F, groupSpace, barSpace)
        barChart.invalidate()
    }

    // will set the  graph to be  displayed per months - 12
    private fun setGraphPerYear(
        barChart: BarChart,
        listOfIncome: Array<Int>,
        listOfExpense: Array<Int>
    ) {
        resetChart()
        barListIncome = ArrayList()

        barListIncome.add(BarEntry(1f, listOfIncome[0].toFloat()))
        barListIncome.add(BarEntry(2f, listOfIncome[1].toFloat()))
        barListIncome.add(BarEntry(3f, listOfIncome[2].toFloat()))
        barListIncome.add(BarEntry(4f, listOfIncome[3].toFloat()))
        barListIncome.add(BarEntry(5f, listOfIncome[4].toFloat()))
        barListIncome.add(BarEntry(6f, listOfIncome[5].toFloat()))
        barListIncome.add(BarEntry(7f, listOfIncome[6].toFloat()))
        barListIncome.add(BarEntry(8f, listOfIncome[7].toFloat()))
        barListIncome.add(BarEntry(9f, listOfIncome[8].toFloat()))
        barListIncome.add(BarEntry(10f, listOfIncome[9].toFloat()))
        barListIncome.add(BarEntry(11f, listOfIncome[10].toFloat()))
        barListIncome.add(BarEntry(12f, listOfIncome[11].toFloat()))

        barListOutcome = ArrayList<BarEntry>()
        barListOutcome.add(BarEntry(1f, listOfExpense[0].toFloat()))
        barListOutcome.add(BarEntry(2f, listOfExpense[1].toFloat()))
        barListOutcome.add(BarEntry(3f, listOfExpense[2].toFloat()))
        barListOutcome.add(BarEntry(4f, listOfExpense[3].toFloat()))
        barListOutcome.add(BarEntry(5f, listOfExpense[4].toFloat()))
        barListOutcome.add(BarEntry(6f, listOfExpense[5].toFloat()))
        barListOutcome.add(BarEntry(7f, listOfExpense[6].toFloat()))
        barListOutcome.add(BarEntry(8f, listOfExpense[7].toFloat()))
        barListOutcome.add(BarEntry(9f, listOfExpense[8].toFloat()))
        barListOutcome.add(BarEntry(10f, listOfExpense[9].toFloat()))
        barListOutcome.add(BarEntry(11f, listOfExpense[10].toFloat()))
        barListOutcome.add(BarEntry(12f, listOfExpense[11].toFloat()))

        incomeBarDataSet = BarDataSet(barListIncome, "Incomes")
        incomeBarDataSet.formSize
        incomeBarDataSet.setColor(Color.GREEN)
        expenseBarDataSet = BarDataSet(barListOutcome, "Expenses")
        expenseBarDataSet.setColor(Color.RED)
        barData = BarData(incomeBarDataSet, expenseBarDataSet)
        barChart.data = barData
        barChart.description.text = " "

        barChart.setFitBars(true)
        incomeBarDataSet.valueTextColor = Color.BLACK
        expenseBarDataSet.valueTextColor = Color.BLACK
        incomeBarDataSet.valueTextSize = 15f
        expenseBarDataSet.valueTextSize = 15f

        val xAxis: XAxis = barChart.xAxis
        xAxis.setLabelCount(12)
        var monthes = arrayOf<String>(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Aoc",
            "Nov",
            "Dec"
        )
        xAxis.setValueFormatter(IndexAxisValueFormatter(monthes))
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        barChart.isDragEnabled = true
        var barSpace = 0.1f
        var groupSpace = 0.5f
        barData.barWidth = 0.15f
        barChart.xAxis.axisMinimum = 0f
        barChart.animateXY(0, 3000)
        barChart.groupBars(0F, groupSpace, barSpace)
        barChart.invalidate()
    }


    /**
     * Populate the graph data by the user's choises.
     * If the user chose to use the month show then the population will be detributed by the 4 weeks of the month
     * that has been chosen in the fragment before that.
     * If by year then the graph will show all the 12 weeks of the year that has been selected
     * **/
    private fun setValues(
        date: String,
        listOfIncome: Array<Int>,
        listOfExpense: Array<Int>,
    ) {
        this.dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    var totalIncome = 0
                    var counter = 0
                    clearArray()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(IncomeClass::class.java)


                        if (user != null) {
                            var tempDate = user.dateOfIncome!!
                            var dateToBeChecked = tempDate.split("/")
                            if (!typeOfGraph) { // if the user chose to see as month - populate by weeks
                                if (dateToBeChecked[1] == date) {
                                    if (dateToBeChecked[2].toInt() > 0 && dateToBeChecked[2].toInt() <= 7) {
                                        if (user.incomeAcmount?.toInt()!! > 0)
                                            listOfIncome[0] += user.incomeAcmount?.toInt()!!
                                        else
                                            listOfExpense[0] += user.incomeAcmount?.toInt()!! * -1
                                    }
                                    if (dateToBeChecked[2].toInt() > 7 && dateToBeChecked[2].toInt() <= 14) {
                                        if (user.incomeAcmount?.toInt()!! > 0)
                                            listOfIncome[1] += user.incomeAcmount?.toInt()!!
                                        else
                                            listOfExpense[1] += user.incomeAcmount?.toInt()!! * -1
                                    }
                                    if (dateToBeChecked[2].toInt() > 14 && dateToBeChecked[2].toInt() <= 21) {
                                        if (user.incomeAcmount?.toInt()!! > 0)
                                            listOfIncome[2] += user.incomeAcmount?.toInt()!!
                                        else
                                            listOfExpense[2] += user.incomeAcmount?.toInt()!! * -1
                                    }
                                    if (dateToBeChecked[2].toInt() > 21 && dateToBeChecked[2].toInt() <= 31) {
                                        if (user.incomeAcmount?.toInt()!! > 0)
                                            listOfIncome[3] += user.incomeAcmount?.toInt()!!
                                        else
                                            listOfExpense[3] += user.incomeAcmount?.toInt()!! * -1
                                    }
                                }
                            } else { // if user chose to populate by year then destribute the 12 month
                                if (dateToBeChecked[0] == date) {
                                    if (user.incomeAcmount?.toInt()!! > 0)
                                        listOfIncome[(dateToBeChecked[1].toInt()) - 1] += user.incomeAcmount?.toInt()!!
                                    else
                                        listOfExpense[(dateToBeChecked[1].toInt()) - 1] += (user.incomeAcmount?.toInt()!! * -1)
                                }
                            }
                        }
                    } // Calling functions for the graph creation
                    if (!typeOfGraph)
                        setGraphPerMonth(barChart, listOfIncome, listOfExpense)
                    else
                        setGraphPerYear(barChart, listOfIncome, listOfExpense)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun clearArray() {
        for (i in listOfExpenses.indices) {
            listOfExpenses[i] = 0
            listOfExpenses[i] = 0
        }
    }

    // when the graph fragment will be shown, the data will be set again by the user's choise
    override fun onResume() {
        resetChart()
        if (!typeOfGraph)
            setValues(sp.getString("Month", "11")!!, listOfExpenses, listOfIncomes)
        else
            setValues(sp.getString("Year", "2021")!!, listOfExpenses, listOfIncomes)
        super.onResume()
    }


    private fun resetChart() {
        barChart.fitScreen()
        barChart.data?.clearValues()
        barChart.xAxis.valueFormatter = null
        barChart.notifyDataSetChanged()
        barChart.clear()
        barChart.invalidate()
    }


}