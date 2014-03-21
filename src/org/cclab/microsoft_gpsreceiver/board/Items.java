package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

public class Items {

	static public class Comment {
		String content;
		Person writer;
		String date;
		
		int writerId;
	}
	
	static public class Board {
		
		int no;
		
		String title;
		String content;
		Person writer;
		
		int kind;
		int commentNum;
		
		// Check. Type
		String date;
		
		// ArrayList<Comment> comments;
		
		public Board() {
			
		}
		
		public Board(String title, String content, Person writer) {
			this.title = title;
			this.content = content;
			this.writer = writer;
		}
	}
	
	static public class Person {
		
		String name;
		String id;
		String hashedId;
		
		String sex;
		
		public Person() {}
		
		public Person(String name, String id, String hashedId, String sex) {
			this.name = name;
			this.id = id;
			this.hashedId = hashedId;
			this.sex = sex;
		}
		
		
	}
	
	
}
