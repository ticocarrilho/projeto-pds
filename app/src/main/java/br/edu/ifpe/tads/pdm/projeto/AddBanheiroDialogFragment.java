package br.edu.ifpe.tads.pdm.projeto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddBanheiroDialogFragment extends DialogFragment {

    public AddBanheiroDialogFragment() {
    }

    public static AddBanheiroDialogFragment newInstance(AddBanheiroSubject addBanheiroSubject) {
        AddBanheiroDialogFragment frag = new AddBanheiroDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("addBanheiroSubject", addBanheiroSubject);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AddBanheiroSubject addBanheiroSubject = (AddBanheiroSubject) getArguments().getSerializable("addBanheiroSubject");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_banheiro_dialog, null);

        alertDialogBuilder.setTitle("Informações do Banheiro");
        alertDialogBuilder.setView(view);
        setupRadioGroup(view);
        setupNomeEditTextWatcher(view);

        alertDialogBuilder.setPositiveButton("Criar", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            EditText localInput = (EditText) view.findViewById(R.id.local_banheiro);
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.tipo_banheiro_radio);
            int selectedRadioId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadio = (RadioButton) view.findViewById(selectedRadioId);
            CheckBox fraldarioCheckbox = (CheckBox) view.findViewById(R.id.fraldario_checkbox);
            String banheiroTipo = selectedRadio.getText().toString();

            if(banheiroTipo.equals("Público")) {
                addBanheiroSubject.setBanheiro(localInput.getText().toString(), banheiroTipo, "0", 0, 0, fraldarioCheckbox.isChecked());
            } else {
                EditText preco = (EditText) view.findViewById(R.id.preco_banheiro);
                addBanheiroSubject.setBanheiro(localInput.getText().toString(), banheiroTipo, preco.getText().toString(), 0, 0, fraldarioCheckbox.isChecked());
            }

            Toast.makeText(getContext(), "Clique na localização do banheiro", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void setupRadioGroup(View view) {
        final EditText precoText = (EditText) view.findViewById(R.id.preco_banheiro);

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.tipo_banheiro_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_privado){
                    precoText.setVisibility(View.VISIBLE);
                } else {
                    precoText.setVisibility(View.GONE);
                    precoText.setText("");
                }
            }
        });
    }

    private void setupNomeEditTextWatcher(View view) {
        EditText localInput = (EditText) view.findViewById(R.id.local_banheiro);
        localInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AlertDialog alertDialog = (AlertDialog) getDialog();
                if(s.toString().trim().length() == 0){
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}