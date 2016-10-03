package sticchi.matias.practico7;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import sticchi.matias.practico7.db.ContactosSQLiteHelper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//    private TextView mMensaje;
    private Spinner filtroSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWidgets();
        addListeners();
        addAdapters();
        crearDB();
    }

    private void addAdapters() {
        ArrayAdapter<CharSequence> adtFiltro = ArrayAdapter.createFromResource(
                this, R.array.filtro, android.R.layout.simple_spinner_item);
        adtFiltro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.filtroSpinner.setAdapter(adtFiltro);

    }

    private void addListeners() {
        this.filtroSpinner.setOnItemSelectedListener(this);
    }

    private void addWidgets()
    {
//        mMensaje = (TextView) findViewById(R.id.mje);
        filtroSpinner = (Spinner) findViewById(R.id.spinner);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
