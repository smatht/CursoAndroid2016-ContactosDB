package sticchi.matias.practico7;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sticchi.matias.practico7.db.ContactosSQLiteHelper;

public class MainActivity extends AppCompatActivity {
    private TextView mMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWidgets();
        crearDB();
    }

    private void addWidgets()
    {
        mMensaje = (TextView) findViewById(R.id.mje);
    }

    private void crearDB()
    {
        ContactosSQLiteHelper contactosHelper = new ContactosSQLiteHelper(this, "db_contactos", null, 1);

        SQLiteDatabase db = contactosHelper.getWritableDatabase();

        if(db != null)
        {
            mMensaje.setText("Base de datos creada!!");
            db.close();
        }
        else
            mMensaje.setText("No se pudo crear la base de datos!!");
    }
}
