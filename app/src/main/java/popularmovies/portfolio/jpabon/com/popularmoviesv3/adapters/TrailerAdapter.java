package popularmovies.portfolio.jpabon.com.popularmoviesv3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.entities.Video;

import java.util.ArrayList;

import popularmovies.portfolio.jpabon.com.popularmoviesv3.R;

/**
 * Created by JPabon on 2015-11-05.
 */
public class TrailerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Video> data;

    public ArrayList<Video> getData() {
        return data;
    }

    public void setData(ArrayList<Video> data) {
        this.data = data;
    }

    public TrailerAdapter(Context c) {
        context = c;
        setData(new ArrayList<Video>());
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public Object getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_trailer, parent, false);

        ImageView ivPreview = (ImageView) layout.findViewById(R.id.imgvTrailer);

        TextView tvName = (TextView) layout.findViewById(R.id.txtvTrailerName);
        tvName.setText(getData().get(position).getName());

        TextView tvIdiom = (TextView) layout.findViewById(R.id.txtvTrailerIdiom);
        tvIdiom.setText(getData().get(position).getIso_639_1().toUpperCase());

        TextView tvSize = (TextView) layout.findViewById(R.id.txtvTrailerSize);
        tvSize.setText(getData().get(position).getSize() + " px");

        TextView tvSite = (TextView) layout.findViewById(R.id.txtvTrailerSite);
        tvSite.setText(getData().get(position).getSite());

        return layout;
    }
}