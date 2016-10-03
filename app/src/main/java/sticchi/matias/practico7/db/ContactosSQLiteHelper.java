package sticchi.matias.practico7.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

/**
 * Created by MatiasGui on 10/3/2016.
 */
public class ContactosSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreateTablaContactos = "CREATE TABLE Contactos (nombre TEXT, apellido TEXT, " +
            "telefono TEXT, grupo TEXT)";

    public ContactosSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(sqlCreateTablaContactos);

        //Insertamos 5 usuarios de ejemplo
        for(int i=0; i<5; i++)
        {
            //Generamos los datos
            String nombre = "Nombre " + i;
            String apellido = "Apellido " + i;
            String telefono = "379-4"+"159753";
            String grupo = "Amigos";

            //Insertamos los datos en la tabla Usuarios
            db.execSQL("INSERT INTO Contactos (nombre, apellido, telefono, grupo) " +
                    "VALUES ('" + nombre + "', '" + apellido + "', '" + telefono + "', '" +
                    grupo +"')");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva)
    {
        db.execSQL("DROP TABLE IF EXISTS Contactos");
        db.execSQL(sqlCreateTablaContactos);
    }
}
