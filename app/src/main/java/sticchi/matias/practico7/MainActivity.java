package sticchi.matias.practico7;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sticchi.matias.practico7.colecciones.Contactos;
import sticchi.matias.practico7.db.ContactosSQLiteHelper;
import sticchi.matias.practico7.entidades.Contacto;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//    private TextView mMensaje;
    private Spinner filtroSpinner;
    private List<Contacto> listaContactos;
    private List<Contacto> listaFiltrada;
    private int filtroSelected = 0;
    private ListView list;
    private MostrarContactoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crearDB();
        addWidgets();
        getAll();
        addListeners();
        addAdapters();
    }

    private void getAll()
    {
        listaContactos = Contactos.getAll(this);
        listaFiltrada = Contactos.getAll(this);
    }

    private void addAdapters() {
        ArrayAdapter<CharSequence> adtFiltro = ArrayAdapter.createFromResource(
                this, R.array.filtro, android.R.layout.simple_spinner_item);
        adtFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.filtroSpinner.setAdapter(adtFiltro);

        if(listaFiltrada != null);
        {
            adapter = new MostrarContactoAdapter(this, listaFiltrada);
            list.setAdapter(adapter);
        }
    }

    private void addListeners() {
        this.filtroSpinner.setOnItemSelectedListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /////////////////////////////////////////////////////////////////////////////
                // Si el elemento clickeado no esta seleccionado lo selecciona,
                // caso contrario lo deselecciona.
                /////////////////////////////////////////////////////////////////////////////
                for (int i = 0; i < list.getChildCount(); i++) {
                    if(position == i ){
                        if (list.getChildAt(position).getTag() == null) {
                            list.getChildAt(position).setBackground(getResources().getDrawable(
                                    R.drawable.gradient_bg_hover));
                            list.getChildAt(position).setTag(R.drawable.gradient_bg_hover);
                        }
                        else
                        {
                            list.getChildAt(position).setBackground(getResources().getDrawable(
                                    R.drawable.gradient_bg));
                            list.getChildAt(position).setTag(null);
                        }
                    }else{
                        list.getChildAt(i).setBackground(getResources().getDrawable(
                                R.drawable.gradient_bg));
                        list.getChildAt(i).setTag(null);
                    }

                }

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, listaFiltrada.get(i).toString(),
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void addWidgets()
    {
//        mMensaje = (TextView) findViewById(R.id.mje);
        filtroSpinner = (Spinner) findViewById(R.id.spinner);
        this.list=(ListView)findViewById(R.id.list);
    }

    private void crearDB()
    {
        ContactosSQLiteHelper contactosHelper = new ContactosSQLiteHelper(this, "db_contactos", null, 1);

        SQLiteDatabase db = contactosHelper.getWritableDatabase();

        if(db != null)
        {
//            mMensaje.setText("Base de datos creada!!");
            System.out.println("Base de datos creada!!");
            db.close();
        }
        else
            System.out.println("No se pudo crear la base de datos!!");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        reloadData(i);
    }

    private void reloadData(int filtro) {
        listaFiltrada = new ArrayList<Contacto>();
        for (Contacto c : this.listaContactos) {
            if (filtro == 0) {
                listaFiltrada.addAll(this.listaContactos);
                break;
            }
            else
            if (c.getGrupo() == filtro)
                listaFiltrada.add(c);
        }
        this.adapter.getData().clear();
        this.adapter.getData().addAll(listaFiltrada);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
