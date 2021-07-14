package com.utcn.glucosediabetestracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Insights extends Fragment {
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userID;
    ArrayList<DisplayData> chartdata = new ArrayList<>();
    private ScatterChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        View v = inflater.inflate(R.layout.insights, container, false);
        chart = v.findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(true);
        chart.setTouchEnabled(true);
        chart.setMaxHighlightDistance(50f);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setMaxVisibleValueCount(200);
        chart.setPinchZoom(true);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXOffset(0f);
        YAxis yl = chart.getAxisLeft();
        yl.setAxisMinimum(5f);
        chart.getAxisRight().setEnabled(false);
        XAxis xl = chart.getXAxis();
        xl.setDrawGridLines(true);
        CollectionReference query = fStore.collection("users").document(userID).collection("glucose_values");
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<DisplayData> userdata = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                                DisplayData displayData = journals.toObject(DisplayData.class);
                                userdata.add(displayData);
                            }
                            if (userdata.size() > 0) {
                                String todaydate = getTodaysDate();
                                for (int i = 0; i < userdata.size(); i++) {
                                    if (todaydate.equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        chartdata.add(userdata.get(i));
                                    }
                                }
                                if (chartdata.size() > 0) {
                                    createchart();
                                }
                                chart.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        chart.setVisibility(View.VISIBLE);
                    }
                });
        return v;
    }

    void createchart() {
        ArrayList<Entry> beforeactivitylist = new ArrayList<>();
        ArrayList<Entry> afteractivitylist = new ArrayList<>();
        ArrayList<Entry> beforetratamentlist = new ArrayList<>();
        ArrayList<Entry> aftertratamentlist = new ArrayList<>();

        for (int i = 0; i < chartdata.size(); i++) {
            DisplayData displayData = chartdata.get(i);
            if (displayData.getValue_type().equalsIgnoreCase("Out of Bed")
                    || displayData.getValue_type().equalsIgnoreCase("Before Breakfast")
                    || displayData.getValue_type().equalsIgnoreCase("Before Lunch")
                    || displayData.getValue_type().equalsIgnoreCase("Before dinner")) {
                float yval = Float.parseFloat(displayData.getValue_glucose());
                String timearr[] = displayData.getValue_time().split(":");
                float xval = Float.parseFloat(timearr[0]);
                beforeactivitylist.add(new Entry(xval, yval));
            }
            if (displayData.getValue_type().equalsIgnoreCase("Before Bed")
                    || displayData.getValue_type().equalsIgnoreCase("After Breakfast")
                    || displayData.getValue_type().equalsIgnoreCase("After Lunch")
                    || displayData.getValue_type().equalsIgnoreCase("After dinner")) {
                float yval = Float.parseFloat(displayData.getValue_glucose());
                String timearr[] = displayData.getValue_time().split(":");
                float xval = Float.parseFloat(timearr[0]);
                afteractivitylist.add(new Entry(xval, yval));
            }
            if (displayData.getValue_type().equalsIgnoreCase("Before tratament")) {
                float yval = Float.parseFloat(displayData.getValue_glucose());
                String timearr[] = displayData.getValue_time().split(":");
                float xval = Float.parseFloat(timearr[0]);
                beforetratamentlist.add(new Entry(xval, yval));
            }
            if (displayData.getValue_type().equalsIgnoreCase("After tratament")) {
                float yval = Float.parseFloat(displayData.getValue_glucose());
                String timearr[] = displayData.getValue_time().split(":");
                float xval = Float.parseFloat(timearr[0]);
                aftertratamentlist.add(new Entry(xval, yval));
            }
        }

        ScatterDataSet set1 = new ScatterDataSet(beforeactivitylist, "Before Activity");
        set1.setScatterShape(ScatterChart.ScatterShape.CROSS);
        set1.setColor(Color.parseColor("#320A28"));

        ScatterDataSet set2 = new ScatterDataSet(afteractivitylist, "After Activity");
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set2.setColor(Color.parseColor("#38b3c7"));

        ScatterDataSet set3 = new ScatterDataSet(beforetratamentlist, "Before Treatment");
        set3.setScatterShape(ScatterChart.ScatterShape.SQUARE);
        set3.setColor(Color.parseColor("#CA054D"));

        ScatterDataSet set4 = new ScatterDataSet(aftertratamentlist, "After Treatment");
        set4.setScatterShape(ScatterChart.ScatterShape.TRIANGLE);
        set4.setColor(Color.parseColor("#008000"));

        set1.setScatterShapeSize(30f);
        set2.setScatterShapeSize(30f);
        set3.setScatterShapeSize(30f);
        set4.setScatterShapeSize(30f);
        set1.setValueTextSize(10f);
        set2.setValueTextSize(10f);
        set3.setValueTextSize(10f);
        set4.setValueTextSize(10f);
        set1.setValueTextColor(Color.parseColor("#320A28"));
        set2.setValueTextColor(Color.parseColor("#38b3c7"));
        set3.setValueTextColor(Color.parseColor("#CA054D"));
        set4.setValueTextColor(Color.parseColor("#008000"));
        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        ScatterData data = new ScatterData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }

    String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month = month + 1;
        return makeDateString(dayOfMonth, month, year);
    }

    String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + String.format("%02d", dayOfMonth) + " " + year;
    }

    String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        return "JAN";
    }
}