package com.coursework.barbershopapp.User.ui.signup;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewBestMastersAdapter;
import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewCommentAdapter;
import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMastersChooseAdapter extends RecyclerView.Adapter<RecyclerViewMastersChooseAdapter.MyViewHolder>{

    // /ServicesMan/HairCut/Barbers/a@a.ri
    List<String> listNameSurname = new ArrayList<>();
    List<String> listOffers = new ArrayList<>();
    List<String> listScore = new ArrayList<>();
    List<Master> personList = new ArrayList<>();
    List<CardView> cardViews;
    List<ConstraintLayout> lays;
    LocalBroadcastManager localBroadcastManager;
    boolean flag;

    Dialog dialog;
    ConstraintLayout constraintLayout;
    FirebaseFirestore db;

    Context mContext;

    public RecyclerViewMastersChooseAdapter(Context mContext, List<Master> personList, boolean flag)
    {
        this.mContext = mContext;
        this.personList = personList;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        this.flag = flag;
        db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.cardview_masters, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.score.setText(personList.get(position).getScore());
        holder.name_surname.setText(personList.get(position).getName() + " " + personList.get(position).getSurname());
        holder.offer.setText(personList.get(position).getPhone());

        if(flag)
        {
            if(!cardViews.contains(holder.card)) {
                lays.add(holder.lay);
                cardViews.add(holder.card);
            }

            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void OnItemSelectedListener(View view, int position) {
                    for(CardView card:cardViews)
                        card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));

                    for(ConstraintLayout card:lays)
                        card.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));

                    holder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));
                    holder.lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));


                    Intent intent = new Intent(Common.KEY_NEXT_BTN);
                    intent.putExtra(Common.KEY_BARBER_SELECTED, personList.get(position));
                    intent.putExtra(Common.KEY_STEP, 3);
                    localBroadcastManager.sendBroadcast(intent);
                }
            });

            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullDialog(v, position);
                }
            });
        }
        else{
            holder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void OnItemSelectedListener(View view, int position) {
                    showFullDialog(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card;
        TextView name_surname, offer, score;
        ImageView img, info;
        ConstraintLayout lay;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_master_foruser);
            name_surname = itemView.findViewById(R.id.textV_name_sur);
            offer = itemView.findViewById(R.id.text_offers);
            score = itemView.findViewById(R.id.text_score);
            img = itemView.findViewById(R.id.image_photo);
            lay = itemView.findViewById(R.id.rel);
            info = itemView.findViewById(R.id.image_choose);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }

//    private void getCommentCount(View view, int position){
//        db.collection("MastersDates").document(personList.get(position).getEmail())
//                .collection("Dates").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful())
//                        {
//                            List<String> dates = new ArrayList<>();
//                            for(DocumentSnapshot doc : task.getResult())
//                            {
//                               dates.add(doc.getId());
//                            }
//
//                            showFullDialog(view, position, dates);
//                        }
//                    }
//                });
//    }

    private void showFullDialog(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_comments_dialog, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        RecyclerView recyclerView = dialog.findViewById(R.id.recview_comments);

            db.collection("Masters").document(personList.get(position).getEmail())
                    .collection("16_03_2020").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<BookingInformation> listQ = new ArrayList<>();
                                for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                    BookingInformation bookInfo = querySnapshot.toObject(BookingInformation.class);
                                    if (!querySnapshot.getId().contains(".") && !bookInfo.getRating().equals("-1"))
                                        listQ.add(bookInfo);
                                }

                                initRecViewComment(listQ, recyclerView);
                            }
                        }
                    });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        constraintLayout = view.findViewById(R.id.constr_master_comm);
        dialog.show();
    }

    private void initRecViewComment(List<BookingInformation> list, RecyclerView recyclerView) {
        RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
    }
}
