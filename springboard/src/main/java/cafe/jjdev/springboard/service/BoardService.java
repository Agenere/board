package cafe.jjdev.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.mapper.BoardfileMapper;
import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.BoardRequest;
import cafe.jjdev.springboard.vo.Boardfile;

//일의단위를 한개로 묶어서 한개가 잘못되면 전부 정지된다
@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private BoardfileMapper boardfileMapper;
	
	//원하는 게시글의 모든 정보를 가져온다
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);		
	}
	//정보를 가공 하는 역할은 service가 한다.
	public Map<String, Object> selectBoardList(int currentPage){
		//1 .. DB에 접속 하기 위해 필요한 정보를 Map을 이용해 가공하여 넘겨주기 위한 작업
		final int ROW_PER_PAGE = 10;//뷰에 표현할 정보의 갯수
		
		Map<String, Integer> map = new HashMap<String,Integer>();
		map.put("currentPage", (currentPage-1)*ROW_PER_PAGE);//현재 보여줄 페이지의 첫번째 게시글
		map.put("rowPerPage", ROW_PER_PAGE);//부터 10개 출력
		
		//2... DB에서 받아온 값과 컨트롤러 나 뷰에서 사용할 (필요한 ) 값들을 가공하여 리턴 해준다.
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int) Math.ceil(boardCount/ROW_PER_PAGE);//올림 해주는거 아닌가??
		// 0으로 나누어 떨어지지 않을때 마지막 페이지를 보기 위해선 +1 을 해준다
		if(boardCount%ROW_PER_PAGE != 0) {++lastPage;}
		
		//현재 페이지를 중간에 보여주기 위한 조건을 걸고 그외는 1부터 출력
		//view 에서 lastPage 이상의 숫자는 if로 노출시키지 않을 예정
		int[] pageList = new int[10];
		for (int i = 0 ; i<10 ; i++) {
			if(currentPage > ROW_PER_PAGE/2) {
				pageList[i] = i+(currentPage-((ROW_PER_PAGE/2)-1));//5번째 칸에 현제 페이지를 노출시키기위해서
			}else {
				pageList[i] = i+1;
			}
		}		
		//view 에서 필요한 값을 모두 보내기위해 Map에 모두 상주 시킨후 리턴시킨다.
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		returnMap.put("pageList", pageList);
		return returnMap;		
	}
	
	//현제 등록된 모든 게시물의 숫자를 가져온다.
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
		
	}
	
	//입력받은 게시글을 DB에 저장 한다 //Board vo와 file의 저장된 주소 도 받는다
	public void addBoard(BoardRequest boardRequest, String path) throws IllegalStateException, IOException {
		/*
		 * 1.Boardf를 분리 : board ,file , file 정보
		 * 2. board -> boardVo
		 * 3.file -> boardfileVo
		 * 4.file - > path를 이용해 물리적 장치에 저장
		 */	
		//1.
		Board board = new Board();
		board.setBoardContent(boardRequest.getBoardContent());
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardTitle(boardRequest.getBoardTitle());
		board.setBoardUser(boardRequest.getBoardUser());
		
		boardMapper.insertBoard(board);//인서트한 객체의 값을 최신화 한다.
		System.out.println(board.getBoardNo()); 
		
		//2.		
		List<MultipartFile> files = boardRequest.getFiles();//멀티파트파일 형식으로 담긴 배열을 추출
		for(MultipartFile f: files) {//배열에 담긴 값들을 Boardfile vo에 맞게 가공 하고 셋팅 한다
			Boardfile boardfile = new Boardfile();
			boardfile.setBoardNo(board.getBoardNo());
			//f - > boardfile
			boardfile.setFileSize(f.getSize()); // 파일 용량
			boardfile.setFileType(f.getContentType());// 파일 타입
			
			String originalFilename = f.getOriginalFilename(); // 파일 이름 가져오기
			int i = originalFilename.lastIndexOf("."); //파일이름중 . 까지의 문자숫자 가져오기
			String ext = originalFilename.substring(i+1); //. 다음의 문자만 가져오기(확장자 추출)
			boardfile.setFileExt(ext);//파일 확장자 입력하기
			
			String filename = UUID.randomUUID().toString();//16진수 랜덤을 문자열로 바꾼다
			boardfile.setFileName(filename);//랜덤 문자열로 파일이름 셋팅
			//전체작업이 롤백되면 파일삭제작업 직접!
			//파일 저장
			f.transferTo(new File (path+"/"+filename+"."+ext));//컨트롤러에서 받아온 주소에 저장할 파일의 이름을 추출한 이름들로 조합해서 저장 한다.
			
			boardfileMapper.addBoardfile(boardfile);
		}
		
		
	//	boardMapper.insertBoard(board);
	//	boardFileMapper.insertBard(boardfile);
		return ;
		
	}
	//원하는 게시글을 삭제 한다
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
		
	}
	//원하는 게시글을 수정한다
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
		
	}
}
