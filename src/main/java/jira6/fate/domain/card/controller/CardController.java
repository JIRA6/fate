package jira6.fate.domain.card.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.dto.CardUpdateRequestDto;
import jira6.fate.domain.card.service.CardService;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param requestDto  : 생성할 카드의 정보
     * @param userDetails : 카드를 생성하려는 사용자의 정보
     * @return : 카드 생성 성공 상태 코드 및 메시지 반환
     */
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<MessageResponse> createCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @RequestBody CardCreateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.createCard(columnId, requestDto, userDetails.getUser());
        MessageResponse response = new MessageResponse(201, "카드 생성 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 카드 수정 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param cardId      : 카드 아이디
     * @param requestDto  : 수정할 카드의 정보
     * @param userDetails : 카드를 수정하려는 사용자의 정보
     * @return : 카드 수정 성공 상태 코드 및 메시지 반환
     */
    @PutMapping("/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<MessageResponse> updateCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @Min(1) @PathVariable Long cardId,
        @RequestBody CardUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.updateCard(columnId, cardId, requestDto, userDetails.getUser());
        MessageResponse response = new MessageResponse(200, "카드 수정 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 카드 삭제 ( 인가 필요 )
     *
     * @param columnId    : 컬럼 아이디
     * @param cardId      : 카드 아이디
     * @param userDetails : 카드를 삭제하려는 사용자의 정보
     * @return : 카드 삭제 성공 상태 코드 및 메시지 반환
     */
    @DeleteMapping("/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<MessageResponse> deleteCard(
        @Min(1) @PathVariable Long columnId,
        @Valid @Min(1) @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(columnId, cardId, userDetails.getUser());
        MessageResponse response = new MessageResponse(204, "카드 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
