package jira6.fate.domain.column.service;

import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.repository.BoardRepository;
import jira6.fate.domain.column.dto.ColumnOrderDto;
import jira6.fate.domain.column.dto.ColumnRequestDto;
import jira6.fate.domain.column.dto.ColumnResponseDto;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.column.repository.ColumnRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {

  private final ColumnRepository columnRepository;
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  public void createColumn(Long boardId, ColumnRequestDto columnRequestDto, User user) {
    Board board = findBoard(boardId);

    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    Columns column = Columns.builder()
        .columnName(columnRequestDto.getColumnName())
        .columnOrder(columnRequestDto.getColumnOrder())
        .board(board)
        .build();

    columnRepository.save(column);
  }

  public ColumnResponseDto updateColumn(Long columnId, ColumnRequestDto columnRequestDto, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Columns column = columnRepository.findById(columnId)
        .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
    Board board = column.getBoard();

    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    column.updateColumnName(columnRequestDto.getColumnName());
    Columns updatedColumn = columnRepository.save(column);

    return ColumnResponseDto.builder()
        .id(updatedColumn.getId())
        .columnName(updatedColumn.getColumnName())
        .boardId(updatedColumn.getBoard().getId())
        .build();
  }

  public void deleteColumn(Long columnId, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Columns column = columnRepository.findById(columnId)
        .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
    Board board = column.getBoard();

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

    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    List<Columns> columns = columnRepository.findByBoardId(boardId);
    return columns.stream()
        .map(column -> ColumnResponseDto.builder()
            .id(column.getId())
            .columnName(column.getColumnName())
            .boardId(column.getBoard().getId())
            .build())
        .collect(Collectors.toList());
  }

  public void updateColumnOrder(Long boardId, List<ColumnOrderDto> columnOrderDtos, String username) {
    User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

    if (!hasAccessToBoard(user, board)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_MANAGER);
    }

    columnOrderDtos.forEach(columnOrderDto -> {
      Columns column = columnRepository.findById(columnOrderDto.getColumnId())
          .orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
      column.updateColumnOrder(columnOrderDto.getColumnOrder());
      columnRepository.save(column);
    });
  }

  private boolean hasAccessToBoard(User user, Board board) {
    return user.getUserRole() == UserRole.MANAGER;
  }

  private Board findBoard(Long boardId) {
    return boardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
  }
}
