package jira6.fate.domain.card.controller;


import jakarta.validation.constraints.Min;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.service.CardService;
import jira6.fate.global.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성 ( 인가 필요 )
     *
     * @param columnId   : 컬럼 아이디
     * @param requestDto : 생성할 카드의 정보
     * @return : 카드 생성 성공 상태 코드 및 메시지 반환
     */
    @PostMapping("/columns/{columnId}/cards")
    public ResponseEntity<MessageResponse> createCard(
        @Min(1) @PathVariable Long columnId,
        @RequestBody CardCreateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.createCard(columnId, requestDto, userDetails.getUser());
        MessageResponse response = new MessageResponse(201, "카드 생성 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
