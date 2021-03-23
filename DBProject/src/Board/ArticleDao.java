package Board;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class ArticleDao {
	
	Connection conn =null;
	Scanner sc = new Scanner(System.in);
	
	static Member loginedMember = null;
	
	//DB 접속 정보
	String url = "jdbc:mysql://127.0.0.1:3306/t1?serverTimezone=UTC";
	String id = "root";
	String pw = "";
	
	//커넥션 얻기
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		if(conn==null) {
			//DB 접속
			Class.forName("com.mysql.cj.jdbc.Driver"); //mysql 드라이버 찾기
			conn=DriverManager.getConnection(url, id, pw); //드라이버를 통해 특정 DBMS 접속
		}
		return conn;
	}
	
	//게시물 추가 메서드
	public void addArticle() throws ClassNotFoundException, SQLException {
		Statement stmt= getConnection().createStatement();
		
		System.out.println("제목을 입력해 주세요");
		String title = sc.nextLine();
		System.out.println("내용을 입력해 주세요");
		String body = sc.nextLine();
		
		String sql = "Insert into article set title ='"+title+"',`body`='"+body+"',regDate = Now(),mid="+loginedMember.getId()+",hit="+0;
		stmt.executeUpdate(sql);
		
		System.out.println("추가 되었습니다.");
		if(stmt!=null) {
			stmt.close();
		}
	}
	//게시물 댓글 리스트 얻기
	public ArrayList<Reply> getRepliesByArticleId(int aid) throws ClassNotFoundException, SQLException {
		Statement stmt= getConnection().createStatement();

		String sql = "select r.*, m.nickname from reply r inner join `member` m on r.mid = m.id where r.aid ="+aid;
		ResultSet rs = stmt.executeQuery(sql);
		ArrayList<Reply> Replies = new ArrayList<Reply>();
		
		while(rs.next()) {
			Reply re = new Reply();
			re.setAid(rs.getInt("aid"));
			re.setBody(rs.getString("body"));
			re.setMid(rs.getInt("mid"));
			re.setNickname(rs.getString("nickname"));
			re.setRegDate(rs.getString("regDate"));
			
			Replies.add(re);
		}
		if(stmt!=null) {
			stmt.close();
		}
		if(rs!=null) {
			rs.close();
		}
		
		return Replies;
	}

	//게시물 리스트 출력
	public void printArticles() throws ClassNotFoundException, SQLException {
		
		Statement stmt = getConnection().createStatement();
		String sql = "select *from article";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			System.out.println("번호 : "+rs.getInt("id"));
			System.out.println("제목 : "+rs.getString("title"));
			System.out.println("===============================");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		if(rs!=null) {
			rs.close();
		}
		
	}

	//게시물 수정
	public void updateArticle() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("수정할 게시물 번호를 입력해 주세요");
		int targetNum = Integer.parseInt(sc.nextLine());
		if(isExistArticleById(targetNum)) {
			System.out.println("새 제목을 입력해 주세요");
			String title = sc.nextLine();
			System.out.println("새 내용을 입력해 주세요");
			String body = sc.nextLine();
			
			String sql = "update article set title='"+title+"', `body`='"+body+"' where id="+targetNum;
			stmt.executeUpdate(sql);
			System.out.println("수정 되었습니다.");
		}else {
			System.out.println("없는 게시물 번호 입니다.");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		
	}
	
	//게시물 번호로 게시물 있는지 체크
	public boolean isExistArticleById(int targetNum) throws SQLException, ClassNotFoundException {
		Statement stmt=getConnection().createStatement();
		
		String sql = "select * from article where id ="+targetNum;
		
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			return true;
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		
		if(rs!=null) {
			rs.close();
		}
		return false;
	}

	//게시물 삭제 메서드
	public void deleteArticle() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("삭제할 게시물 번호를 입력해 주세요");
		int targetNum = Integer.parseInt(sc.nextLine());
		if(isExistArticleById(targetNum)) {
			String sql = "delete from article where id="+targetNum;
			stmt.executeUpdate(sql);
			deleteReplyByArticleId(targetNum);
			System.out.println("삭제 되었습니다.");
		}else {
			System.out.println("없는 게시물 번호 입니다.");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		
	}
	
	public void deleteReplyByArticleId(int aid) throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();

		String sql = "delete from reply where aid="+aid;
		stmt.executeUpdate(sql);
			
		if(stmt!=null) {
			stmt.close();
		}
		
	}

	//게시물 상세보기
	public void readArticle() throws ClassNotFoundException, SQLException {
		System.out.println("상세보기할 게시물 번호를 입력해 주세요");
		int targetNum = Integer.parseInt(sc.nextLine());
		
		if(isExistArticleById(targetNum)) {
			Statement stmt = getConnection().createStatement();
			
			String sql = "select * from article where id="+targetNum;

			ResultSet rs =stmt.executeQuery(sql);
			
			ArrayList<Reply> re = getRepliesByArticleId(targetNum);
			
			if(rs.next()) {
				System.out.println("번호 : "+rs.getInt("id"));
				System.out.println("제목 : "+rs.getString("title"));
				System.out.println("내용 : "+rs.getString("body"));
				System.out.println("작성시간 : "+rs.getString("regDate"));
				System.out.println("============댓글=================");
				for(int i = 0 ; i<re.size();i++) {
					System.out.println(re.get(i).getNickname());
					System.out.println(re.get(i).getBody());
					System.out.println(re.get(i).getRegDate());
					System.out.println("============================");
				}
				
			}
			if(stmt!=null) {
				stmt.close();
			}
			
			if(rs!=null) {
				rs.close();
			}
		}else{
			System.out.println("없는 게시물 번호 입니다.");
		}
		
	}

	//회원가입
	public void signup() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("아이디를 입력해 주세요");
		String loginId = sc.nextLine();
		System.out.println("비밀번호를 입력해 주세요");
		String loginPw = sc.nextLine();
		System.out.println("닉네임을를 입력해 주세요");
		String nickname = sc.nextLine();
		
		Member m = new Member();
		m.setLoginId(loginId);
		m.setLoginPw(loginPw);
		m.setNickname(nickname);
		
		if(isExistMemberId(m)) {
			String sql = "insert into `member` set loginId='"+m.getLoginId()+"', loginPw='"+m.getLoginPw()+"'"
					+ ", nickname='"+m.getNickname()+"', regDate = now()";
			
			stmt.executeUpdate(sql);
			System.out.println("추가되었습니다.");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
	}
	
	//로그인
	public void signin() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("아이디를 입력해 주세요");
		String loginId = sc.nextLine();
		System.out.println("비밀번호를 입력해 주세요");
		String loginPw = sc.nextLine();
		
		String sql = "select * from `member` where loginId='"+loginId+"' and loginPw ='"+loginPw+"'";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			loginedMember = new Member();
			loginedMember.setLoginId(rs.getString("loginId"));
			loginedMember.setLoginPw(rs.getString("loginPw"));
			loginedMember.setNickname(rs.getString("nickname"));
			loginedMember.setId(rs.getInt("id"));
			System.out.println(loginedMember.getNickname()+"님 환영합니다!");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
	}
	
	//아이디 닉네임 중복체크
	public boolean isExistMemberId(Member m) throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();

		String sql = "select * from `member` where loginId ='"+m.getLoginId()+"' or nickname ='"+m.getNickname()+"'";		
		
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			System.out.println("아이디나 닉네임이 중복되어 있습니다.");
			return false;
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		
		if(rs!=null) {
			rs.close();
		}
		
		return true;
	}
}
