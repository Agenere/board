package cafe.jjdev.springboard.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;

//일의단위를 한개로 묶어서 한개가 잘못되면 전부 정지된다
@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	
	//원하는 게시글의 모든 정보를 가져온다
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);		
	}
	//정보를 가공 하는 역할은 service가 한다.
	public Map<String, Object> selectBoardList(int currentPage){
		//1 .. DB에 접속 하기 위해 필요한 정보를 Map을 이용해 가공하여 넘겨주기 위한 작업
		final int ROW_PER_PAGE = 10;//뷰에 표현할 정보의 갯수
		
		Map<String, Integer> map = new HashMap<String,Integer>();
		map.put("currentPage", (currentPage-1)*ROW_PER_PAGE);//현재 보여줄 페이지
		map.put("rowPerPage", ROW_PER_PAGE);
		
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
	
	//입력받은 게시글을 DB에 저장 한다
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
		
	}
	//원하는 게시글을 삭제 한다
	public int removeBoard(int boardNo) {
		return boardMapper.deleteBoard(boardNo);
		
	}
	//원하는 게시글을 수정한다
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
		
	}
}
