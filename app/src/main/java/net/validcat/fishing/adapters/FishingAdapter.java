package net.validcat.fishing.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.validcat.fishing.FishingItem;
import net.validcat.fishing.R;
import net.validcat.fishing.fragments.ListFragment;
import net.validcat.fishing.ui.RoundedImageView;

public class FishingAdapter extends CursorRecyclerViewAdapter<FishingAdapter.ViewHolder> { //RecyclerView.Adapter<FishingAdapter.ViewHolder> {
	private Context context;
	private ListFragment.IClickListener listener;

	public FishingAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
	}

	@Override
	public FishingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		// create a new view and set the view's size, margins, paddings and layout parameters
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_adapter, parent, false));
	}

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        FishingItem item = FishingItem.createFishingItemFromCursor(context, cursor);

        viewHolder.id = item.getId();
        viewHolder.view.setOnClickListener(viewHolder);
        viewHolder.place.setText(item.getPlace());
        viewHolder.date.setText(item.getDate());
        viewHolder.description.setText(item.getDescription());
//		byte[] photo = item.getPhoto();
//		if(photo!= null) {
//			Bitmap dbPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);
//			viewHolder.photoPreview.setImageBitmap(dbPhoto);
//		}else {
//			Log.d(LOG_TAG,"byte[] photo =" +photo);
//		}
        if(item.getBitmap() != null) {
            viewHolder.photoPreview.setImageBitmap(item.getBitmap());
        }else{
            Bitmap noPhoto = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_no_photo);
            viewHolder.photoPreview.setImageBitmap(noPhoto);
        }
    }

    public void setIClickListener(ListFragment.IClickListener listener) {
		this.listener = listener;
	}

	// Provide a reference to the views for each data item. Complex data items may need more than
	// one view per item, and you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View view;
		public TextView place;
		public TextView date;
		public TextView description;
		public RoundedImageView photoPreview;
		public RoundedImageView weatherPreview;

        long id;

		public ViewHolder(View view) {
			super(view);
            this.view = view;
			place = (TextView) view.findViewById(R.id.tv_adapter_place);
			date = (TextView) view.findViewById(R.id.tv_adapter_date);
			description = (TextView) view.findViewById(R.id.tv_adapter_description);
			photoPreview = (RoundedImageView) view.findViewById(R.id.foto_preview);
			weatherPreview = (RoundedImageView) view.findViewById(R.id.weather_preview);
		}

		@Override
		public void onClick(View v) {
			listener.onItemClicked(id);
		}
	}

}
