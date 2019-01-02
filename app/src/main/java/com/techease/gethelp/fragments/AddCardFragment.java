package com.techease.gethelp.fragments;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.addCardModel.AddCardResponse;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.GeneralUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.et_card_holder_name)
    EditText etCardHolderName;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.btn_expiration_date)
    Button btnExpDate;
    @BindView(R.id.et_cvv)
    EditText etCVV;
    @BindView(R.id.btn_add_card)
    Button btnAddCard;
    private View view;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private boolean valid = false;
    private String name, cardNumber, expMonth, expYear, cvv;


    public AddCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        btnAddCard.setOnClickListener(this);
        btnExpDate.setOnClickListener(this);
        initCalendar();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expiration_date:
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_add_card:
                GeneralUtils.acProgressPieDialog(getActivity());
                addCard();


        }

    }

    private void addCard() {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AddCardResponse> call = service.addCard(GeneralUtils.getUserID(getActivity()),
                name, cardNumber, cvv, expMonth, expYear);
        call.enqueue(new Callback<AddCardResponse>() {
            @Override
            public void onResponse(Call<AddCardResponse> call, Response<AddCardResponse> response) {
                GeneralUtils.progress.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddCardResponse> call, Throwable t) {
                GeneralUtils.progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initCalendar() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {
        String myFormat = "MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        btnExpDate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean validate() {
        valid = true;
        name = etCardHolderName.getText().toString();
        cardNumber = etCardNumber.getText().toString();
        expYear = btnExpDate.getText().toString();
        String[] expYearArray = expYear.split("/");
        expMonth = expYearArray[0];
        expYear = expYearArray[1];
        cvv = etCVV.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            etCardHolderName.setError("at least 3 characters");
            valid = false;
        } else {
            etCardHolderName.setError(null);
        }
        if (cardNumber.isEmpty() || cardNumber.length() < 5) {
            Toast.makeText(getActivity(), "Please scan an ATM card", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            etCardNumber.setError(null);
        }
        if (expYear.equals("05/18")) {
            Toast.makeText(getActivity(), "Please enter a valid expiration date", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            btnExpDate.setError(null);
        }
        if (cvv.isEmpty() || cvv.length() < 3) {
            etCVV.setError("enter a valid CVV");
            valid = false;
        } else {
            etCVV.setError(null);
        }
        return valid;
    }
}
