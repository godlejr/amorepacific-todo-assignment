package amorepacific.todo.assignment.model.request;

import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoRequest {

    @Builder
    public TodoRequest(LocalDate date, User user, Priority priority, String task, String description, String status) {
        this.date = date;
        this.user = user;
        this.priority = priority;
        this.task = task;
        this.description = description;
        this.status = status;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Valid
    @Schema(description = "담당자", required = true)
    private User user;

    @Schema(description = "우선순위")
    private Priority priority;

    @NotBlank(message = "업무 제목은 필수 입력 값입니다")
    @Schema(description = "업무 제목", example = "모니터링 시스템 - 예거 연동", required = true)
    private String task;

    @Schema(description = "업무 설명", example = "Background\n" +
            "현재 로그 및 예외처리에 대한 모니터링 시스템 연계가 부재\n" +
            "\n" +
            "Conditions of Satisfaction\n" +
            "쿠버네티스 상의 예거를 띄워 해당 프로젝트에 사이드카로 적용할 예정, 그때 span 값을 설정하여 주입할 예정")
    private String description;

    @Schema(description = "상태", example = "진행중")
    private String status;
}
