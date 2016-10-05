package sticchi.matias.practico7;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    ///////////////////////////////////////////////////////////////////////////
    // NOMBRE BD
    ///////////////////////////////////////////////////////////////////////////
    private final String NOMBRE_DB = "db_contactos";

    View v;

    private Spinner filtroSpinner;
    private List<Contacto> listaContactos;
    private List<Contacto> listaFiltrada;
    private int filtroSelected = 0;
    private EditText etFiltro;
    private ListView list;
    private MostrarContactoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crearDB();
        addWidgets();
        getAll();
        addAdapters();
        addListeners();
        addContextMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                LayoutInflater li = LayoutInflater.from(this);
                View addContactView = li.inflate(R.layout.new_contact, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);
                alertDialogBuilder.setView(addContactView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Dialog f = (Dialog) dialog;

                                        EditText etNombre, etApellido, etTelefono, etGrupo;
                                        etNombre = (EditText) f.findViewById(R.id.etNombre);
                                        etApellido = (EditText) f.findViewById(R.id.etApellido);
                                        etTelefono = (EditText) f.findViewById(R.id.etTelefono);
                                        etGrupo = (EditText) f.findViewById(R.id.etGrupo);

                                        String nombre = etNombre.getText().toString();
                                        String apellido = etApellido.getText().toString();
                                        String telefono = etTelefono.getText().toString();
                                        String grupo = etGrupo.getText().toString();
                                        Contacto c = new Contacto(nombre,apellido,telefono,grupo);
                                        insertContact(c);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.create();
                alertDialogBuilder.show();
                return true;
            case R.id.menu_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int insertContact(Contacto c) {
        int transaccion = 0;
        ContactosSQLiteHelper contactos = new ContactosSQLiteHelper(this, NOMBRE_DB, null, 1);
        SQLiteDatabase db = contactos.getWritableDatabase();

        if(db != null) {
            ContentValues nuevoRegistro = new ContentValues();
            nuevoRegistro.put("nombre", c.getNombre());
            nuevoRegistro.put("apellido", c.getApellido());
            nuevoRegistro.put("telefono", c.getTelefono());
            nuevoRegistro.put("grupo", c.getGrupoSt());
            try
            {
                transaccion = (int) db.insert("Contactos", null, nuevoRegistro);
                Toast.makeText(this, "Inserción realizada con éxito.", Toast.LENGTH_SHORT).show();
                updateAddView(c);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "No se pudo realizar la inserción.", Toast.LENGTH_SHORT).show();
            }
            finally
            {
                db.close();
            }
        }
        return transaccion;
    }

    private int updateContact(Contacto c, Contacto cActualizado) {
        int transaccion = 0;
        ContactosSQLiteHelper contactos = new ContactosSQLiteHelper(this, NOMBRE_DB, null, 1);
        SQLiteDatabase db = contactos.getWritableDatabase();

        if(db != null) {
            ContentValues registroEditar = new ContentValues();
            registroEditar.put("nombre", cActualizado.getNombre());
            registroEditar.put("apellido", cActualizado.getApellido());
            registroEditar.put("telefono", cActualizado.getTelefono());
            registroEditar.put("grupo", cActualizado.getGrupoSt());
            String where = "nombre=? and apellido=? and telefono=?";
            String[] whereArgs = new String[]{c.getNombre(),c.getApellido(),c.getTelefono()};
            try
            {
                transaccion = db.update("Contactos", registroEditar, where, whereArgs);
                Toast.makeText(this, "Actualizacion realizada con éxito.", Toast.LENGTH_SHORT).show();
                updateReplaceView(c,cActualizado);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "No se pudo realizar la actualizacion.", Toast.LENGTH_SHORT).show();
            }
            finally
            {
                db.close();
            }
        }
        return transaccion;
    }

    private int deleteContact(Contacto c) {
        int transaccion = 0;
        ContactosSQLiteHelper contactos = new ContactosSQLiteHelper(this, NOMBRE_DB, null, 1);
        SQLiteDatabase db = contactos.getWritableDatabase();
        String where = "nombre=? and apellido=? and telefono=?";
        String[] whereArgs = new String[]{c.getNombre(),c.getApellido(),c.getTelefono()};

        try
        {
            transaccion = db.delete("Contactos", where ,whereArgs);
            Toast.makeText(this, "Se elimino correctamente.", Toast.LENGTH_SHORT).show();
            updateDeleteView(c);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "No se pudo eliminar.", Toast.LENGTH_SHORT).show();
        }
        finally
        {
            db.close();
        }
        return transaccion;
    }

    private void updateAddView(Contacto c) {
        this.listaFiltrada.add(c);
        this.listaContactos.add(c);
        this.adapter.getData().clear();
        this.adapter.getData().addAll(listaFiltrada);
        this.adapter.notifyDataSetChanged();
    }

    private void updateDeleteView(Contacto c){
        this.listaFiltrada.remove(c);
        this.listaContactos.remove(c);
        this.adapter.getData().clear();
        this.adapter.getData().addAll(listaFiltrada);
        this.adapter.notifyDataSetChanged();
    }

    private void updateReplaceView(Contacto anterior, Contacto nuevo) {
        this.listaFiltrada.add(nuevo);
        this.listaContactos.add(nuevo);
        updateDeleteView(anterior);
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

        this.v = v;

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
        final Contacto c = listaFiltrada.get(contactoID);

        switch (item.getItemId()) {
            case R.id.CtxOpc1:
                String uri = "tel:"+c.getTelefono();
                call(uri);
                return true;
            case R.id.CtxOpc2:
                Toast.makeText(MainActivity.this, c.toString(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.CtxOpc3:
                LayoutInflater li = LayoutInflater.from(this);
                View addContactView = li.inflate(R.layout.new_contact, null);

                final EditText etNombre = (EditText) addContactView.findViewById(R.id.etNombre);
                final EditText etApellido = (EditText) addContactView.findViewById(R.id.etApellido);
                final EditText etTelefono = (EditText) addContactView.findViewById(R.id.etTelefono);
                final EditText etGrupo = (EditText) addContactView.findViewById(R.id.etGrupo);
                etNombre.setText(c.getNombre(), TextView.BufferType.EDITABLE);
                etApellido.setText(c.getApellido(), TextView.BufferType.EDITABLE);
                etTelefono.setText(c.getTelefono(), TextView.BufferType.EDITABLE);
                etGrupo.setText(c.getGrupoSt(), TextView.BufferType.EDITABLE);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);
                alertDialogBuilder.setView(addContactView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        String nombre = etNombre.getText().toString();
                                        String apellido = etApellido.getText().toString();
                                        String telefono = etTelefono.getText().toString();
                                        String grupo = etGrupo.getText().toString();
                                        Contacto cActualizado = new Contacto(nombre,apellido,telefono,grupo);
                                        updateContact(c, cActualizado);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.create();
                alertDialogBuilder.show();
                return true;
            case R.id.CtxOpc4:
                deleteContact(c);
                Snackbar.make(v, "Registro eliminado", Snackbar.LENGTH_LONG)
                        .setAction("Restaurar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                insertContact(c);
                            }
                        })
                        .show();
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
        etFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //here is your code

                adapter.getFilter().filter(s.toString());
//                list.setAdapter(adapter);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void addWidgets()
    {
        this.filtroSpinner = (Spinner) findViewById(R.id.spinner);
        this.list=(ListView) findViewById(R.id.list);
        this.etFiltro = (EditText) findViewById(R.id.editText);
    }

    private void crearDB()
    {
        ContactosSQLiteHelper contactosHelper = new ContactosSQLiteHelper(this, NOMBRE_DB, null, 1);

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
