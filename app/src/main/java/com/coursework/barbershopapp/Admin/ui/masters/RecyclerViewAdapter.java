package com.coursework.barbershopapp.Admin.ui.masters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewCommentAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Comment;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Master> personList;
    private List<Master> personListFull;
    private Context mContext;
    private Dialog dialog;
    private FirebaseFirestore db;

    public RecyclerViewAdapter(Context mContext, List<Master> personList) {
        this.mContext = mContext;
        this.personList = personList;
        this.personListFull = new ArrayList<>(personList);
        db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_masters_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        db.collection("Comments").document(personList.get(position).getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int count  = Integer.valueOf(task.getResult().getString("count"));
                if(count == 0)
                    holder.comments.setClickable(false);
                switch (count%10)
                {
                    case 1:
                        holder.comments.setText(count + " "+ mContext.getResources().getString(R.string.comment1));
                        break;
                    case 2:
                    case 3:
                    case 4:
                        holder.comments.setText(count + " "+ mContext.getResources().getString(R.string.comment234));
                        break;
                    default:
                        holder.comments.setText(count + " "+ mContext.getResources().getString(R.string.commentDef));
                        break;
                }
            }
        });

        if(language.equals("ru")) {
            holder.nameSurname.setText(personList.get(position).getName() + " " + personList.get(position).getSurname());
        }
        else {
            holder.nameSurname.setText(translitClass.toTranslit(personList.get(position).getName()) + " " + translitClass.toTranslit(personList.get(position).getSurname()));
        }

        holder.score.setText(personList.get(position).getScore());

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+personList.get(position).getEmail());
        phRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //img.setImageURI(uri);

                Glide.with(mContext)
                        .load(uri)
                        .into(holder.photo);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!personList.get(position).getScore().equals(String.valueOf(0.0)))
                    showFullDialog(v, position);
                else
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_comments_master), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Master> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(personListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Master master : personListFull)
                    if(master.getSurname().toLowerCase().contains(filterPattern) ||
                    master.getName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(master);
                    }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            personList.clear();
            personList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView photo;
        TextView nameSurname, score, comments;
        ImageView starImg;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.masterPhoto);
            nameSurname = itemView.findViewById(R.id.masterNameSurname);
            score = itemView.findViewById(R.id.tv_score_master_admin);
            starImg = itemView.findViewById(R.id.iv_star_admin);
            cardView = itemView.findViewById(R.id.cardview_master_admin_for_list);
            comments = itemView.findViewById(R.id.textView10);
        }
    }

    private void showFullDialog(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_comments_dialog, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        RecyclerView recyclerView = dialog.findViewById(R.id.recview_comments);

        db.collection("Comments")
                .document(personList.get(position).getEmail())
                .collection("Comments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<Comment> commentList = new ArrayList<>();
                    for(DocumentSnapshot comment : task.getResult())
                        commentList.add(comment.toObject(Comment.class));

                    initRecViewComment(commentList, recyclerView, personList.get(position).getEmail(), true);
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

    private void initRecViewComment(List<Comment> list, RecyclerView recyclerView, String email, boolean canDelete) {
        RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(mContext, list, email, canDelete);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
    }
}
