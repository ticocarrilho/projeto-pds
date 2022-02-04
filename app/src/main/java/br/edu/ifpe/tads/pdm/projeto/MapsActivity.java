package br.edu.ifpe.tads.pdm.projeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifpe.tads.pdm.projeto.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FloatingActionButton fab;
    private AddBanheiroSubject addBanheiroSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        this.addBanheiroSubject = new AddBanheiroSubject();
        this.fab = (FloatingActionButton) findViewById(R.id.fab);

        this.toolbarSetup();
        this.drawerSetup();
        this.fabSetup();

        getIntent().putExtra("addBanheiroSubject", addBanheiroSubject);
        this.setFragmentView(MapsFragment.class, "Maps");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
                Menu navMenu = navigationView.getMenu();

                if (null != user) {
                    fab.show();
                    navMenu.findItem(R.id.nav_login_fragment).setVisible(false);
                    navMenu.findItem(R.id.nav_cadastro_fragment).setVisible(false);
                    navMenu.findItem(R.id.sair_button).setVisible(true);
                } else {
                    fab.hide();
                    navMenu.findItem(R.id.nav_login_fragment).setVisible(true);
                    navMenu.findItem(R.id.nav_cadastro_fragment).setVisible(true);
                    navMenu.findItem(R.id.sair_button).setVisible(false);
                }
            }
        });
    }

    private void toolbarSetup() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void drawerSetup() {
        this.mDrawer = findViewById(R.id.drawer_layout);

        this.nvDrawer = findViewById(R.id.navigationView);

        this.nvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void fabSetup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    private void showAddDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddBanheiroDialogFragment addBanheiroDialogFragment = AddBanheiroDialogFragment.newInstance(this.addBanheiroSubject);
        addBanheiroDialogFragment.show(fm, "fragment_add_banheiro_dialog");
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Class fragmentClass = MapsFragment.class;
        String fragmentTag = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_inicio_fragment:
                fragmentClass = MapsFragment.class;
                fragmentTag = "Maps";
                this.fab.show();
                break;
            case R.id.nav_login_fragment:
                fragmentClass = LoginFragment.class;
                fragmentTag = "Login";
                this.fab.hide();
                break;
            case R.id.nav_cadastro_fragment:
                fragmentClass = CadastroFragment.class;
                fragmentTag = "Cadastro";
                this.fab.hide();
                break;
            case R.id.sair_button:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                break;
        }

        if(fragmentTag != null) {
            setFragmentView(fragmentClass, fragmentTag);

            setTitle(menuItem.getTitle());
        }

        mDrawer.closeDrawers();
    }

    private void setFragmentView(Class fragmentClass, String fragmentTag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentTag.equals("Maps")) {
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment, fragmentTag).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment, fragmentTag).addToBackStack("Maps").commit();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}