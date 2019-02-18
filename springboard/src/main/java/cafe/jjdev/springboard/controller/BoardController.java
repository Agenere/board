package cafe.jjdev.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;

@Controller
public class BoardController {
    @Autowired private BoardService boardService;
    
    //글 삭제 화면 요청
    @GetMapping(value="/boardDelete")
    public String boardRemove(Model model, @RequestParam int boardNo) {
    	System.out.println("boardDelete = "+boardNo+" 요청");
        model.addAttribute("boardNo", boardNo);
		return "boardDelete";	
    }
    
     // 글 삭제 요청
    @PostMapping(value="/boardDeleteAction")
    public String boardRemove(Board board) {
    	boardService.removeBoard(board);
    	System.out.println("boardDelete 요청");
        return "redirect:/boardList";
    }
    
    // 글 상세 내용 요청 
    @GetMapping(value="/boardView")
    public String boardView(Model model
                            , @RequestParam int boardNo) {
        Board board = boardService.getBoard(boardNo);
        model.addAttribute("board", board);
        System.out.println("boardView = "+boardNo+" 요청");
        return "boardView";
    }
    
    // 리스트 요청
    @GetMapping(value="/boardList")
    public String boardList(Model model
                            , @RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
    						//currentPage 에 담긴 값이 없을때 1 값을 넣어준다...
    	Map<String, Object> map = boardService.selectBoardList(currentPage);
    	model.addAttribute("list",map.get("list"));
    	model.addAttribute("boardCount",map.get("boardCount"));
    	model.addAttribute("lastPage",map.get("lastPage"));
    	model.addAttribute("pageList", map.get("pageList"));
    	model.addAttribute("currentPage",currentPage);
    	System.out.println("boardList = " +currentPage+" 요청");
        return "boardList";
    }
    
    
    // 입력(액션) 요청
    @PostMapping(value="/boardAddAction")
    public String boardAdd(Board board) {
        boardService.addBoard(board);
        System.out.println("boardAddAction 요청");
        return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
    }
    
    // 입력페이지 요청
    @GetMapping(value="/boardAdd")
    public String boardAdd() {
        System.out.println("boardAdd 요청");
        return "boardAdd";
    }

    //수정페이지 요청
    @GetMapping(value="/boardUpdate")
    public String boardUpdate(Model model, @RequestParam int boardNo) {
    	Board board = boardService.getBoard(boardNo);
    	model.addAttribute("board",board);
    	System.out.println("boardUpdate = "+boardNo+" 요청");
		return "boardUpdate";
   	
    }
    
    //수정 (액션) 요청
    @PostMapping(value="/boardUpdateAction")
    public String boardUpdateAction(Board board) {
    	boardService.modifyBoard(board); 
    	System.out.println("boardUpdateAction 요청");
    	//수정 완료후 수정된 화면을 boardView에서 확인 하기위해 boardNo값을 같이 리턴 한다.
		return "redirect:/boardView?boardNo="+board.getBoardNo();   	
    }
    
}
