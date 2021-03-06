package com.example.a45vd.smartcanteen.database;

import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.a45vd.smartcanteen.R;

        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.List;


public class RedemptionAdapter extends ArrayAdapter<Redemption> {

    public RedemptionAdapter(Activity context, int resource, List<Redemption> rewardListlist) {
        super(context, resource, rewardListlist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Redemption reward = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.redeem_product_record, parent, false);

        TextView tv_product_name, tv_point_required , tv_availability;
        ImageView ivRewardImage;

        tv_product_name = (TextView) rowView.findViewById(R.id.tv_product_name);
        tv_point_required = (TextView) rowView.findViewById(R.id.tv_point_required);
        tv_availability = (TextView) rowView.findViewById(R.id.tv_availability);

        tv_product_name.setText(tv_product_name.getText() + ":" + reward.getProductName());
        tv_point_required.setText(tv_point_required.getText() + ":" + reward.getPointNeeded());
        tv_availability.setText( tv_availability .getText() + ":" + reward.getAmountAvailable());
        return rowView;
    }

    private void getImage(String urlToImage, final ImageView ivImage) {
        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                Bitmap image = null;

                String urlToImage = params[0];
                try {
                    url = new URL(urlToImage);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "Downloading Image...", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();
                ivImage.setImageBitmap(bitmap);
            }
        }
        GetImage gi = new GetImage();
        gi.execute(urlToImage);
    }
}


