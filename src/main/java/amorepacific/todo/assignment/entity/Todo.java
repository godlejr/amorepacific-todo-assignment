package amorepacific.todo.assignment.entity;

import amorepacific.todo.assignment.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends AbstractEntity {

    @Builder
    public Todo(LocalDate date, User user, Priority priority, String task, String description, String status) {
        this.date = date;
        this.user = user;
        this.priority = priority;
        this.task = task;
        this.description = description;
        this.status = status;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "날짜", example = "2023-03-31", required = true)
    private LocalDate date;

    private User user;

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

    //위임 표시를 위한 변수
    @Schema(description = "태그", example = "위임(담당자 안지슬) 1")
    private String tag;
}
