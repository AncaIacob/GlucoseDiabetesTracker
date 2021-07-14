package com.utcn.glucosediabetestracker;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ForwardingListeningExecutorService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import maes.tech.intentanim.CustomIntent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Glucose extends Fragment  {

    Button button_values;
    RecyclerView values_display;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userID;
    FirestoreRecyclerAdapter<DisplayData, DisplayDataHolder> displayDataAdapter;
    //slide menu
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView mynavigationView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //display data
        fStore = FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        Query query = fStore.collection("users").document(userID).collection("glucose_values").orderBy("value_data", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<DisplayData> allDisplayData = new FirestoreRecyclerOptions.Builder<DisplayData>()
                .setQuery(query, DisplayData.class)
                .build();

        displayDataAdapter = new FirestoreRecyclerAdapter<DisplayData, DisplayDataHolder>(allDisplayData) {
            @Override
            protected void onBindViewHolder(@NonNull DisplayDataHolder displayDataHolder, int i, @NonNull DisplayData displayData) {
                displayDataHolder.display_glucose_value.setText(displayData.getValue_glucose());
                displayDataHolder.display_date_value.setText(displayData.getValue_data());
                displayDataHolder.display_time_value.setText(displayData.getValue_time());
                displayDataHolder.display_type_value.setText(displayData.getValue_type());

                //////////////////




            }


            @NonNull
            @Override
            public DisplayDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_recyclerview,parent,false);
                return new DisplayDataHolder(view);
            }
        };
        //add new values
        View v =  inflater.inflate(R.layout.glucose,container,false);
        //recycle view
        values_display = v.findViewById(R.id.recycle_view);
        values_display.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        values_display.setAdapter(displayDataAdapter);




        button_values = v.findViewById(R.id.button_values);
        button_values.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),Values.class);
                startActivity(intent1);

                CustomIntent.customType(getActivity(),"left-to-right");
            }
        });
        // end /add new values

        //slide menu
        mDrawerLayout = v.findViewById(R.id.drawerLayout1);
        mToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


          mynavigationView = v.findViewById(R.id.navView);
          mynavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  item.setChecked(true);
                  int menu_id = item.getItemId();
                  switch (menu_id) {
                      case R.id.Item1:
                          Intent intent2 = new Intent(getActivity(), ChangePassword.class);
                          startActivity(intent2);

                          CustomIntent.customType(getActivity(), "left-to-right");
                          break;
                      case R.id.Item2:
                          FirebaseAuth.getInstance().signOut();
                          Intent intent3 = new Intent(getActivity(), RegisterAndLogin.class);
                          startActivity(intent3);
                          CustomIntent.customType(getActivity(), "left-to-right");
                          getActivity().finish();
                          break;

                  }
                  return true;
              }
          });




        return v;
        // end /add new values

        

    }
    //display data

    public class DisplayDataHolder extends RecyclerView.ViewHolder {
        TextView display_glucose_value, display_date_value, display_time_value, display_type_value;
        View view;
        CardView mCardView;


        public DisplayDataHolder(@NonNull View itemView) {
            super(itemView);
            display_glucose_value = itemView.findViewById(R.id.glucose_value_display);
            display_date_value = itemView.findViewById(R.id.date_value_display);
            display_time_value = itemView.findViewById(R.id.time_value_display);
            display_type_value = itemView.findViewById(R.id.type_value_display);
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

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);
        int menu_id = item.getItemId();
        switch (menu_id)
        {
            case R.id.Item1:
                Intent intent2 = new Intent(getActivity(),ChangePassword.class);
                startActivity(intent2);

                CustomIntent.customType(getActivity(),"left-to-right");
                break;
            case R.id.Item2:
                Intent intent3 = new Intent(getActivity(),ChangePassword.class);
                startActivity(intent3);

                CustomIntent.customType(getActivity(),"left-to-right");
                break;
        }

        return super.onOptionsItemSelected(item);
    } */

}
