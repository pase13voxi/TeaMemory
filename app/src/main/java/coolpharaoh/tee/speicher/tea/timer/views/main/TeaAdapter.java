package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.utils.LanguageConversation;

class TeaAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Tea> items;

    private final Context context;

    TeaAdapter(Activity context, List<Tea> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Tea item = items.get(position);

        View vi=convertView;

        if (convertView == null)
            // Pass null because (parent, false) destroys the layout
            vi = inflater.inflate(R.layout.tealist_single_layout, null);

        TextView txtName = vi.findViewById(R.id.textViewListTeaName);
        TextView txtVariety = vi.findViewById(R.id.textViewListVarietyOfTea);

        txtName.setText(item.getName());
        if(item.getVariety().equals("")){
            txtVariety.setText(LanguageConversation.convertCodeToVariety("-", context));
        }else{
            txtVariety.setText(LanguageConversation.convertCodeToVariety(item.getVariety(), context));
        }

        return vi;
    }
}
