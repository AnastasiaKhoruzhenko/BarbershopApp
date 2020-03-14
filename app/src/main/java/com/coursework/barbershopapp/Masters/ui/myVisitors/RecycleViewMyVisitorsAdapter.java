package com.coursework.barbershopapp.Masters.ui.myVisitors;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleViewMyVisitorsAdapter extends RecyclerView.Adapter<RecycleViewMyVisitorsAdapter.MyViewHolder>{

    List<BookingInformation> bookList;
    Context mContext;

    public RecycleViewMyVisitorsAdapter(Context mContext, List<BookingInformation> bookList) {
        this.bookList = bookList;
        this.mContext = mContext;
    }

    public RecycleViewMyVisitorsAdapter(Context mContext) {
        this.mContext = mContext;
        this.bookList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.cardview_my_visitor, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(getItemCount() != 0)
        {
            holder.name.setText(bookList.get(position).getCustomerName());
            holder.time.setText(bookList.get(position).getTime());

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name, time;
        CircleImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_my_visitor);
            time = itemView.findViewById(R.id.tv_time_visitor);
            name = itemView.findViewById(R.id.tv_name_visitor);
            img = itemView.findViewById(R.id.visitor_photo);
        }
    }

    private void showDialog(MyViewHolder holder, int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomAlertDialog);
        ViewGroup viewGroup = holder.itemView.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_visitor_info, viewGroup, false);

        TextView name = dialogView.findViewById(R.id.tv_dialog_name);
        TextView time = dialogView.findViewById(R.id.tv_dialog_time);
        TextView date = dialogView.findViewById(R.id.tv_dialog_date);
        TextView service = dialogView.findViewById(R.id.tv_dialog_service);
        TextView inDays = dialogView.findViewById(R.id.tv_dialog_days);
        ImageView img = dialogView.findViewById(R.id.btn_dialog_close);

        try {
            Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
            Date appointmentDate = simpleDateFormat.parse(bookList.get(position).getDate());
            long count;
            if((count = (appointmentDate.getTime() - nowDate.getTime())/ (24 * 60 * 60 * 1000)) == 1)
                inDays.setText(count + " Day");
            else
                inDays.setText(count + "Days");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        name.setText(bookList.get(position).getCustomerName());
        time.setText(bookList.get(position).getTime());
        date.setText(bookList.get(position).getDate());
        service.setText(bookList.get(position).getService());

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
