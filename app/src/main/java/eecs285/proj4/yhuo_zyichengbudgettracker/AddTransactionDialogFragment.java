package eecs285.proj4.yhuo_zyichengbudgettracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionDialogFragment extends DialogFragment {
    interface AddTransactionDialogListener {
        void onDialogPositiveClick(DialogFragment dialog,
                                   String category,
                                   String name,
                                   String time,
                                   long cost);
    }

    AddTransactionDialogListener listener;
    View dialogLayoutView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddTransactionDialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        dialogLayoutView = inflater.inflate(R.layout.dialog_add_transaction, null);

        builder.setView(dialogLayoutView);
        builder.setTitle(R.string.add_transaction_title);
        builder.setPositiveButton(R.string.add_button,
                        null);
        builder.setNegativeButton(R.string.cancel_button,
                        (dialog, id) -> getDialog().cancel());

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean wantToCloseDialog = false;
                    boolean categoryIsValid = false;
                    boolean nameIsValid = false;
                    boolean costIsValid = false;

                    EditText categoryText = dialogLayoutView
                            .findViewById(R.id.editTransactionCategory);
                    EditText nameText = dialogLayoutView
                            .findViewById(R.id.editTransactionName);
                    EditText costText = dialogLayoutView
                            .findViewById(R.id.editTransactionCost);

                    if (TextUtils.isEmpty(categoryText.getText())) {
                        categoryText.setError("Enter a category");
                        categoryIsValid = false;
                    } else {
                        categoryIsValid = true;
                    }

                    if (TextUtils.isEmpty(nameText.getText())) {
                        nameText.setError("Enter a name");
                        nameIsValid = false;
                    } else {
                        nameIsValid = true;
                    }

                    if (TextUtils.isEmpty(costText.getText())) {
                        costText.setError("Enter a valid cost");
                        costIsValid = false;
                    } else {
                        // when cost not empty
                        costIsValid = true;
                        String costString = costText.getText().toString();
                        if (costString.contains(".")) {
                            int decimalDigitLen =
                                    costString.length() - costString.indexOf(".") - 1;
                            if (decimalDigitLen != 2) {
                                costText.setError("Enter a valid cost");
                                costIsValid = false;
                            }
                        }
                    } // else, cost not empty

                    wantToCloseDialog = categoryIsValid && nameIsValid && costIsValid;

                    if(wantToCloseDialog) {
                        addTransaction();
                        dialog.dismiss();
                    }
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }


    void addTransaction() {
        EditText categoryText = dialogLayoutView.findViewById(R.id.editTransactionCategory);
        EditText nameText = dialogLayoutView.findViewById(R.id.editTransactionName);
        EditText costText = dialogLayoutView.findViewById(R.id.editTransactionCost);
        String costString = costText.getText().toString();
        long centValue;

        if (costString.contains(".")) {
            // remove the "." from cost, and store as cents
            String newCostString = costString.replace(".", "");
            centValue = Long.parseLong(newCostString);
        } else {
            centValue = Long.parseLong(costString);
            centValue = centValue * 100;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        Date date = new Date();
        String timeString = (String) formatter.format(date);

        listener.onDialogPositiveClick(this,
                categoryText.getText().toString().trim(),
                nameText.getText().toString().trim(),
                timeString,
                centValue);
    }



}
