package com.example.bhargav.cloudmagicdemoapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bhargav.cloudmagicdemoapp.R;
import com.example.bhargav.cloudmagicdemoapp.models.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bhargav on 7/30/2016.
 */
public class EmailRecyclerAdapter extends RecyclerView.Adapter<EmailRecyclerAdapter.ViewHolder> {

    private List<Message> emailList;
    private Context mContext;
    private recyclerClickListner clickListner;
    private recyclerLongClickListner longClickListner;
    private View previousLongPressedView = null;
    public EmailRecyclerAdapter(List<Message> emails, Context context){
        this.emailList = emails;
        this.mContext = context;

        clickListner = (recyclerClickListner) mContext;
        longClickListner = (recyclerLongClickListner) mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_email, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = emailList.get(position);
        holder.senderName.setText(message.getParticipants().get(0));
        holder.emailPreviewContent.setText(message.getPreview());
        holder.emailTime.setText("1:57PM");
        if(message.isStarred())
            holder.importantEmail.setImageResource(R.drawable.ic_imp);
        else
            holder.importantEmail.setImageResource(R.drawable.ic_star);
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private de.hdodenhof.circleimageview.CircleImageView profileImageView;
        private TextView senderName, emailPreviewContent, emailTime;
        private ImageView importantEmail;
        public ViewHolder(final View itemView) {
            super(itemView);
            profileImageView = (CircleImageView) itemView.findViewById(R.id.profile_image);
            senderName = (TextView) itemView.findViewById(R.id.tv_email_sender_name);
            emailPreviewContent = (TextView) itemView.findViewById(R.id.tv_email_description);
            emailTime = (TextView) itemView.findViewById(R.id.tv_email_time);
            importantEmail = (ImageView) itemView.findViewById(R.id.iv_important);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBackgroundOfPreviousLongPressedView();
                    Log.d("MainActiviy",getAdapterPosition() + " " );
                    clickListner.onItemClicked(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setBackgroundOfPreviousLongPressedView();
                    previousLongPressedView = v;
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.gray_91));
                    Log.d("MainActiviy","long click   " +getAdapterPosition() + " " );
                    longClickListner.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    private void setBackgroundOfPreviousLongPressedView() {
        if(previousLongPressedView != null) {
            previousLongPressedView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        }
    }

    public interface recyclerClickListner {
        void onItemClicked(int position);
    }

    public interface recyclerLongClickListner {
        void onItemLongClicked(int position);
    }
}
