package com.coursework.barbershopapp.User.ui.myVisitings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewMyVisitingAdapter extends RecyclerView.Adapter<RecyclerViewMyVisitingAdapter.ViewHolder> {

    private List<BookingInformation> bookingList;
    private Context mContext;
    private FirebaseFirestore db;
    private FirebaseAuth user;
    private int title;

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

    public void deleteItem(int position)
    {
        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
        BookingInformation bookCopy = bookingList.get(position);
        bookingList.remove(bookingList.get(position));

        db.collection("Masters").document(bookCopy.getBarberEmail())
                .collection(bookCopy.getDateId())
                .whereEqualTo("id", String.valueOf(bookCopy.getId()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot doc : task.getResult())
                                doc.getReference().delete();
                        }
                    }
                });

        db.collection("Users").document(bookCopy.getCustomerEmail())
                .collection("Visitings")
                .whereEqualTo("id", bookCopy.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc : task.getResult())
                        doc.getReference().delete();
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                checkThisDateExists(position, bookCopy);
            }
        }, 1000);
    }

    public void checkThisDateExists(int position, BookingInformation copy) {
        db.collection("Masters").document(copy.getBarberEmail())
                .collection(copy.getDateId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            int time = Integer.valueOf(copy.getTimeService());
                            int countSl = Integer.valueOf(String.valueOf(Math.round(Math.ceil(time/20.0))));
                            int count = 0;
                            for(QueryDocumentSnapshot doc : task.getResult())
                                count++;
                            //Toast.makeText(mContext, String.valueOf(count) + " " + String.valueOf(countSl), Toast.LENGTH_SHORT).show();
                            if(count == 0)
                            {
                                db.collection("Masters").document(copy.getBarberEmail())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            Master master = task.getResult().toObject(Master.class);
                                            master.getDates().remove(String.valueOf(copy.getDateId()));
                                            //Toast.makeText(mContext, String.valueOf(master.getDates().size()), Toast.LENGTH_SHORT).show();
                                            db.collection("Masters")
                                                    .document(copy.getBarberEmail())
                                                    .set(master);
                                        }
                                    }
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(mContext, "Visiting not exists", Toast.LENGTH_SHORT).show();
                        db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    Master master = task.getResult().toObject(Master.class);
                                    master.getDates().remove(String.valueOf(bookingList.get(position).getDateId()));
                                    db.collection("Masters")
                                            .document(bookingList.get(position).getBarberEmail())
                                            .set(master);
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        if(language.equals("ru")) {
            holder.barber_name.setText(bookingList.get(position).getBarberName() + " " + bookingList.get(position).getBarberSurname());
            holder.service_name.setText(bookingList.get(position).getService());
        }
        else {
            holder.barber_name.setText(translitClass.toTranslit(bookingList.get(position).getBarberName()) + " " + translitClass.toTranslit(bookingList.get(position).getBarberSurname()));
            holder.service_name.setText(bookingList.get(position).getServiceEN());
        }

        holder.price.setText("RUB " + bookingList.get(position).getPrice().toString());
        holder.time.setText(bookingList.get(position).getTime() + "  " + bookingList.get(position).getDate());

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+bookingList.get(position).getBarberEmail());
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

        if(title == 1)
        {
            holder.rating.setVisibility(View.INVISIBLE);

            holder.rate_me.setText(R.string.rate_is_avail_after_visiting);

        }
        else if (title == 2){

            holder.rating.setVisibility(View.VISIBLE);

            switch (Integer.valueOf(bookingList.get(position).getRating()))
            {
                case 0:
                    holder.rating.setRating(0);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                case 1:
                    holder.rating.setRating(1);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                case 2:
                    holder.rating.setRating(2);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                case 3:
                    holder.rating.setRating(3);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                case 4:
                    holder.rating.setRating(4);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                case 5:
                    holder.rating.setRating(5);
                    holder.rate_me.setText(R.string.already_rated);
                    holder.rating.setIsIndicator(true);
                    break;
                default:
                    holder.rating.setVisibility(View.INVISIBLE);
                    holder.rate_me.setText(R.string.avail_for_raiting);
                    holder.rate_me.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(user.getCurrentUser() == null)
                                showDialogRegister();
                            else if(!holder.rate_me.getText().equals(R.string.already_rated))
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
        CircleImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_visiting);
            barber_name = itemView.findViewById(R.id.tv_barber_vis_my);
            time = itemView.findViewById(R.id.tv_date_vis_my);
            price = itemView.findViewById(R.id.tv_price_visiting);
            service_name = itemView.findViewById(R.id.tv_service_name_vis);
            rate_me = itemView.findViewById(R.id.tv_rate_me);
            rating = itemView.findViewById(R.id.ratingBar);
            img = itemView.findViewById(R.id.img_service);
        }
    }

    private void showDialogForGetStars(ViewHolder holder, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_set_stars, viewGroup, false);

        EditText comment = dialogView.findViewById(R.id.et_comment);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar_master);

        builder.setView(dialogView);

        Toast.makeText(mContext, bookingList.get(position).getBarberEmail(), Toast.LENGTH_LONG).show();

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String com = comment.getText().toString();
                float rat = ratingBar.getRating();

                Map<String, Object> map = new HashMap<>();
                map.put("rating", String.valueOf(Math.round(rat)));
                map.put("comment", com);

                db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                        .collection(bookingList.get(position).getDateId())
                        .document(String.valueOf(bookingList.get(position).getSlot()))
                        .update(map);

                db.collection("Comments").document(bookingList.get(position).getBarberEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        int count = Integer.valueOf(task.getResult().getString("count"));
                        Float ratingMaster = Float.valueOf(task.getResult().getString("rating"));
                        Map<String, Object> map = new HashMap<>();
                        map.put("comment", com);
                        map.put("rating", String.valueOf(Math.round(rat)));
                        map.put("customerEmail", bookingList.get(position).getCustomerEmail());
                        map.put("name", bookingList.get(position).getCustomerName());
                        map.put("surname", bookingList.get(position).getCustomerSurname());
                        db.collection("Comments").document(bookingList.get(position).getBarberEmail())
                                .collection("Comments").document(String.valueOf(count)).set(map);

                        db.collection("Comments").document(bookingList.get(position).getBarberEmail())
                                .update("count", String.valueOf(count+1));

                        if(count>0)
                        {
                            float rating = ((ratingMaster * (count-1)) + rat)/count;

                            db.collection("Comments").document(bookingList.get(position).getBarberEmail())
                                    .update("rating", String.valueOf(rating/count));
                            db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                                    .update("score", String.valueOf(rating/count));
                        }
                        else
                        {
                            db.collection("Comments").document(bookingList.get(position).getBarberEmail())
                                    .update("rating", String.valueOf(rat));
                            db.collection("Masters").document(bookingList.get(position).getBarberEmail())
                                    .update("score", String.valueOf(rat));
                        }
                    }
                });

                //   /Users/rfff@mail.ru/Visitings/1
                db.collection("Users").document(user.getCurrentUser().getEmail())
                        .collection("Visitings")
                        .document(String.valueOf(position)).update(map);

                holder.rating.setVisibility(View.VISIBLE);
                holder.rate_me.setText(R.string.already_rated);
                holder.rating.setRating(Math.round(rat));
                holder.rating.setIsIndicator(true);
            }
        });
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
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
        alertDialog.setMessage(mContext.getResources().getString(R.string.to_comment_should_register));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.not_now),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.registration),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, RegistrationActivity.class);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}
