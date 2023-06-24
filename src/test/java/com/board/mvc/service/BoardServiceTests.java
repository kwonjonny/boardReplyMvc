package com.board.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.board.mvc.dto.board.BoardCreateDTO;
import com.board.mvc.dto.board.BoardDTO;
import com.board.mvc.dto.board.BoardListDTO;
import com.board.mvc.dto.board.BoardUpdateDTO;
import com.board.mvc.dto.page.PageRequestDTO;
import com.board.mvc.dto.page.PageResponseDTO;
import com.board.mvc.service.BoardService;

import lombok.extern.log4j.Log4j2;

// 게시판 서비스 테스트 클래스 
@Log4j2
@SpringBootTest
public class BoardServiceTests {
    
    // 의존성 주입 
    @Autowired
    private BoardService boardService;

    private static final String TEST_TITLE = "JunitServiceTest";
    private static final String TEST_WRITER = "JunitServiceTest";
    private static final String TEST_CONTENT = "JunitServiceTest";
    private static final Long TEST_TNO = 1L;

    // BeforeEach 사용을 위한 BoardCreateDTO , BoardUpdateDTO 정의 
    private BoardCreateDTO boardCreateDTO;
    private BoardUpdateDTO boardUpdateDTO;

    // BoardService Create Test Set Up
    // BoardService Update TEst Set Up
    @BeforeEach
    public void setUp() {
        boardCreateDTO = BoardCreateDTO.builder()
        .title(TEST_TITLE)
        .writer(TEST_WRITER)
        .content(TEST_CONTENT)
        .build();

        boardUpdateDTO = BoardUpdateDTO.builder()
        .tno(TEST_TNO)
        .title(TEST_TITLE)
        .writer(TEST_WRITER)
        .content(TEST_CONTENT)
        .build();
    }

    // Create BoardSerivce Test
    @Test
    @Transactional
    @DisplayName("생성 게시판 서비스 테스트")
    public void createBoardServiceTest() {
        log.info("========== Start Create Board Service Test");
        int insertCount = boardService.createBoard(boardCreateDTO);
        Assertions.assertEquals(1, insertCount, "Create Board Service Should Be Successful");
        log.info("========== End Create Board Service Test");
    }

    // Read BoardService Test
    @Test
    @Transactional
    @DisplayName("조회 게시판 서비스 테스트")
    public void readBoardServiceTest() {
        log.info("========== Start Read Board Service Test");
        BoardDTO readBoardDTO = boardService.readBoard(TEST_TNO);
        log.info(readBoardDTO);
        log.info("========== End Read Board Service Test ==========");
    }

    // Delete BoardService Test
    @Test
    @Transactional
    @DisplayName("삭제 게시판 서비스 테스트")
    public void deleteBoardServiceTest() {
        log.info("========== Start Delete Board Service Test ==========");
        boardService.deleteBoard(TEST_TNO);
        BoardDTO boardDeletedDTO = boardService.readBoard(TEST_TNO);
        Assertions.assertNull(boardDeletedDTO, "Delete Board Service Should Be Null");
        log.info("========== End Read Board Service Test ==========");
    }

    // Update BoardService Test 
    @Test
    @Transactional
    @DisplayName("업데이트 게시판 서비스 테스트")
    public void updateBoardServiceTest() {
        log.info("========== Start Update Board Service Test ==========");
        boardService.updateBoard(boardUpdateDTO);
        BoardDTO boardUpdatedDTO = boardService.readBoard(TEST_TNO);
         assertNotNull(boardUpdatedDTO, "Updated Board should not be null");
        assertEquals(TEST_TITLE, boardUpdatedDTO.getTitle(), "Title should be updated");
        log.info("========== End Update Board Service Test ==========");
    }

    // List BoardService Test 
    @Test
    @Transactional
    @DisplayName("리스트 게시판 서비스 테스트")
    public void listBoardServiceTest() {
          log.info("========== Start Board Service list =========");
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .searchType("title")
            .keyword("권성준님")
            .build();
        PageResponseDTO<BoardListDTO> response = boardService.listBoard(pageRequestDTO); 

        // list 로그 
        log.info(response);
        // 가져온 게시물들이 검색 조건에 부합하는지 확인
        for (BoardListDTO board : response.getList()) {
        assertTrue(board.getTitle().contains("권성준님"), "Title should contain '권성준님'");
        }
        log.info("========== End Board Service List ==========");
    }
}