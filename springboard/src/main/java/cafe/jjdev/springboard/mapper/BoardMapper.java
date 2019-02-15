package cafe.jjdev.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cafe.jjdev.springboard.vo.Board;
@Mapper
public interface BoardMapper {
	//매개변수로 입력 받은 값과 일치하는 DB의 키값에 해당되는 데이터를 Board vo 에 셋팅 후 주소값을 리턴 한다
	Board selectBoard(int boardNo);

	//리스트 뷰어에 원하는 갯 수 만큼 표현 하기위해 매개변수로 표현할 양 과 보여줄 번호 를 입력 받아
	//List 에 표현할 양만큼 데이터를 담고 그 주소 값을 리턴 한다.
	//2개 이상의 매개변수는 map 으로 묶어서 사용한다. (int currentPage, int ROW_PER_PAGE)
	List<Board> selectBoardList(Map<String,Integer> map);
	
	//DB에 저장 되어있느 정보의 총 갯수를 구한 값을 리턴 받는다.
	int selectBoardCount();

	//DB에 화면에서 입력 받은 값을 저장 하기위한 값이 셋팅된 Board vo 를 매개변수로 입력 해주준다.
	int insertBoard(Board board);

	//DB에 원하는 정보를 삭제 하기위해 필요한 정보를 Board vo 에 셋팅에 입력 해준다.
	//(int boardNo, String boardPw)
	int deleteBoard(int boardNo);
	
	//DB에 화면에서 입력 받은 값을 수정 하기위한 값이 셋팅된 Board vo 를 매개변수로 입력 해주준다.
	int updateBoard(Board board);

	

	

}
