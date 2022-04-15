package eecs285.proj4.yhuo_zyichengbudgettracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {
    TransactionsAdapter(Context context, int resource, List<Transaction> transactionList) {
        super(context, resource, transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.single_list_item, parent, false);
        }

        TextView categoryView =
                convertView.findViewById(R.id.transactionCategoryView);
        TextView costView =
                convertView.findViewById(R.id.transactionCostView);

        categoryView.setText(getItem(position).getCategory());
        costView.setText(getItem(position).getCostString());

        return convertView;
    }
}


