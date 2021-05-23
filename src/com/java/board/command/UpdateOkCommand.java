package com.java.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.board.model.BoardDAO;
import com.java.board.model.BoardDTO;
import com.java.command.Command;

public class UpdateOkCommand implements Command {

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.setCharacterEncoding("utf-8");
		
		int pageNumber=Integer.parseInt(request.getParameter("pageNumber"));
		int boardNumber=Integer.parseInt(request.getParameter("boardNumber"));

		BoardDTO boarddto=new BoardDTO();
		boarddto.setBoardNumber(boardNumber);
		boarddto.setWriter(request.getParameter("writer"));
		boarddto.setSubject(request.getParameter("subject"));
		boarddto.setEmail(request.getParameter("email"));
		boarddto.setContent(request.getParameter("content"));
		boarddto.setPassword(request.getParameter("password"));
		
		int check=BoardDAO.getInstance().update(boarddto);
		logger.info(logMsg+"check : "+check);
		
		request.setAttribute("check",check);
		request.setAttribute("pageNumber",pageNumber);
		
		return "/WEB-INF/views/board/updateOk.jsp";
	}

}
