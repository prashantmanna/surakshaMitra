package com.surakshamitra;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.surakshamitra.R;
import com.surakshamitra.model;
import java.util.List;

public class contactAdaptor1 extends RecyclerView.Adapter<contactAdaptor1.ViewHolder> {

    private List<model> contacts;
    private OnItemClickListener listener;

    public contactAdaptor1(List<model> contacts, OnItemClickListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(model contact);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chattext, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewContactName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContactName = itemView.findViewById(R.id.text_contact_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(contacts.get(position));
                    }
                }
            });
        }

        public void bind(model contact) {
            textViewContactName.setText(contact.getContact());
        }
    }
}
