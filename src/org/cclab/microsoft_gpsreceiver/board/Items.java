package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Items {

	static public class Base {

		String content;
		
		String date;
		Person writer;
		
	}
	
	static public class Comment extends Base {

	}
	
	static public class Board extends Base implements Parcelable {
		
		int no;
		
		String title;
		
		
		int kind;
		int commentNum;
		
		// ArrayList<Comment> comments;
		
		public Board() {
			
		}
		
		public Board(String title, String content, Person writer) {
			this.title = title;
			this.content = content;
			this.writer = writer;
		}
		
		public Board(int no, String title, String content, int kind, String date) {
			this.no = no;
			this.title = title;
			this.content = content;
			this.kind = kind;
			this.date = date;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeInt(no);
			dest.writeString(title);
			dest.writeString(content);
			dest.writeInt(kind);
			dest.writeString(date);
			
			
		}
		
		public static final Parcelable.Creator<Board> CREATOR = new Creator<Board>() {

			@Override
			public Board createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				int no = source.readInt();
				String title = source.readString();
				String content = source.readString();
				int kind = source.readInt();
				String date = source.readString();
				
				
				return new Board(no, title, content, kind, date);
			}

			@Override
			public Board[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Board[size];
			}
			
		};
		
		

	}
	
	
	static public class Person implements Parcelable {
		
		String name;
		String id;
		String hashedId;
		String sex;
		String nickname;
		
		public Person() {}
		
		public Person(String name, String id, String hashedId, String sex, String nickname) {
			this.name = name;
			this.id = id;
			this.hashedId = hashedId;
			this.sex = sex;
			this.nickname = nickname;
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeString(name);
			dest.writeString(id);
			dest.writeString(hashedId);
			dest.writeString(sex);
			dest.writeString(nickname);
		}
		
		public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {

			@Override
			public Person createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				String name = source.readString();
				String id = source.readString();
				String hashedId = source.readString();
				String sex = source.readString();
				String nickname = source.readString();
				
				return new Person(name, id, hashedId, sex, nickname);
			}

			@Override
			public Person[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Person[size];
			}
			
		};
		
		
	
	}
	
	
}
