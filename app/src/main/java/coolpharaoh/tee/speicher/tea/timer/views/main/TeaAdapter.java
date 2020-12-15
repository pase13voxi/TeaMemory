package coolpharaoh.tee.speicher.tea.timer.views.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.language.LanguageConversation;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;

class TeaAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Tea> items;

    private final Application application;

    TeaAdapter(Activity activity, List<Tea> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.application = activity.getApplication();
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
            vi = inflater.inflate(R.layout.list_single_layout_tea, null);

        TextView txtName = vi.findViewById(R.id.textViewListTeaName);
        TextView txtVariety = vi.findViewById(R.id.textViewListVarietyOfTea);

        txtName.setText(item.getName());
        if(item.getVariety().equals("")){
            txtVariety.setText(LanguageConversation.convertCodeToVariety("-", application));
        }else{
            txtVariety.setText(LanguageConversation.convertCodeToVariety(item.getVariety(), application));
        }

        return vi;
    }
}
