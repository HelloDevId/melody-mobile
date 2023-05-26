package com.melodybeauty.melody_mobile;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.melodybeauty.melody_mobile.databinding.ActivityHomepageBinding;
import com.melodybeauty.melody_mobile.fragment.FragmentKategori2;
import com.melodybeauty.melody_mobile.fragment.HomeFragment;
import com.melodybeauty.melody_mobile.fragment.KonsultasiFragment;
import com.melodybeauty.melody_mobile.fragment.ProfileFragment;

public class HomepageActivity extends AppCompatActivity {

    private ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        replaceFragment(new HomeFragment());
        binding.navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_content:
                    replaceFragment(new KonsultasiFragment());
                    break;
                case R.id.navigation_profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }
    private  void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment,fragment);
        fragmentTransaction.commit();
    }
}