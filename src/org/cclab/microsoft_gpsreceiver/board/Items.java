package org.cclab.microsoft_gpsreceiver.board;

import java.util.ArrayList;

public class Items {

	static public class Comment {
		String contents;
		String writer;
		
		int writerId;
	}
	
	static public class Board {
		int num;
		
		String title;
		String contents;
		
		String writer;
		int writerId;
		
		ArrayList<Comment> comments;
		
		public Board() {
			
		}
		
		public Board(String title, String contents, String writer) {
			this.title = title;
			this.contents = contents;
			this.writer = writer;
		}
	}
	
	
}
