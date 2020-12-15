package coolpharaoh.tee.speicher.tea.timer.views.about;

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
import coolpharaoh.tee.speicher.tea.timer.views.utils.ListRowItem;

/**
 * Created by paseb on 03.11.2016.
 */

class AboutListAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<ListRowItem> items;

    AboutListAdapter(Activity context, List<ListRowItem> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ListRowItem item = items.get(position);

        View vi = convertView;

        if (convertView == null)
            // Pass null because (parent, false) destroys the layout
            vi = inflater.inflate(R.layout.list_single_layout_about, null);

        TextView txtName = vi.findViewById(R.id.textViewListAboutHeading);
        TextView txtSort = vi.findViewById(R.id.textViewListAboutDescription);

        txtName.setText(item.getHeading());
        txtSort.setText(item.getDescription());

        return vi;
    }
}
