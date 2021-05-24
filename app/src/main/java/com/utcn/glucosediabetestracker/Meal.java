package com.utcn.glucosediabetestracker;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import maes.tech.intentanim.CustomIntent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Meal extends Fragment {
    Button button_values_food;
    RecyclerView values_display_food;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userID;
    FirestoreRecyclerAdapter<DisplayDataFood, Meal.DisplayDataHolder> displayDataAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //display data
        fStore = FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        Query query = fStore.collection("users").document(userID).collection("meal_values");
        FirestoreRecyclerOptions<DisplayDataFood> allDisplayData = new FirestoreRecyclerOptions.Builder<DisplayDataFood>()
                .setQuery(query, DisplayDataFood.class)
                .build();

        displayDataAdapter = new FirestoreRecyclerAdapter<DisplayDataFood, Meal.DisplayDataHolder>(allDisplayData) {
            @Override
            protected void onBindViewHolder(@NonNull Meal.DisplayDataHolder displayDataHolder, int i, @NonNull DisplayDataFood displayData) {
                displayDataHolder.display_glucose_value_before.setText(displayData.getValue_glucose_before());
                displayDataHolder.display_glucose_value_after.setText(displayData.getValue_glucose_after());
                displayDataHolder.display_food_value.setText(displayData.getValue_food());




            }


            @NonNull
            @Override
            public Meal.DisplayDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_recyclerview_food,parent,false);
                return new Meal.DisplayDataHolder(view);
            }
        };
        //add new values
        View v =  inflater.inflate(R.layout.meal,container,false);
        //recycle view
        values_display_food= v.findViewById(R.id.recycle_view_food);
        values_display_food.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        values_display_food.setAdapter(displayDataAdapter);




        button_values_food = v.findViewById(R.id.button_values);
        button_values_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),FoodValues.class);
                startActivity(intent1);


                CustomIntent.customType(getActivity(),"left-to-right");
            }
        });


        return v;
        // end /add new values



    }
    //display data

    public class DisplayDataHolder extends RecyclerView.ViewHolder {
        TextView display_glucose_value_before, display_glucose_value_after, display_food_value;
        View view;
        CardView mCardView;


        public DisplayDataHolder(@NonNull View itemView) {
            super(itemView);
            display_glucose_value_before = itemView.findViewById(R.id.glucose_value_before_display);
            display_glucose_value_after = itemView.findViewById(R.id.glucose_value_after_display);
            display_food_value = itemView.findViewById(R.id.food_value_display);
            view = itemView;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        displayDataAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (displayDataAdapter != null){
            displayDataAdapter.stopListening();
        }
    }
}
