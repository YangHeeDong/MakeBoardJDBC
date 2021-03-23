package Board;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	static ArticleDao adao = new ArticleDao();
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		while(true) {
			
			if(adao.loginedMember!=null) {
				System.out.println("명령어를 입력해 주세요["+adao.loginedMember.getNickname()+"] :");
			}else {
				System.out.println("명령어를 입력해 주세요 :");
			}
			String cmd = sc.nextLine();	

			if(cmd.equals("add")) {
				//게시물 추가
				if(isLogined(adao.loginedMember)) {
					adao.addArticle();
				}				
				
			}else if(cmd.equals("list")) {
				//게시물 리스트 출력
				if(isLogined(adao.loginedMember)) {
					adao.printArticles();
				}
				
			}else if(cmd.equals("update")) {
				//게시물 업데이트
				if(isLogined(adao.loginedMember)) {
					adao.updateArticle();					
				}
				
			}else if(cmd.equals("delete")) {
				//게시물 삭제
				if(isLogined(adao.loginedMember)) {
					adao.deleteArticle();			
				}
				
			}else if(cmd.equals("read")) {
				//게시물 상세보기
				if(isLogined(adao.loginedMember)) {
					adao.readArticle();			
				}
				
			}else if(cmd.equals("signup")) {
				//회원가입
				if(adao.loginedMember!=null) {
					System.out.println("이미 로그인 되어 있습니다!");
				}else {
					adao.signup();
				}
				
			}else if(cmd.equals("signin")) {
				//로그인
				if(adao.loginedMember!=null) {
					System.out.println("이미 로그인 되어 있습니다!");
				}else {
					adao.signin();
				}
				
			}else if(cmd.equals("logout")) {
				//로그인
				if(adao.loginedMember==null) {
					System.out.println("로그인 되어 있지 않습니다.");
				}else {
					adao.loginedMember=null;
					System.out.println("로그아웃 되었습니다.");
				}
				
			}else if(cmd.equals("exit")) {
				//로그인
				System.out.println("시스템이 종료 됩니다.");
				break;
			}
			else {
				System.out.println("알맞는 명령어를 입력해 주세요");
			}
		}
	}
	
	//로그인 체크
	public static boolean isLogined(Member m) {
		if(m==null) {
			System.out.println("로그인이 필요한 서비스 입니다");
			return false;
		}else {
			return true;
		}
	}
	
	
}
