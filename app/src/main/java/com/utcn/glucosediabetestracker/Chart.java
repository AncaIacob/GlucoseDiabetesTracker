package com.utcn.glucosediabetestracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Chart extends Fragment {
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userID;
    private PieChart chart;
    final String[] parties = new String[]{"Before Activity", "After Activity", "Before Treatment", "After Treatment"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        View v = inflater.inflate(R.layout.chart, container, false);
        chart = v.findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterText("Weekly Glucose Analysis");
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);
        // add a selection listener
        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setTextColor(Color.BLACK);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        CollectionReference query = fStore.collection("users").document(userID).collection("glucose_values");
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<DisplayData> userdata = new ArrayList<>();
                        ArrayList<DisplayData> weekdata = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                                DisplayData displayData = journals.toObject(DisplayData.class);
                                userdata.add(displayData);
                            }
                            if (userdata.size() > 0) {
                                ArrayList<String> dateStringArray = new ArrayList<String>();
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                                DateFormat df = new SimpleDateFormat("MMM dd yyyy");
                                for (int i = 0; i < 7; i++) {
                                    dateStringArray.add(df.format(c.getTime()));
                                    c.add(Calendar.DATE, 1);
                                }
                                for (int i = 0; i < userdata.size(); i++) {
                                    if (dateStringArray.get(0).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(1).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(2).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(3).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(4).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(5).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                    if (dateStringArray.get(6).equalsIgnoreCase(userdata.get(i).getValue_data())) {
                                        weekdata.add(userdata.get(i));
                                    }
                                }
                                if (weekdata.size() > 0) {
                                    generateChart(weekdata);
                                }
                            }
                            chart.setVisibility(View.VISIBLE);
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

    void generateChart(ArrayList<DisplayData> weekdata) {
        int beforeactivitycount = 0, afteractivitycount = 0, beforetratamentcount = 0, aftertratamentcount = 0;
        float beforeactivitytotal = 0.0f, afteractivitytotal = 0.0f, beforetratamenttotal = 0.0f, aftertratamenttotal = 0.0f;
        float beforeactivityval = 0.0f, afteractivityval = 0.0f, beforetratamentval = 0.0f, aftertratamentval = 0.0f;
        for (int i = 0; i < weekdata.size(); i++) {
            DisplayData displayData = weekdata.get(i);
            if (displayData.getValue_type().equalsIgnoreCase("Out of Bed")
                    || displayData.getValue_type().equalsIgnoreCase("Before Breakfast")
                    || displayData.getValue_type().equalsIgnoreCase("Before Lunch")
                    || displayData.getValue_type().equalsIgnoreCase("Before dinner")) {
                beforeactivitycount++;
                beforeactivitytotal = beforeactivitytotal + Float.parseFloat(displayData.getValue_glucose());
            }
            if (displayData.getValue_type().equalsIgnoreCase("Before Bed")
                    || displayData.getValue_type().equalsIgnoreCase("After Breakfast")
                    || displayData.getValue_type().equalsIgnoreCase("After Lunch")
                    || displayData.getValue_type().equalsIgnoreCase("After dinner")) {
                afteractivitycount++;
                afteractivitytotal = afteractivitytotal + Float.parseFloat(displayData.getValue_glucose());
            }
            if (displayData.getValue_type().equalsIgnoreCase("Before tratament")) {
                beforetratamentcount++;
                beforetratamenttotal = beforetratamenttotal + Float.parseFloat(displayData.getValue_glucose());
            }
            if (displayData.getValue_type().equalsIgnoreCase("After tratament")) {
                aftertratamentcount++;
                aftertratamenttotal = aftertratamenttotal + Float.parseFloat(displayData.getValue_glucose());
            }
        }
        beforeactivityval = beforeactivitytotal / beforeactivitycount;
        afteractivityval = afteractivitytotal / afteractivitycount;
        beforetratamentval = beforetratamenttotal / beforetratamentcount;
        aftertratamentval = aftertratamenttotal / aftertratamentcount;
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(beforeactivityval, parties[0], getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry(afteractivityval, parties[1], getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry(beforetratamentval, parties[2], getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry(aftertratamentval, parties[3], getResources().getDrawable(R.drawable.star)));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.YELLOW);
        colors.add(Color.parseColor("#FFA500"));
        colors.add(Color.CYAN);
        colors.add(Color.GREEN);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.RED);
        chart.setNoDataTextColor(Color.RED);
        chart.setData(data);
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
        ((PieChartRenderer) chart.getRenderer()).getPaintEntryLabels().setColor(Color.RED);
        ((PieChartRenderer) chart.getRenderer()).getPaintEntryLabels().setTextSize(35f);
    }
}