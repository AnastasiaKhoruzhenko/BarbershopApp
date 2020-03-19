//package com.coursework.barbershopapp.User.ui.signup;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.coursework.barbershopapp.R;
//
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class RecyclerViewAdapterDateStep3 extends RecyclerView.Adapter<RecyclerViewAdapterDateStep3.ViewHolder>{
//
//    List<String> listDates;
//    Context mContext;
//
//    public RecyclerViewAdapterDateStep3(List<String> listDates, Context mContext) {
//        this.listDates = listDates;
//        this.mContext = mContext;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.date.setText(listDates.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return listDates.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        CardView dateCard;
//        TextView date;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            dateCard = itemView.findViewById(R.id.cardview_choose_data);
//            date = itemView.findViewById(R.id.recview_choose_data);
//        }
//    }
//}
