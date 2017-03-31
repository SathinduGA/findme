package dim.mobilecomputing.findme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dim.mobilecomputing.findme.R;
import dim.mobilecomputing.findme.models.User;

/**
 * Created by Sathindu on 2016-07-19.
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> {

    List<User> users;
    Context context;
    LayoutInflater layoutInflater;

    public AddFriendAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.row_add_friend, parent, false);
        return new AddFriendViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddFriendViewHolder holder, int position) {
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

    public class AddFriendViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView email;

        public AddFriendViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.friend_image);
            name = (TextView) itemView.findViewById(R.id.friend_name);
            email = (TextView) itemView.findViewById(R.id.friend_email);

        }

    }
}
