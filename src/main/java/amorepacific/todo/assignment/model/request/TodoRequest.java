package amorepacific.todo.assignment.model.request;

import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TodoRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2023-03-31")
    private LocalDate date;

    @Schema(description = "담당자", example = "{ 1, '김동주'}", required = true)
    private User user;

    @Schema(description = "중요도 및 우선 순위 정책", example = "{'B', 0}")
    private Priority priority;

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
