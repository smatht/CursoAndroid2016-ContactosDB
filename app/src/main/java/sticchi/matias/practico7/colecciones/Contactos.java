package sticchi.matias.practico7.colecciones;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sticchi.matias.practico7.db.ContactosSQLiteHelper;
import sticchi.matias.practico7.entidades.Contacto;

/**
 * Created by MatiasGui on 10/3/2016.
 */
public class Contactos {

    public static ArrayList<Contacto> getAll(Context cntx)
    {
        ArrayList<Contacto> contactos = new ArrayList<Contacto>();

        ContactosSQLiteHelper contactosHelper = new ContactosSQLiteHelper(cntx, "db_contactos", null, 1);
        SQLiteDatabase db = contactosHelper.getWritableDatabase();
        Cursor c = null;

        if(db != null)
        {
            String[] campos;
            campos = new String[] {"nombre", "apellido", "telefono", "grupo"};

            try
            {
                c = db.query("Contactos", campos, null, null, null, null, null);

                if (c.moveToFirst())
                {
                    do
                    {
                        Contacto cont = new Contacto(c.getString(0),c.getString(1),c.getString(2),
                                c.getString(3));


                        contactos.add(cont);
                    } while(c.moveToNext());
                }
            }
            catch (Exception e)
            {

            }
            finally
            {
                if(c != null)
                    c.close();

                db.close();
            }

        }

        return contactos;
    }

}
