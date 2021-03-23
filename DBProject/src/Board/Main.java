package Board;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	static ArticleDao adao = new ArticleDao();
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		while(true) {
			
			if(adao.loginedMember!=null) {
				System.out.println("��ɾ �Է��� �ּ���["+adao.loginedMember.getNickname()+"] :");
			}else {
				System.out.println("��ɾ �Է��� �ּ��� :");
			}
			String cmd = sc.nextLine();	

			if(cmd.equals("add")) {
				//�Խù� �߰�
				if(isLogined(adao.loginedMember)) {
					adao.addArticle();
				}				
				
			}else if(cmd.equals("list")) {
				//�Խù� ����Ʈ ���
				if(isLogined(adao.loginedMember)) {
					adao.printArticles();
				}
				
			}else if(cmd.equals("update")) {
				//�Խù� ������Ʈ
				if(isLogined(adao.loginedMember)) {
					adao.updateArticle();					
				}
				
			}else if(cmd.equals("delete")) {
				//�Խù� ����
				if(isLogined(adao.loginedMember)) {
					adao.deleteArticle();			
				}
				
			}else if(cmd.equals("read")) {
				//�Խù� �󼼺���
				if(isLogined(adao.loginedMember)) {
					adao.readArticle();			
				}
				
			}else if(cmd.equals("signup")) {
				//ȸ������
				if(adao.loginedMember!=null) {
					System.out.println("�̹� �α��� �Ǿ� �ֽ��ϴ�!");
				}else {
					adao.signup();
				}
				
			}else if(cmd.equals("signin")) {
				//�α���
				if(adao.loginedMember!=null) {
					System.out.println("�̹� �α��� �Ǿ� �ֽ��ϴ�!");
				}else {
					adao.signin();
				}
				
			}else if(cmd.equals("logout")) {
				//�α���
				if(adao.loginedMember==null) {
					System.out.println("�α��� �Ǿ� ���� �ʽ��ϴ�.");
				}else {
					adao.loginedMember=null;
					System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
				}
				
			}else if(cmd.equals("exit")) {
				//�α���
				System.out.println("�ý����� ���� �˴ϴ�.");
				break;
			}
			else {
				System.out.println("�˸´� ��ɾ �Է��� �ּ���");
			}
		}
	}
	
	//�α��� üũ
	public static boolean isLogined(Member m) {
		if(m==null) {
			System.out.println("�α����� �ʿ��� ���� �Դϴ�");
			return false;
		}else {
			return true;
		}
	}
	
	
}
