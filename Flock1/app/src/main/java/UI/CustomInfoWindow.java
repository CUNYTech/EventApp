package UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.natia.flock1.GlideApp;
import com.example.natia.flock1.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by napti on 12/29/2017.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private View view;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomInfoWindow(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView title = view.findViewById(R.id.winTitle);
        title.setText(marker.getTitle());

        TextView lines = view.findViewById(R.id.winLines);
        lines.setText(marker.getSnippet());
        Log.d("this is case 1", marker.getSnippet());
        ImageView mta = view.findViewById(R.id.iconInfo);


        switch (marker.getSnippet()){
            case "1":
                Log.d("this is case 1", marker.getSnippet());
                GlideApp.
                        with(view).
                        load(R.mipmap.train_1).
                        into(mta);
                break;
            default:
                Log.d("this is default", marker.getSnippet());
                GlideApp.
                        with(view).
                        load(R.mipmap.mta).
                        into(mta);
        }






        return view;
    }
}
