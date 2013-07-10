
package org.chinamil.ui.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

import org.chinamil.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageDialog extends Activity implements
        OnItemSelectedListener, ViewFactory {
    private ImageSwitcher is;
    private Gallery gallery;
    ArrayList<String> list;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        list = getIntent().getStringArrayListExtra("url");
        setContentView(R.layout.imagedialog);
        is = (ImageSwitcher) findViewById(R.id.switcher);
        is.setFactory(this);
        is.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        is.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemSelectedListener(this);
    }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return i;
    }

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            try {
                i.setImageBitmap(BitmapFactory
                        .decodeStream(new FileInputStream(list.get(position))));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return i;
        }

        private Context mContext;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        try {
            is.setImageDrawable(Drawable.createFromPath(list.get(position)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
