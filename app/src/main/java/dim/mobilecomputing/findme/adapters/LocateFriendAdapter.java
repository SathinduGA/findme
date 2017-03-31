package dim.mobilecomputing.findme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.activities.MainActivity;
import dim.mobilecomputing.findme.application.FindMe;
import dim.mobilecomputing.findme.models.User;

/**
 * Created by Sathindu on 2016-07-22.
 */
public class LocateFriendAdapter extends RecyclerView.Adapter<LocateFriendAdapter.LocateFriendViewHolder> {

    List<User> users;
    Context context;
    LayoutInflater layoutInflater;

    public LocateFriendAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public LocateFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.row_locate_friend, parent, false);
        return new LocateFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LocateFriendViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getItem(int position) {
        return users.get(position);
    }

    public class LocateFriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name;
        TextView email;

        public LocateFriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            FindMe.bus.register(this);

            image = (ImageView) itemView.findViewById(R.id.friend_locate_image);
            name = (TextView) itemView.findViewById(R.id.friend_locate_name);
            email = (TextView) itemView.findViewById(R.id.friend_locate_email);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("SELECTION",getItem(position).getName());

            User u = new User();
            u.setUid(getItem(position).getUid());
            u.setName(getItem(position).getName());
            u.setEmail(getItem(position).getEmail());
            FindMe.bus.post(u);
        }
    }
}
