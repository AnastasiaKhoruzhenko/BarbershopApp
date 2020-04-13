package com.coursework.barbershopapp.User.ui.signup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewBestMastersAdapter;
import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewCommentAdapter;
import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Comment;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewMastersChooseAdapter extends RecyclerView.Adapter<RecyclerViewMastersChooseAdapter.MyViewHolder>{

    private List<Master> personList = new ArrayList<>();
    private List<CardView> cardViews;
    private List<ConstraintLayout> lays;
    private List<RadioButton> rButtons;
    private LocalBroadcastManager localBroadcastManager;
    private boolean flag;
    private Dialog dialog;
    private FirebaseFirestore db;
    private Context mContext;

    public RecyclerViewMastersChooseAdapter(Context mContext, List<Master> personList, boolean flag)
    {
        this.mContext = mContext;
        this.personList = personList;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        this.flag = flag;
        rButtons = new ArrayList<>();
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

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        if(language.equals("ru"))
            holder.name_surname.setText(personList.get(position).getName() + " " + personList.get(position).getSurname());
        else
            holder.name_surname.setText(translitClass.toTranslit(personList.get(position).getName()) + " " + translitClass.toTranslit(personList.get(position).getSurname()));

        holder.score.setText(personList.get(position).getScore());
        holder.offer.setText(personList.get(position).getPhone());
        holder.info.setClickable(true);
        holder.radioButton.setChecked(false);

        List<String> services = personList.get(position).getServices();
        if(services!=null)
        {
            String offerStr = "";

            if(language.equals("ru"))
            {
                for (String serv : services){
                    switch (serv)
                    {
                        case "HairCut":
                            offerStr += "стрижка, ";break;
                        case "BarberSPA":
                            offerStr += "SPA-процедуры, ";break;
                        case "BeardAndMustacheCut":
                            offerStr += "оформление бороды и усов, ";break;
                        case "Coloring":
                            offerStr += "покраска волос, ";break;
                        case "CombineService":
                            offerStr += "комбинированные услуги, ";break;
                        case "Tatoo":
                            offerStr += "нанесение татуировок, ";break;
                    }
                }
            }
            else
            {
                for (String serv : services){
                    switch (serv)
                    {
                        case "HairCut":
                            offerStr += "haircut, ";break;
                        case "BarberSPA":
                            offerStr += "SPA, ";break;
                        case "BeardAndMustacheCut":
                            offerStr += "beard and mustache cut, ";break;
                        case "Coloring":
                            offerStr += "coloring, ";break;
                        case "CombineService":
                            offerStr += "combine services, ";break;
                        case "Tatoo":
                            offerStr += "tattoo, ";break;
                    }
                }
            }

            offerStr = offerStr.substring(0, offerStr.length()-2);
            holder.offer.setText(offerStr);
        }

        db.collection("Comments").document(personList.get(position).getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int count  = Integer.valueOf(task.getResult().getString("count"));
                if(count == 0)
                    holder.info.setClickable(false);
                switch (count%10)
                {
                    case 1:
                        holder.info.setText(count + " "+ mContext.getResources().getString(R.string.comment1));
                        break;
                    case 2:
                    case 3:
                    case 4:
                        holder.info.setText(count + " "+ mContext.getResources().getString(R.string.comment234));
                        break;
                    default:
                        holder.info.setText(count + " "+ mContext.getResources().getString(R.string.commentDef));
                        break;
                }
            }
        });

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+personList.get(position).getEmail());
        phRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //img.setImageURI(uri);

                Glide.with(mContext)
                        .load(uri)
                        .into(holder.img);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        if(flag)
        {
            if(!cardViews.contains(holder.card)) {
                lays.add(holder.lay);
                cardViews.add(holder.card);
                rButtons.add(holder.radioButton);
            }

            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(RadioButton rButt : rButtons)
                        rButt.setChecked(false);

                    holder.radioButton.setChecked(true);

                    Intent intent = new Intent(Common.KEY_NEXT_BTN);
                    intent.putExtra(Common.KEY_BARBER_SELECTED, personList.get(position));
                    intent.putExtra(Common.KEY_STEP, 3);
                    localBroadcastManager.sendBroadcast(intent);
                }
            });

            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
                @Override
                public void OnItemSelectedListener(View view, int position) {
                    for(RadioButton rButt : rButtons)
                        rButt.setChecked(false);

                    holder.radioButton.setChecked(true);

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
            holder.radioButton.setChecked(false);

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
        TextView info;
        CircleImageView img;
        ConstraintLayout lay;
        RadioButton radioButton;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_master_foruser);
            name_surname = itemView.findViewById(R.id.textV_name_sur);
            offer = itemView.findViewById(R.id.text_offers);
            score = itemView.findViewById(R.id.text_score);
            img = itemView.findViewById(R.id.image_photo);
            lay = itemView.findViewById(R.id.rel);
            info = itemView.findViewById(R.id.image_choose);
            radioButton = itemView.findViewById(R.id.radioButton2);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }

    private void showFullDialog(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_comments_dialog, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        RecyclerView recyclerView = dialog.findViewById(R.id.recview_comments);

            db.collection("Comments").document(personList.get(position).getEmail())
                    .collection("Comments").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Comment> listQ = new ArrayList<>();
                                for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                    Comment comment = querySnapshot.toObject(Comment.class);
                                    listQ.add(comment);
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
        ConstraintLayout constraintLayout = view.findViewById(R.id.constr_master_comm);
        dialog.show();
    }

    private void initRecViewComment(List<Comment> list, RecyclerView recyclerView) {
        RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(mContext, list);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
    }
}
