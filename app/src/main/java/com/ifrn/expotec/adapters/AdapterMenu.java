package com.ifrn.expotec.adapters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ifrn.expotec.fragments.TabFragment;
import com.ifrn.expotec.models.Atividade;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by fabiano on 02/10/15.
 */
public class AdapterMenu extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;
    boolean conectado;
    TabLayout tabLayout;
    HashMap<String,List<Atividade>> atividadesPorTipo;
    public AdapterMenu(FragmentManager supportFragmentManager, TabLayout tabLayout,HashMap<String,List<Atividade>> atividadesPorTipo,Context context, boolean conectado) {
        super(supportFragmentManager);
        mNumOfTabs = tabLayout.getTabCount();
        this.conectado = conectado;
        this.tabLayout = tabLayout;
        this.atividadesPorTipo = atividadesPorTipo;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        String key = tabLayout.getTabAt(position).getText().toString();

        TabFragment tab = new TabFragment(conectado,atividadesPorTipo.get(key));

        return tab;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
