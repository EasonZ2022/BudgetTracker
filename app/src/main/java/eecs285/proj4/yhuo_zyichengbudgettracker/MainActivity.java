package eecs285.proj4.yhuo_zyichengbudgettracker;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AddTransactionDialogFragment.AddTransactionDialogListener {

    private static final String TRANSACTIONS_FILE = "transactions";
    private static final String TRANSACTIONS_RECORD_FILE = "records";
    static final String EXTRA_TRANSACTION_RECORD = "eecs285.proj4.yhuo_zyichengbudgettracker.RECORD";

    private List<Transaction> transactionList;
    private List<Transaction> transactionRecordList;
    private ArrayAdapter<Transaction> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readTransactions();
        readTransactionsRecord();

        adapter = new TransactionsAdapter(this, R.layout.single_list_item,
                transactionList);

        ListView listView = findViewById(R.id.transactionListView);
        listView.setAdapter(adapter);

        updateTotalCost();
    }

    private void readTransactions() {
        File file = new File(getFilesDir(), TRANSACTIONS_FILE);
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {
            transactionList = (ArrayList<Transaction>)input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            transactionList = new ArrayList<>();
        }
    }

    private void readTransactionsRecord() {
        File file = new File(getFilesDir(), TRANSACTIONS_RECORD_FILE);
        try (ObjectInputStream input =
                new ObjectInputStream(new FileInputStream(file))) {
            transactionRecordList = (ArrayList<Transaction>)input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            transactionRecordList = new ArrayList<>();
        }
    }

    private void writeTransactions() {
        File file = new File(getFilesDir(), TRANSACTIONS_FILE);
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(transactionList);
        } catch (IOException exception) {
            // cause runtime error
            throw new IllegalStateException("something bad happened");
        }
    }

    private void writeTransactionsRecord() {
        File file = new File(getFilesDir(), TRANSACTIONS_RECORD_FILE);
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(transactionRecordList);
        } catch (IOException exception) {
            // cause runtime error
            throw new IllegalStateException("something bad happened");
        }
    }

    private void updateTotalCost() {
        TextView totalCostView = findViewById(R.id.totalCostView);
        long totalCost = 0;
        long totalCostDollar;
        long totalCostCent;
        String displayString = "Total: $";
        for (Transaction transaction : transactionList) {
            totalCost = totalCost + transaction.getCost();
        }
        totalCostDollar = totalCost / 100;
        totalCostCent = totalCost % 100;
        if (totalCostCent < 10) {
            displayString = displayString + totalCostDollar + ".0" + totalCostCent;
        } else {
            displayString = displayString + totalCostDollar + "." + totalCostCent;
        }
        totalCostView.setText(displayString);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,
                                      String category,
                                      String name,
                                      String time,
                                      long cost) {
        // 1: add to transactionList
        boolean canFindCategory = false;
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(category)) {
                transaction.addCost(cost);
                canFindCategory = true;
                break;
            }
        }
        if (!canFindCategory) {
            transactionList.add(new Transaction(category, name, time, cost));
        }

        // 2: add to transactionRecordList
        transactionRecordList.add(new Transaction(category, name, time, cost));

        writeTransactions();
        writeTransactionsRecord();

        adapter.notifyDataSetChanged();
        updateTotalCost();
    }

    // this is the "ADD" button on main activity
    public void addTransaction(View view) {
        DialogFragment dialog = new AddTransactionDialogFragment();
        dialog.show(getSupportFragmentManager(),
                "AddTransactionDialogFragment");
    }

    public void clearData(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure to delete all transactions?");
        alertDialogBuilder.setPositiveButton(R.string.yes,
                        (dialog, id) -> reallyClearData());

        alertDialogBuilder.setNegativeButton(R.string.no,
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void reallyClearData() {
        transactionList.clear();
        transactionRecordList.clear();

        writeTransactions();
        writeTransactionsRecord();

        adapter.notifyDataSetChanged();
        updateTotalCost();
    }

    public void viewTransactionRecord(View view) {

        Intent intent = new Intent(this, ViewTransactionRecordActivity.class);
        intent.putExtra(EXTRA_TRANSACTION_RECORD, (Serializable) transactionRecordList);
        startActivity(intent);

    }


}