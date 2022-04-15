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

public class TransactionRecordAdapter extends ArrayAdapter<Transaction> {
    TransactionRecordAdapter(Context context, int resource, List<Transaction> transactionRecordList) {
        super(context, resource, transactionRecordList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.single_record_list_item, parent, false);
        }

        TextView recordCategoryView =
                convertView.findViewById(R.id.recordCategoryView);
        TextView recordNameView =
                convertView.findViewById(R.id.recordNameView);
        TextView recordCostView =
                convertView.findViewById(R.id.recordCostView);
        TextView recordTimeView =
                convertView.findViewById(R.id.recordTimeView);

        recordCategoryView.setText(getItem(position).getCategory());
        recordNameView.setText(getItem(position).getName());
        recordCostView.setText(getItem(position).getCostString());
        recordTimeView.setText(getItem(position).getTime());

        return convertView;
    }
}
