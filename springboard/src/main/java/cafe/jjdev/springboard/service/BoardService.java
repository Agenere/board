package cafe.jjdev.springboard.service;

import java.util.HashMap;
import java.util.List;
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
	
	
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);		
	}
	//정보를 가공 하는 역할은 service가 한다.
	public Map<String, Object> selectBoardList(int currentPage){
		//1 .. DB에 접속 하기 위해 필요한 정보를 Map을 이용해 가공하여 넘겨주기 위한 작업
		final int ROW_PER_PAGE = 10;//뷰에 표현할 정보의 갯수
		
		Map<String, Integer> map = new HashMap<String,Integer>();
		map.put("currentPage", currentPage);//현재 보여줄 페이지
		map.put("rowPerPage", ROW_PER_PAGE);
		
		//2... DB에서 받아온 값과 컨트롤러 나 뷰에서 사용할 (필요한 ) 값들을 가공하여 리턴 해준다.
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int)(Math.ceil(boardCount/ROW_PER_PAGE));
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		return returnMap;
		
	}
	
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
		
	}
	
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
		
	}
	
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
		
	}
	
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
		
	}
}
