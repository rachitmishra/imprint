package in.ceeq.imprint.entity;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class App implements Parcelable {

	public String name;
	public Drawable logo;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public App() {
	}

	protected App(Parcel in) {
	}

	public static final Creator<App> CREATOR = new Creator<App>() {
		public App createFromParcel(Parcel source) {
			return new App(source);
		}

		public App[] newArray(int size) {
			return new App[size];
		}
	};
}
