package sticchi.matias.practico7;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sticchi.matias.practico7.entidades.Contacto;

/**
 * Created by MatiasGui on 9/22/2016.
 */
public class MostrarContactoAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    protected List<Contacto> lista;
    private ItemFilter mFilter = new ItemFilter();
    private static LayoutInflater inflater=null;
    private List<Contacto> filteredData;

    public MostrarContactoAdapter(Activity a, List<Contacto> list) {
        activity = a;
        this.lista = list;
        this.filteredData = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return filteredData.size();
    }

    public List getData(){
        return this.filteredData;
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Contacto contacto;
        if(convertView==null)
            vi = inflater.inflate(R.layout.fila_lista, null);

        TextView title = (TextView) vi.findViewById(R.id.title); // title
        TextView telefono = (TextView)vi.findViewById(R.id.items); // items de la pizza
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image


        contacto = (Contacto) filteredData.get(position);

        // Setting all values in listview
        title.setText(contacto.getNombre()+" "+contacto.getApellido());
        telefono.setText(contacto.getTelefono());

        return vi;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Contacto> list = lista;

            int count = list.size();

            ArrayList<Contacto> nlist = new ArrayList<Contacto>();

            Contacto filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getNombre().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            getData().addAll((List) results.values);
            filteredData = (ArrayList<Contacto>) results.values;
            notifyDataSetChanged();
        }

    }
}