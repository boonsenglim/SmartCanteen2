package com.example.a45vd.smartcanteen.database;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Color;
        import android.support.constraint.ConstraintLayout;
        import android.support.v4.content.ContextCompat;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import com.example.a45vd.smartcanteen.R;
        import com.example.a45vd.smartcanteen.database.History;
        import com.example.a45vd.smartcanteen.database.Redemption;

        import java.util.List;



public class HistoryAdapter extends ArrayAdapter<History>{
    public HistoryAdapter(Activity context, int resource, List<History> list) {
        super(context, resource, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        History member = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.history_record, parent, false);

        TextView tvItemName, tvMercID, tvRedeemDate, tvUserName;


        tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
        tvMercID = (TextView) rowView.findViewById(R.id.tvMercID);
        tvRedeemDate = (TextView) rowView.findViewById(R.id.tvRedeemDate);
        tvUserName = (TextView)rowView.findViewById(R.id.tvUserName);

        tvItemName.setText(History.getItemName());
       // tvMercID.setText(History.getSeller());
        tvUserName.setText(History.getWalletID());
        tvRedeemDate.setText(History.getTransactionDate());

//       ConstraintLayout layout = (ConstraintLayout) rowView.findViewById(R.id.layoutTransaction);

 /*       if (History.getBuyerID().equals(MainActivity.username))
            layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOut));*/

        return rowView;
    }

}
