package amorepacific.todo.assignment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("Amorepacific Assignment (김동주)")
                .description("## Todo 서비스 API 명세서" + "\n\n\n\n" +
                        "" +
                        "#### [API 호출 확인 사항]\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "- **사전 데이터 셋**\n" +
                        "    - 과제 정의서 기반 규칙 기반\n" +
                        "        - 초기값으로 임의의 이름으로 5 명의 프로필이 생성되어 있다\n" +
                        "- **순서**\n" +
                        "    1. UserController \n" +
                        "        - 프로필 리스트 조회\n" +
                        "            - Dataset 확인 (프로필 5명)\n" +
                        "    2. TodoController  \n" +
                        "        - Todo 생성, 조회, 변경, 삭제\n" +
                        "            - 입력 format 주의\n" +
                        "                - Required 정의, Validation 및 Exception 적용 완료\n" +
                        "                - Priortiy 중요도 입력 확인 필요 (S,A,B,C,D 중 하나) \n" +
                        "                ***(정상적인 Priority Object가 전달되는 상황을 가정)***\n" +
                        "                - 특히, User에서 name은 별도 확인 필요 \n" +
                        "                ***(ID 기반 동작, ID만 정상이면 name은 별도로 오기입에 대한 정정은 하지 않은 상황 - 정상적인 Object가 전달되는 상황을 가정)***\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "" +
                        "#### **[중요도 및 우선 순위 정책]**\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "1. **중요도**\n" +
                        "    - 같은 담당자, 동일 날짜 내에 중복, 누락이 가능함\n" +
                        "2.  **순서**\n" +
                        "    - 같은 담당자, 동일 날짜, 동일 중요도 내에서 우선순위를 나타냄\n" +
                        "    - 같은 담당자, 동일 날짜, 동일 중요도 내에서 중복과 누락이 없어야 함\n" +
                        "\n" +
                        "- **'같은 담당자'를 정책의 기준으로 넣은 이유**\n" +
                        "    1.  과제 정의서의 Todo 생성 규칙을 기반으로 함 (같은 담당자의 동일 날짜 처음 생성되면 초기값은 B0 를 설정 함)\n" +
                        "        \n" +
                        "        *→  반례 : \"다른 담당자의 동일 날짜 처음 생성되면 또 다른 초기값 BO가 적용됨\"*\n" +
                        "        \n" +
                        "    2.  과제 정의서의 Todo 위임 규칙을 기반으로 함 (위임 받은 담당에게는 todo가 A0 로 생성되며, 위임 한 담당자가 표시된다. 위임 받은 담당에게 A0 가 이미 존재할 경우 하위 우선순위로 저장된다)\n" +
                        "        \n" +
                        "        *→  반례 : \"위임 전 기존의 todo가 A0인 경우, 새로운 위임 todo가 A0로 생성시 담당자 별로 A0 todo가 2개가 존재함\"*\n" +
                        "        \n" +
                        "    - **결론 : 담당자 별 우선순위는 날짜 별로 고유 값을 가질 수 있음 (예 김동주 - B0 - 2023.04.01, 안지슬 - B0 - 2023.04.01 )**\n" +
                        "    \n" +
                        "    - **P.S :** 코드 상에서는 '같은 담당자'를 제외한 기준으로 바로 교체 할 수 있음\n" +
                        "    \n");

        return new OpenAPI()
                .info(info);
    }

}
