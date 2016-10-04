package sticchi.matias.practico7;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        addContextMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(MainActivity.this, "Funcion no implementada", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void addContextMenu() {
        registerForContextMenu(list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_ctx, menu);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)menuInfo;

        int contactoID = Integer.parseInt(list.getAdapter().getItem(info.position).toString());
        Contacto c = listaFiltrada.get(contactoID);

        menu.setHeaderTitle(c.getNombre()+" "+c.getApellido());

        inflater.inflate(R.menu.menu_ctx, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int contactoID = Integer.parseInt(list.getAdapter().getItem(info.position).toString());
        Contacto c = listaFiltrada.get(contactoID);

        switch (item.getItemId()) {
            case R.id.CtxOpc1:
                String uri = "tel:"+c.getTelefono();
                call(uri);
                return true;
            case R.id.CtxOpc2:
                Toast.makeText(MainActivity.this, c.toString(), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void call(String uri) {
        Intent in=new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        try{
            startActivity(in);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(),"No se puede realizar la llamada",Toast.LENGTH_SHORT).show();
        }
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
