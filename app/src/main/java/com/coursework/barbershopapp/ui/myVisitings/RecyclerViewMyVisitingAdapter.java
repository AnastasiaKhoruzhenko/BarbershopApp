package com.coursework.barbershopapp.ui.myVisitings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseAuth user;
    int title;

    public RecyclerViewMyVisitingAdapter(Context mContext, List<BookingInformation> bookingList, int title) {
        this.bookingList = bookingList;
        this.mContext = mContext;
        this.title = title;
        user = FirebaseAuth.getInstance();
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

        if(title == 1)
        {
            holder.barber_name.setText(bookingList.get(position).getBarberName()+ " " + bookingList.get(position).getBarberSurname());
            holder.service_name.setText(bookingList.get(position).getService());
            holder.time.setText(bookingList.get(position).getTime() + "  " + bookingList.get(position).getDate());
            holder.rating.setVisibility(View.INVISIBLE);
            holder.price.setText("RUB " + bookingList.get(position).getPrice().toString());

            holder.rate_me.setText("Оценивание доступно после посещения салона");

        }
        else if (title == 2){
            holder.barber_name.setText(bookingList.get(position).getBarberName()+ " " + bookingList.get(position).getBarberSurname());
            holder.service_name.setText(bookingList.get(position).getService());
            holder.time.setText(bookingList.get(position).getTime() + "  " + bookingList.get(position).getDate());
            holder.rating.setVisibility(View.VISIBLE);
            holder.price.setText("RUB " + bookingList.get(position).getPrice().toString());

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
                    holder.rating.setVisibility(View.INVISIBLE);
                    holder.rate_me.setText("Доступно для оценивания");
                    holder.rate_me.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(user.getCurrentUser() == null)
                                showDialogRegister();
                            else
                                showDialogForGetStars(holder, position);
                        }
                    });
                    break;
            }
        }
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

    private void showDialogForGetStars(ViewHolder holder, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_set_stars, viewGroup, false);

        EditText comment = dialogView.findViewById(R.id.et_comment);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar_master);

        builder.setView(dialogView);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String com = comment.getText().toString();
                Float rat = ratingBar.getRating();

                Map<String, Object> map = new HashMap<>();
                map.put("rating", String.valueOf(Math.round(rat)));
                map.put("comment", com);

                db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                        .collection(bookingList.get(position).getDateId())
                        .document(String.valueOf(bookingList.get(position).getSlot()))
                        .update(map);

                //   /Users/rfff@mail.ru/Visitings/1
                db.collection("Users").document("rfff@mail.ru")
                        .collection("Visitings")
                        .document(String.valueOf(position)).update(map);

                holder.rating.setVisibility(View.VISIBLE);
                holder.rate_me.setText("Оценено");
                holder.rating.setRating(Math.round(rat));
                holder.rating.setIsIndicator(true);
            }
        });
        builder.setNegativeButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void showDialogRegister() {

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        //alertDialog.setTitle("Alert");
        alertDialog.setMessage("Чтобы оставлять комментарии, вам необходимо зарегистрироваться");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Не сейчас",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Зарегистрироваться",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }
}
