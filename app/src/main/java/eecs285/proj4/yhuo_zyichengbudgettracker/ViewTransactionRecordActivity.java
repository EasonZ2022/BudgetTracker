package eecs285.proj4.yhuo_zyichengbudgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ViewTransactionRecordActivity extends AppCompatActivity {
    private ArrayAdapter<Transaction> adapter;
    private List<Transaction> transactionRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_record);
        setTitle(R.string.all_transactions_title);


        Intent intent = getIntent();
        transactionRecordList
                = (ArrayList<Transaction>)
                intent.getSerializableExtra(MainActivity.EXTRA_TRANSACTION_RECORD);

        Collections.reverse(transactionRecordList);

        adapter = new TransactionRecordAdapter(this,
                R.layout.activity_view_transaction_record,
                transactionRecordList);

        ListView listView = findViewById(R.id.transactionRecordListView);
        listView.setAdapter(adapter);
    }
}