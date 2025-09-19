package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    private static final String ARG_IS_EDIT = "is_edit";
    private static final String ARG_INDEX = "index";
    private static final String ARG_CITY = "city";

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int index, City city);
    }

    private AddCityDialogListener listener;

    public AddCityFragment() {}

    public static AddCityFragment newInstance(int index, City city) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putBoolean(ARG_IS_EDIT, true);
        b.putInt(ARG_INDEX, index);
        b.putSerializable(ARG_CITY, city);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        boolean isEdit = false;
        int index = -1;
        City cityArg = null;

        Bundle args = getArguments();
        if (args != null && args.getBoolean(ARG_IS_EDIT, false)) {
            isEdit = true;
            index = args.getInt(ARG_INDEX);
            cityArg = (City) args.getSerializable(ARG_CITY);
            if (cityArg != null) {
                editCityName.setText(cityArg.getName());
                editProvinceName.setText(cityArg.getProvince());
            }
        }

        boolean finalIsEdit = isEdit;
        int finalIndex = index;
        City finalCityArg = cityArg;

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(finalIsEdit ? "Edit city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(finalIsEdit ? "OK" : "Add", (d, w) -> {
                    String name = editCityName.getText().toString();
                    String prov = editProvinceName.getText().toString();
                    if (finalIsEdit && finalCityArg != null) {
                        finalCityArg.setName(name);
                        finalCityArg.setProvince(prov);
                        listener.updateCity(finalIndex, finalCityArg);
                    } else {
                        listener.addCity(new City(name, prov));
                    }
                })
                .create();
    }
}
