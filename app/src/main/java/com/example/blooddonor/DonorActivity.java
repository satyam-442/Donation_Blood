package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorActivity extends AppCompatActivity
{

    MaterialSearchView searchView;
    FirebaseAuth mAuth;
    DatabaseReference DonorsRef;
    String currentUserId;
    RecyclerView donorList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        DonorsRef = FirebaseDatabase.getInstance().getReference().child("Donors");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donor");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        //RECYCLER VIEW FOR QUERY UPDATE(OPEN)

        donorList = (RecyclerView) findViewById(R.id.all_donor_list);
        donorList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        donorList.setLayoutManager(linearLayoutManager);


        //RECYCLER VIEW FOR DONORS UPDATE(CLOSE)
        startListen();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListen();
    }

    //THIS IS FOR THE QUERY PAGE
    private void startListen()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Donors").limitToLast(50);
        FirebaseRecyclerOptions<Donor> options = new FirebaseRecyclerOptions.Builder<Donor>().setQuery(query, Donor.class).build();
        FirebaseRecyclerAdapter<Donor, DonorsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donor, DonorsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull DonorsViewHolder holder, final int position, @NonNull Donor model)
            {
                //final String PostKey = getRef(position).getKey();

                holder.setAddres(model.getAddres());
                holder.setNamee(model.getNamee());
                holder.setGenderr(model.getGenderr());
                holder.setGroupp(model.getGroupp());
                holder.setDobb(model.getDobb());
                holder.setImagee(getApplicationContext(),model.getImagee());
                holder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();
                        Intent teacherProfileIntent = new Intent(DonorActivity.this,DonorsProfile.class);
                        teacherProfileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(teacherProfileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public DonorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_donor_layout,parent,false);
                return new DonorsViewHolder(view);
            }
        };
        donorList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*And this is the static class*/
    public static class DonorsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public DonorsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setNamee(String name)
        {
            TextView username = (TextView) mView.findViewById(R.id.donor_username);
            username.setText(name);
        }

        public void setImagee(Context ctx, String image)
        {
            CircleImageView donorimage = (CircleImageView) mView.findViewById(R.id.donor_profile_image);
            Picasso.with(ctx).load(image).into(donorimage);
        }

        public void setDobb(String DOB)
        {
            TextView dob = (TextView) mView.findViewById(R.id.donor_dob);
            dob.setText(DOB);
        }

        public void setGroupp(String group)
        {
            TextView bldgroup = (TextView) mView.findViewById(R.id.blood_group_tv);
            bldgroup.setText(group);
        }

        public void setAddres(String address)
        {
            TextView addresdoonor = (TextView) mView.findViewById(R.id.donor_address);
            addresdoonor.setText(address);
        }

        public void setGenderr(String gender)
        {
            TextView donorgender = (TextView) mView.findViewById(R.id.donor_gender);
            donorgender.setText(gender);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}
