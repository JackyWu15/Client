package com.hechuangwu.server.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class Book implements Parcelable {
    private int id;
    private String name;
    private String price;

    public Book(int id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( id );
        dest.writeString( name );
        dest.writeString( price );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book( in );
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
