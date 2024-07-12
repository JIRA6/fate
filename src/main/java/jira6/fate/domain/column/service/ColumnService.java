package jira6.fate.domain.column.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.BoardRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.column.repository.ColumnRepository;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.domain.user.entity.UserRole;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColumnService {

  private final ColumnRepository columnRepository;
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  @Autowired
  public ColumnService(ColumnRepository columnRepository, BoardRepository boardRepository, UserRepository userRepository) {
    this.columnRepository = columnRepository;
    this.boardRepository = boardRepository;
    this.userRepository = userRepository;
  }

  public ColumnResponseDto createColumn(ColumnRequestDto columnRequestDto, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Board board = boardRepository.findById(columnRequestDto.getBoardId())
        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    // 권한 확인
    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    if (columnRepository.existsByNameAndBoardId(columnRequestDto.getColumnName(), columnRequestDto.getBoardId())) {
      throw new CustomException(ErrorCode.COLUMN_NOT_UNIQUE);
    }

    Columns column = new Columns();
    column.setColumnName(columnRequestDto.getColumnName());
    column.setColumnOrder(columnRequestDto.getColumnOrder());
    column.setBoard(board);
    Columns savedColumn = columnRepository.save(column);

    ColumnResponseDto responseDto = new ColumnResponseDto();
    responseDto.setId(savedColumn.getId());
    responseDto.setColumnName(savedColumn.getColumnName());
    responseDto.setBoardId(savedColumn.getBoard().getId());

    return responseDto;
  }

  public ColumnResponseDto updateColumn(Long columnId, ColumnRequestDto columnRequestDto, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Columns column = columnRepository.findById(columnId)
        .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
    Board board = column.getBoard();

    // 권한 확인
    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    column.setColumnName(columnRequestDto.getColumnName());
    column.setColumnOrder(columnRequestDto.getColumnOrder());
    Columns updatedColumn = columnRepository.save(column);

    ColumnResponseDto responseDto = new ColumnResponseDto();
    responseDto.setId(updatedColumn.getId());
    responseDto.setColumnName(updatedColumn.getColumnName());
    responseDto.setBoardId(updatedColumn.getBoard().getId());

    return responseDto;
  }

  public void deleteColumn(Long columnId, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Columns column = columnRepository.findById(columnId)
        .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
    Board board = column.getBoard();

    // 권한 확인
    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    columnRepository.delete(column);
  }

  public List<ColumnResponseDto> getColumns(Long boardId, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    // 권한 확인
    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    List<Columns> columns = columnRepository.findByBoardId(boardId);
    return columns.stream()
        .map(column -> {
          ColumnResponseDto dto = new ColumnResponseDto();
          dto.setId(column.getId());
          dto.setColumnName(column.getColumnName());
          dto.setBoardId(column.getBoard().getId());
          return dto;
        })
        .collect(Collectors.toList());
  }

  private boolean hasAccessToBoard(User user, Board board) {
    // 사용자 역할 확인
    return user.getUserRole() == UserRole.MANAGER;
  }
}
