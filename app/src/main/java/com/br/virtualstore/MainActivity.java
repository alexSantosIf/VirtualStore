package com.br.virtualstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.br.virtualstore.adapter.ViewPagerAdapter;
import com.br.virtualstore.async.AsyncUsuario;
import com.br.virtualstore.entity.ProdutoNotification;
import com.br.virtualstore.firebase.MyFirebaseMessagingService;
import com.br.virtualstore.fragment.FragmentCompras;
import com.br.virtualstore.fragment.FragmentPerfil;
import com.br.virtualstore.fragment.FragmentProdutos;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Drawer drawer;

    private BroadcastReceiver registrationBroadcastReceiver;

    private static final long ID_ND_FOOTER = 500;

    private static final String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    private static final String PUSH = "PUSH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        configurarViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final PrimaryDrawerItem itemPefil = new PrimaryDrawerItem()
                .withName("Perfil")
                .withIcon(GoogleMaterial.Icon.gmd_person);
        final PrimaryDrawerItem itemProdutos = new PrimaryDrawerItem()
                .withName("Produtos")
                .withBadge("43")
                .withIcon(FontAwesome.Icon.faw_th_list)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));
        final PrimaryDrawerItem itemCompras = new PrimaryDrawerItem()
                .withName("Últimas Compras")
                .withBadge("2")
                .withIcon(GoogleMaterial.Icon.gmd_shop_two)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Diogo Souza")
                                .withEmail("diogosouzac@gmail.com")
                                .withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(drawerHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Conta do Usuário"),
                        itemPefil,
                        new SectionDrawerItem().withName("Ações do Sistema"),
                        itemProdutos,
                        itemCompras
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        configuraItensDrawer(position, drawerItem);
                        return true;
                    }
                })
                .build();

        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                .withName("Sobre o App")
                .withIcon(GoogleMaterial.Icon.gmd_info)
                .withIdentifier(ID_ND_FOOTER));

        Serializable serializable = getIntent().getExtras() != null ? getIntent().getExtras().getSerializable("nf_produto") : null;
        if (serializable != null) {
            ProdutoNotification nf_produto = (ProdutoNotification) serializable;
            Toast.makeText(MainActivity.this, nf_produto.toString(), Toast.LENGTH_SHORT).show();
        }

     //   configurarFCM();
    }

    private void configuraItensDrawer(int position, IDrawerItem drawerItem) {
        viewPager.setCurrentItem(position);

        switch ((int) drawerItem.getIdentifier()) {
            case (int) ID_ND_FOOTER:
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

                    String versao = info.versionName;
                    Toast.makeText(MainActivity.this, "Versão: " + versao, Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }

        drawer.closeDrawer();
    }

    private void configurarViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentCompras(), "Compras");
        viewPagerAdapter.addFragment(new FragmentProdutos(), "Produtos");
        viewPagerAdapter.addFragment(new FragmentPerfil(), "Perfil");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* private void configurarFCM() {
        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra("token");
                Toast.makeText(MainActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
            }
        };

        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver, new IntentFilter(REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver, new IntentFilter(PUSH));
    }*/
}
