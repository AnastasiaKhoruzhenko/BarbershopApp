package com.coursework.barbershopapp.Admin.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewBestMastersAdapter extends RecyclerView.Adapter<RecyclerViewBestMastersAdapter.ViewHolder>{

    List<Master> list;
    Context mContext;
    Dialog dialog;
    FirebaseFirestore db;
    ConstraintLayout constraintLayout;

    public RecyclerViewBestMastersAdapter(List<Master> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
    }

    public RecyclerViewBestMastersAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_best_master, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.score.setText(list.get(position).getScore());
        holder.name_surname.setText(list.get(position).getName()+" "+list.get(position).getSurname());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullDialog(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name_surname, score;
        CircleImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_bestmaster);
            name_surname = itemView.findViewById(R.id.tv_namesurname_home_a);
            score = itemView.findViewById(R.id.tv_score_home_adm);
            photo = itemView.findViewById(R.id.master_photo_home_adm);
        }
    }

    private void showFullDialog(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_comments_dialog, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        RecyclerView recyclerView = dialog.findViewById(R.id.recview_comments);

        db.collection("Masters").document(list.get(position).getEmail())
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
