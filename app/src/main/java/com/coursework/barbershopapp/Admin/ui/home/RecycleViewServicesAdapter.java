package com.coursework.barbershopapp.Admin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.Common;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewServicesAdapter extends RecyclerView.Adapter<RecycleViewServicesAdapter.ViewHolder> {

    private List<Banner> listService;
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private List<CardView> listCard;
    private List<ConstraintLayout> lays;

    public RecycleViewServicesAdapter(Context mContext, List<Banner> listService) {
        this.listService = listService;
        this.mContext = mContext;
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        listCard = new ArrayList<>();
        lays = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(mContext)
//                .asBitmap()
//                .load(listService.get(position).getImg())
//                .into(holder.img);
        holder.text.setText(listService.get(position).getText());

        holder.step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                switch (position)
                {
                    case 0:
                         str = "BarberSPA";
                        break;
                    case 1:
                         str = "BeardAndMustacheCut";
                        break;
                    case 2:
                         str = "Coloring";
                        break;
                    case 3:
                         str = "CombineService";
                        break;
                    case 4:
                         str = "HairCut";
                        break;
                    case 5:
                         str = "Tatoo";
                        break;
                }

                Intent intent = new Intent(mContext, EditServicesActivity.class);
                intent.putExtra("service", str);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        CardView step1;
        TextView text;
        ImageView img;
        ConstraintLayout lay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step1 = itemView.findViewById(R.id.cardview_service_admin);
            text = itemView.findViewById(R.id.tv_name_admin);
            lay = itemView.findViewById(R.id.const_lay_admin);

//            step1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    OnCardViewClicked();
//                }
//            });
        }
    }
}
