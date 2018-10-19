package techease.com.gethelp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import techease.com.gethelp.R;


public class OnBoardFragment extends Fragment {
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.btn_email)
    Button btnEmail;

    Unbinder unbinder;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_onboard, container, false);
        unbinder = ButterKnife.bind(this,view);
        initUI();
        return view;
    }

    private void initUI(){

       btnEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragment fragment = new SignUpFragment();
               getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("abc").commit();
           }
       });

       tvLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragment fragment = new LoginFragment();
               getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("abc").commit();
           }
       });
    }

}
