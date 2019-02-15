package cafe.jjdev.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;

@Controller
public class BoardController {
    @Autowired private BoardService boardService;
    
 // 글 삭제 폼 요청(비밀번호 입력 폼)
    @GetMapping(value="/boardRemove")
    public String boardRemove(@RequestParam(value="boardNo", required=true) int boardNo) {
        return "boardRemove";
    }
    // 글 삭제 요청
    @RequestMapping(value="/boardAdd", method = RequestMethod.POST)
    public String boardRemove(Board board) {
    	boardService.removeBoard(board);
        return "redirect:/boardList";
    }
    
    // 글 상세 내용 요청 
    @GetMapping(value="/boardView")
    public String boardView(Model model
                            , @RequestParam(value="boardNo", required=true) int boardNo) {
        Board board = boardService.getBoard(boardNo);
        model.addAttribute("board", board);
        return "/boardView";
    }
    
    // 리스트 요청
    @GetMapping(value="/boardList")
    public String boardList(Model model
                            , @RequestParam(value="currentPage", required=false, defaultValue="0") int currentPage) {
    	Map<String, Object> map = boardService.selectBoardList(currentPage);
    	model.addAttribute("list",map.get("list"));
    	model.addAttribute("boardCount",map.get("boardCount"));
    	model.addAttribute("lastPage",map.get("lastPage"));
    	model.addAttribute("currentPage",currentPage);
    	System.out.println("boardList 요청");
        return "boardList";
    }
    
    
    // 입력(액션) 요청
    @RequestMapping(value="/boardAddAction", method = RequestMethod.POST)
    public String boardAdd(Board board) {
        boardService.addBoard(board);
        System.out.println("boardAdd 액션 요청");
        return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
    }
    
    // 입력페이지 요청
    @GetMapping(value="/boardAdd")
    public String boardAdd() {
        System.out.println("boardAdd 폼 요청");
        return "boardAdd";
    }

}
