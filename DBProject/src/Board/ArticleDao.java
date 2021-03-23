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
	
	//DB ���� ����
	String url = "jdbc:mysql://127.0.0.1:3306/t1?serverTimezone=UTC";
	String id = "root";
	String pw = "";
	
	//Ŀ�ؼ� ���
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		if(conn==null) {
			//DB ����
			Class.forName("com.mysql.cj.jdbc.Driver"); //mysql ����̹� ã��
			conn=DriverManager.getConnection(url, id, pw); //����̹��� ���� Ư�� DBMS ����
		}
		return conn;
	}
	
	//�Խù� �߰� �޼���
	public void addArticle() throws ClassNotFoundException, SQLException {
		Statement stmt= getConnection().createStatement();
		
		System.out.println("������ �Է��� �ּ���");
		String title = sc.nextLine();
		System.out.println("������ �Է��� �ּ���");
		String body = sc.nextLine();
		
		String sql = "Insert into article set title ='"+title+"',`body`='"+body+"',regDate = Now(),mid="+loginedMember.getId()+",hit="+0;
		stmt.executeUpdate(sql);
		
		System.out.println("�߰� �Ǿ����ϴ�.");
		if(stmt!=null) {
			stmt.close();
		}
	}
	//�Խù� ��� ����Ʈ ���
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

	//�Խù� ����Ʈ ���
	public void printArticles() throws ClassNotFoundException, SQLException {
		
		Statement stmt = getConnection().createStatement();
		String sql = "select *from article";
		ResultSet rs = stmt.executeQuery(sql);
		
		while(rs.next()) {
			System.out.println("��ȣ : "+rs.getInt("id"));
			System.out.println("���� : "+rs.getString("title"));
			System.out.println("===============================");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		if(rs!=null) {
			rs.close();
		}
		
	}

	//�Խù� ����
	public void updateArticle() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("������ �Խù� ��ȣ�� �Է��� �ּ���");
		int targetNum = Integer.parseInt(sc.nextLine());
		if(isExistArticleById(targetNum)) {
			System.out.println("�� ������ �Է��� �ּ���");
			String title = sc.nextLine();
			System.out.println("�� ������ �Է��� �ּ���");
			String body = sc.nextLine();
			
			String sql = "update article set title='"+title+"', `body`='"+body+"' where id="+targetNum;
			stmt.executeUpdate(sql);
			System.out.println("���� �Ǿ����ϴ�.");
		}else {
			System.out.println("���� �Խù� ��ȣ �Դϴ�.");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
		
	}
	
	//�Խù� ��ȣ�� �Խù� �ִ��� üũ
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

	//�Խù� ���� �޼���
	public void deleteArticle() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("������ �Խù� ��ȣ�� �Է��� �ּ���");
		int targetNum = Integer.parseInt(sc.nextLine());
		if(isExistArticleById(targetNum)) {
			String sql = "delete from article where id="+targetNum;
			stmt.executeUpdate(sql);
			deleteReplyByArticleId(targetNum);
			System.out.println("���� �Ǿ����ϴ�.");
		}else {
			System.out.println("���� �Խù� ��ȣ �Դϴ�.");
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

	//�Խù� �󼼺���
	public void readArticle() throws ClassNotFoundException, SQLException {
		System.out.println("�󼼺����� �Խù� ��ȣ�� �Է��� �ּ���");
		int targetNum = Integer.parseInt(sc.nextLine());
		
		if(isExistArticleById(targetNum)) {
			Statement stmt = getConnection().createStatement();
			
			String sql = "select * from article where id="+targetNum;

			ResultSet rs =stmt.executeQuery(sql);
			
			ArrayList<Reply> re = getRepliesByArticleId(targetNum);
			
			if(rs.next()) {
				System.out.println("��ȣ : "+rs.getInt("id"));
				System.out.println("���� : "+rs.getString("title"));
				System.out.println("���� : "+rs.getString("body"));
				System.out.println("�ۼ��ð� : "+rs.getString("regDate"));
				System.out.println("============���=================");
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
			System.out.println("���� �Խù� ��ȣ �Դϴ�.");
		}
		
	}

	//ȸ������
	public void signup() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("���̵� �Է��� �ּ���");
		String loginId = sc.nextLine();
		System.out.println("��й�ȣ�� �Է��� �ּ���");
		String loginPw = sc.nextLine();
		System.out.println("�г������� �Է��� �ּ���");
		String nickname = sc.nextLine();
		
		Member m = new Member();
		m.setLoginId(loginId);
		m.setLoginPw(loginPw);
		m.setNickname(nickname);
		
		if(isExistMemberId(m)) {
			String sql = "insert into `member` set loginId='"+m.getLoginId()+"', loginPw='"+m.getLoginPw()+"'"
					+ ", nickname='"+m.getNickname()+"', regDate = now()";
			
			stmt.executeUpdate(sql);
			System.out.println("�߰��Ǿ����ϴ�.");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
	}
	
	//�α���
	public void signin() throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();
		
		System.out.println("���̵� �Է��� �ּ���");
		String loginId = sc.nextLine();
		System.out.println("��й�ȣ�� �Է��� �ּ���");
		String loginPw = sc.nextLine();
		
		String sql = "select * from `member` where loginId='"+loginId+"' and loginPw ='"+loginPw+"'";
		
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			loginedMember = new Member();
			loginedMember.setLoginId(rs.getString("loginId"));
			loginedMember.setLoginPw(rs.getString("loginPw"));
			loginedMember.setNickname(rs.getString("nickname"));
			loginedMember.setId(rs.getInt("id"));
			System.out.println(loginedMember.getNickname()+"�� ȯ���մϴ�!");
		}
		
		if(stmt!=null) {
			stmt.close();
		}
	}
	
	//���̵� �г��� �ߺ�üũ
	public boolean isExistMemberId(Member m) throws ClassNotFoundException, SQLException {
		Statement stmt = getConnection().createStatement();

		String sql = "select * from `member` where loginId ='"+m.getLoginId()+"' or nickname ='"+m.getNickname()+"'";		
		
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			System.out.println("���̵� �г����� �ߺ��Ǿ� �ֽ��ϴ�.");
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
