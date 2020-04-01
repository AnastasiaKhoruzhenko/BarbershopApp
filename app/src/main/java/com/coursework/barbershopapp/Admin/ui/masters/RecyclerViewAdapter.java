package com.coursework.barbershopapp.Admin.ui.masters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mScore = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImages, ArrayList<String> mScore) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mScore = mScore;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_masters_admin, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.photo);

        holder.nameSurname.setText(mImageNames.get(position));
        //holder.score.setText(mScore.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView photo;
        TextView nameSurname, score;
        ConstraintLayout relativeLayout;
        ImageView starImg;
        CardView cardView;
        LinearLayout lin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lin = itemView.findViewById(R.id.lay_master_admin_card);
            photo = itemView.findViewById(R.id.masterPhoto);
            nameSurname = itemView.findViewById(R.id.masterNameSurname);
            //relativeLayout = itemView.findViewById(R.id.rellay_masters_admin);
            score = itemView.findViewById(R.id.tv_score_master_admin);
            starImg = itemView.findViewById(R.id.iv_star_admin);
            cardView = itemView.findViewById(R.id.cardview_master_admin_for_list);
        }
    }
}
