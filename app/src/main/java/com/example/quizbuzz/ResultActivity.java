package com.example.quizbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    TextView correctText, wrongText, totalText;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        totalText = findViewById(R.id.textTotal);
        correctText = findViewById(R.id.textCorrect);
        wrongText = findViewById(R.id.textWrong);
        pieChart = findViewById(R.id.pie_chart);

        float total = Integer.parseInt(getIntent().getStringExtra("total"));
        float correct = Integer.parseInt(getIntent().getStringExtra("correct"));
        float incorrect = Integer.parseInt(getIntent().getStringExtra("incorrect"));
        float unattempted = total - (correct+incorrect);

        float[] numData = {correct, incorrect, unattempted};
        String[] dataLabel = {"Correct", "Incorrect", "Unattempted"};
        Integer[] labelColors = {Color.GREEN, Color.RED, Color.YELLOW};

        float totalQuestions = 0;
        for(int i=0;i<numData.length;i++){
            totalQuestions += numData[i];
        }

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0;i<dataLabel.length;i++) {
            float dataPercentage = numData[i] * 100 / totalQuestions;
            PieEntry pieEntry = new PieEntry(dataPercentage, dataLabel[i]);
            pieEntries.add(pieEntry);
            colors.add(labelColors[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Score");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setDataSet(pieDataSet);
        pieChart.getDescription().setEnabled(false);
        pieChart.setData(pieData);
        pieChart.invalidate();

        totalText.setText("Total Attempted: "+total);
        correctText.setText("Correct: "+correct);
        wrongText.setText("Incorrect: "+incorrect);
    }
}