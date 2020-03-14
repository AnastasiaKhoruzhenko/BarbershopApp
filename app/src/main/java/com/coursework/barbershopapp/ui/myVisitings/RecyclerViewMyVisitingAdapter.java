package com.coursework.barbershopapp.ui.myVisitings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coursework.barbershopapp.Masters.ui.settings.RecyclerViewAdapterMasterSett;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMyVisitingAdapter extends RecyclerView.Adapter<RecyclerViewMyVisitingAdapter.ViewHolder> {

    List<BookingInformation> bookingList;
    private Context mContext;
    FirebaseFirestore db;

    public RecyclerViewMyVisitingAdapter(Context mContext, List<BookingInformation> bookingList) {
        this.bookingList = bookingList;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_visiting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.barber_name.setText(bookingList.get(position).getBarberName()+ " " + bookingList.get(position).getBarberSurname());
        holder.service_name.setText(bookingList.get(position).getService());
        holder.price.setText("RUB" + bookingList.get(position).getPrice().toString());
        switch (Integer.valueOf(bookingList.get(position).getRating()))
        {
            case 0:
                holder.rating.setRating(0);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            case 1:
                holder.rating.setRating(1);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            case 2:
                holder.rating.setRating(2);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            case 3:
                holder.rating.setRating(3);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            case 4:
                holder.rating.setRating(4);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            case 5:
                holder.rating.setRating(5);
                holder.rate_me.setText("Оценено");
                holder.rating.setIsIndicator(true);
                break;
            default:
                holder.rating.setRating(0);
                holder.rate_me.setText("Оценените качество услуги");
                break;
        }


        holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Map<String, Object> map = new HashMap<>();
                map.put("rating", String.valueOf(Math.round(rating)));
                db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                        .collection(bookingList.get(position).getDateId())
                        .document(String.valueOf(bookingList.get(position).getSlot()))
                        .update(map);

                //   /Users/rfff@mail.ru/Visitings/1
                db.collection("Users").document("rfff@mail.ru")
                        .collection("Visitings")
                        .document(String.valueOf(position)).update(map);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView barber_name, time, price, service_name, rate_me;
        RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_visiting);
            barber_name = itemView.findViewById(R.id.tv_barber_vis_my);
            time = itemView.findViewById(R.id.tv_date_vis_my);
            price = itemView.findViewById(R.id.tv_price_visiting);
            service_name = itemView.findViewById(R.id.tv_service_name_vis);
            rate_me = itemView.findViewById(R.id.tv_rate_me);
            rating = itemView.findViewById(R.id.ratingBar);
        }
    }
}
