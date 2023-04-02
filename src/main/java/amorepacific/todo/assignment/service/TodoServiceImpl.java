package amorepacific.todo.assignment.service;

import amorepacific.todo.assignment.entity.Todo;
import amorepacific.todo.assignment.entity.User;
import amorepacific.todo.assignment.model.Priority;
import amorepacific.todo.assignment.model.TodoPriorityComparator;
import amorepacific.todo.assignment.repository.TodoRepository;
import amorepacific.todo.assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Override
    public Todo addTodo(Todo todo) {

        // 담당자 Validation
        User todoUser = todo.getUser();

        if (userRepository.findById(todoUser.getId()).isEmpty()) {
            throw new RuntimeException("담당자 ID 다시 확인하세요");
        }

        //setting
        todo.setPriority(new Priority('B', 0));


        //save
        Todo savedTodo = todoRepository.upsert(todo);

        //filtering & action
        todoRepository.findByUserIdAndDate(todo.getUser().getId(), todo.getDate())
                .ifPresent(todos -> {
                    //같은 담당자의 동일 날짜의 Todos가 있는 경우
                    int size = todos.size();
                    if (size > 1) {
                        //중요도, 순서 순으로 오름차 정렬
                        List<Todo> sortedTodos = sortPriorityByTodos(todos);

                        int lastIndex = size - 1;
                        Todo lastTodo = sortedTodos.get(lastIndex);

                        //우선 순위의 초기값은 동일날짜 todo들 중 최하 순위로 설정 (ex. B1, B2, B3)
                        //마지막 우선순위 보다 아래로 설정
                        savedTodo.setPriority(new Priority(lastTodo.getPriority().getImportance()
                                , lastTodo.getPriority().getOrder() + 1));

                        todoRepository.upsert(savedTodo);
                    }
                });


        return savedTodo;
    }

    @Override
    public Optional<List<Todo>> getTodosByUserIdOrDateOrNothing(long userId, String date) {

        List<Todo> todos = null;

        //userId와 date 둘다 있는 경우
        if (userId > 0 && date != null && !date.trim().equals("")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            todos = todoRepository.findByUserIdAndDate(userId, localDate).get();

        } else {
            //userId만 있는 경우
            if (userId > 0) {
                todos = todoRepository.findByUserId(userId).get();

                //date만 있는 경우
            } else if (date != null && !date.trim().equals("")) {
                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                todos = todoRepository.findByDate(localDate).get();

                //userId와 date가 둘다 없는 경우 (전체 조회)
            } else {
                todos = todoRepository.findAll().get();
            }
        }

        //중요도/우선순위 로 정렬
        if (todos.size() > 0)
            todos = sortPriorityByTodos(todos);

        return Optional.ofNullable(todos);
    }

    @Override
    public Todo updateTodo(Todo todo) {

        Todo oldTodo = todoRepository.findById(todo.getId()).orElseThrow(() -> {
            throw new NoSuchElementException("해당 todo가 없습니다. todoId를 확인하세요.");
        });


        //특정 todo의 task, description 변경함 -> API를 통해 변경되어 들어온 값 유지


        //특정 todo의 status 변경함 -> 그 중 "위임", "취소" 여부 값 체크, 그 외 API를 통해 변경되어 들어온 값 유지
        String todoStatus = todo.getStatus();

        //위임과정에서 담당자에 대한 체크
        User oldUser = oldTodo.getUser();
        User todoUser = todo.getUser();

        //위임태그 여부를 위한 셋팀
        String oldtag = oldTodo.getTag();
        todo.setTag(oldtag);
        String todoTag = todo.getTag();

        //특정인에게 todo를 위임할 수 있다
        /*
         * 별도 표시 위치 안내가 없어, 표시는 별도 tag 변수에 기재
         * 위임한 담당자에게 todo는 위임 + 위임 받은 사람으로 표시 된다. 예) 위임(담당자 안지슬)
         * 위임 받은 담당에게는 todo가 A0 로 생성되며, 위임 한 담당자가 표시된다. 예) {todoId} 위임(위임자 김동주)
         */

        //변경된 todo의 Status가 "위임", 다른 담당자에게 todo를 위임 하는 경우
        if (("위임").equals(todoStatus)) {

            //위임 받은 todo는 재 위임할 수 없다.
            //위임 받은 여부에 대해 표시되는 이전 todo의 tag 변수를 통해 확인

            if (todoTag != null && !("").equals(todoTag)) {
                //첫 공백 이후 '위임' 이라는 단어가 나오면 이미 위임 받은 todo임. 예) {todoId} 위임(위임자 김동주)
                String[] args = todoTag.split(" ");
                if (args[1].startsWith("위임")) {
                    throw new RuntimeException("위임 받은 todo는 재 위임할 수 없습니다");
                }

                if (args[0].startsWith("위임")) {
                    throw new RuntimeException("이미 위임한 todo 입니다");
                }
            }


            if (userRepository.findById(todoUser.getId()).isEmpty() || oldUser.getId() == todoUser.getId()) {
                throw new RuntimeException("위임할 담당자를 다시 확인하세요 - 본인에게 위임할 수 없습니다");
            }

            //1. 위임 todo생성

            //위임 받은 담당에게는 todo가 A0 로 생성됨
            Priority priority = new Priority('A', 0);

            //신규 생성시 Repository에서 초기값 셋팅 예정 (Priority, User 제외)
            Todo mandatedTodo = new Todo(todo.getDate(), todo.getUser(), priority, todo.getTask(), todo.getDescription(), todo.getStatus());

            //위임 todo생성
            todoRepository.upsert(mandatedTodo);

            //우선순위 정렬
            //위임받은 담당에게 A0가 이미 존재할 경우 하위 우선순위 저장된다.
            todoRepository.findByUserIdAndDate(mandatedTodo.getUser().getId(), mandatedTodo.getDate())
                    .ifPresent(todos -> {
                        //같은 담당자의 동일 날짜의 Todos가 있는 경우
                        //같은 담당자의 Todo전체 리스트 중 A0를 찾는다.
                        int size = todos.size();
                        if (size > 1) {
                            //중요도, 순서 순으로 오름차 정렬 ('S' 중요도는 정렬이 안됨)
                            Collections.sort(todos, new TodoPriorityComparator());

                            //'A' 중요도가 있는 Todos 체크
                            int index = 0;
                            while (todos.get(index).getPriority().getImportance() == 'A') {
                                index++;
                            }

                            //'A' 중요도가 있는 경우
                            //'A' 중요도를 가진 todo들 중 마지막 todo보다 아래 순위로 설정
                            int lastIndex = index - 1;
                            if (lastIndex >= 1) {
                                Todo lastTodo = todos.get(lastIndex);

                                mandatedTodo.setPriority(new Priority(lastTodo.getPriority().getImportance()
                                        , lastTodo.getPriority().getOrder() + 1));
                            }
                        }
                    });


            //tag값에 todoId 기재
            //이전 todo의 담당자인, 위임 한 담당자가 표시된다
            //예) {todoId} 위임(위임자 김동주)
            mandatedTodo.setTag(todo.getId() + " 위임(위임자 " + oldUser.getName() + ")");
            todoRepository.upsert(mandatedTodo);


            //2. todo변경을 위한 위임 표시 Tag 추가

            //신규 담당자는 userId와 name를 통해 확인 + ID 표시(위임관계 삭제용 ID)
            todo.setTag("위임(담당자 " + todoUser.getName() + ") " + mandatedTodo.getId());

            //담당자 위임 후 위임자 재설정
            todo.setUser(oldUser);

            //위임 받은 todo를 취소할 수 있다
            //취소하면 원상 복귀된다.
        } else if (("취소").equals(todoStatus)) {

            //위임자 todoId 확인 예) {todoId} 위임(위임자 김동주)
            //위임 받은 여부에 대해 표시되는 현재 todo의 tag 변수를 통해 확인
            if (todoTag != null && !("").equals(todoTag)) {
                //첫 공백 이후 '위임' 이라는 단어가 나오면 이미 위임 받은 todo임. 예) {todoId} 위임(위임자 김동주)
                String[] args = todoTag.split(" ");
                if (args[1].startsWith("위임")) {

                    //1. 위임자 todo원상 복구

                    long mandatedTodoId = Long.parseLong(args[0]);
                    Todo mandatedTodo = todoRepository.findById(mandatedTodoId).get();

                    //위임 표시 Null 처리
                    mandatedTodo.setTag(null);
                    //이전 상태 저장
                    mandatedTodo.setStatus(oldTodo.getStatus());

                    //2. 위임 받은 todo삭제 (기존(old) todo삭제)
                    long oldTodoId = oldTodo.getId();

                    todoRepository.findByUserIdAndDate(todo.getUser().getId(), todo.getDate()).ifPresent(todos -> {
                        //같은 담당자의 동일 날짜의 Todos가 있는 경우
                        Collections.sort(todos, new TodoPriorityComparator());
                        sortTodosBySortedTodosAndDeletedTodoId(todos, oldTodoId);
                    });

                    todoRepository.deleteById(oldTodoId);


                    //3. 위임자 todo반환
                    return todoRepository.upsert(mandatedTodo);

                }
            }
            throw new RuntimeException("위임 받은 todo가 아닙니다");

        }

        if (!("위임").equals(todoStatus) && (userRepository.findById(todoUser.getId()).isEmpty() || oldUser.getId() != todoUser.getId())) {
            throw new RuntimeException("위임상태 지정이 아닌 경우 담당자는 변경할 수 없습니다");
        }


        //특정 todo의 우선순위 변경함 -> 중요도 및 순서를 유지 체크

        //1. 변경된 사항 저장
        todoRepository.upsert(todo);

        //2. 중요도 및 순서 재정렬 (중요도와 순서가 변경했을때, 기존에 Todos들을 재정렬 필요)

        //이전 todo의 중요도와 순서를 현재 todo와 비교
        char oldImportance = oldTodo.getPriority().getImportance();
        int OldOrder = oldTodo.getPriority().getOrder();

        char todoImportance = todo.getPriority().getImportance();
        int todoOrder = todo.getPriority().getOrder();

        // 중요도와 순서의 변경이 있을 경우
        if (todoImportance != oldImportance || todoOrder != OldOrder) {

            todoRepository.findByUserIdAndDate(todo.getUser().getId(), todo.getDate()).ifPresent(todos -> {
                //같은 담당자의 동일 날짜의 Todos가 있는 경우
                int size = todos.size();

                //현재 updated Todo외 다른 todo들이 있는 경우
                if (size > 1) {

                    //중요도, 순서 순으로 오름차 정렬
                    List<Todo> sortedTodos = sortPriorityByTodos(todos);

                    //정렬된 todos를 기반으로 updated todo를 재정렬
                    sortTodoBySortedTodosAndUpdatedTodo(sortedTodos, todo);


                    //현재 updated된 todo만 있는 경우
                } else {

                    Todo updatedTodo = todos.get(0);

                    //순서만 0으로 고정하여 정렬
                    //order의 입력값이 0이 아니게 입력되는 것에 순서를 0으로 재정렬
                    if (updatedTodo.getPriority().getOrder() != 0) {
                        updatedTodo.setPriority(new Priority(updatedTodo.getPriority().getImportance(), 0));
                        todoRepository.upsert(updatedTodo);
                    }
                }
            });
        }

        //최종적으로 수정된 todo반환
        return todoRepository.findById(todo.getId()).get();
    }

    @Override
    public Todo deleteTodoByTodoId(long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> {
            throw new NoSuchElementException("해당 todo가 없습니다. todoId를 확인하세요.");
        });

        //위임 관계 태그 확인
        String todoTag = todo.getTag();
        if (todoTag != null && !("").equals(todoTag)) {
            //첫 공백 이후 '위임' 이라는 단어가 나오면 이미 위임 받은 todo임. 예) {todoId} 위임(위임자 김동주)
            String[] args = todoTag.split(" ");

            long mandatedTodoId;
            Todo mandatedTodo = null;

            //위임 받은 todo가 삭제요청 된 경우
            if (args[1].startsWith("위임")) {
                //1. 위임자 todo삭제
                mandatedTodoId = Long.parseLong(args[0]);
                mandatedTodo = todoRepository.findById(mandatedTodoId).get();

                //위임 한 todo가 삭제요청 된 경우
            } else if (args[0].startsWith("위임")) {
                //2. 담당자 todo삭제
                mandatedTodoId = Long.parseLong(args[2]);
                mandatedTodo = todoRepository.findById(mandatedTodoId).get();
            } else {
                mandatedTodoId = 0;
            }

            //위임 관계 todo삭제
            //cross check
            if (mandatedTodoId > 0) {
                todoRepository.findByUserIdAndDate(mandatedTodo.getUser().getId(), mandatedTodo.getDate()).ifPresent(todos -> {
                    //같은 담당자의 동일 날짜의 Todos가 있는 경우
                    Collections.sort(todos, new TodoPriorityComparator());
                    sortTodosBySortedTodosAndDeletedTodoId(todos, mandatedTodoId);
                });
                todoRepository.deleteById(mandatedTodoId);
            }
        }


        todoRepository.findByUserIdAndDate(todo.getUser().getId(), todo.getDate()).ifPresent(todos -> {
            //같은 담당자의 동일 날짜의 Todos가 있는 경우
            Collections.sort(todos, new TodoPriorityComparator());
            sortTodosBySortedTodosAndDeletedTodoId(todos, todoId);
        });

        todoRepository.deleteById(todoId);


        //삭제된 Origin Todo반환
        return todo;
    }

    private void sortTodoBySortedTodosAndUpdatedTodo(List<Todo> sortedTodos, Todo updatedTodo) {
        //중요도 및 우선순위 정책은 항상 지켜져야 한다

        /*  중요도 및 우선 순위 정책

        1. 중요도
            - 같은 담당자, 동일 날짜 내에 중복, 누락이 가능함

        2. 순서
            - 같은 담당자, 동일 날짜, 동일 중요도 내에서 우선순위를 나타냄
            - 같은 담당자, 동일 날짜, 동일 중요도 내에서 중복과 누락이 없어야 함

        * '같은 담당자'를 정책의 기준으로 넣은 이유 :
            1. 과제정의서의 Todo생성 규칙을 기반으로 함 (같은 담당자의 동일 날짜 처음 생성되면 초기값은 B0 를 설정 함)
                -> 반례 : "다른 담당자의 동일 날짜 처음 생성되면 또 다른 초기값 BO가 적용됨"

            2. 과제정의서의 Todo위임 규칙을 기반으로 함 (위임 받은 담당에게는 todo가 A0 로 생성되며, 위임 한 담당자가 표시된다. 위임받은 담당에게 A0 가 이미 존재할 경우 하위 우선순위로 저장된다)
                -> 반례 : "위임전 기존의 todo가 A0인 경우, 새로운 위임 todo가 A0로 생성시 담당자 별로 A0 todo가 2개가 존재함"

            - 결론 : 담당자 별 우선순위는 날짜별로 고유 값을 가질 수 있음 (예 김동주 - B0 - 2023.04.01, 안지슬 - B0 - 2023.04.01 )

            P.S 코드 상에서는 '같은 담당자'를 제외한 기준으로 바로 교체 할 수 있음
        */

        //updated Todo의 중요도와 순서가 체크(중복과 누락여부)없이 sortedTodos에 적용된 상황을 재정렬하기 위함

        //전체 sortedTodos 내의 Object 두개씩 (Todo1, Todo2) 비교하여 중요도, 순서의 동일여부, 누락여부를 체크
        //Todo1이 기준이 된다.
        //Todd2는 비교 대상
        int sortedTodosSize = sortedTodos.size();
        for (int i = 0; i < sortedTodosSize - 1; i++) {
            Todo stdTodo = sortedTodos.get(i);
            Todo nextTodo = sortedTodos.get(i + 1);


            // Object 두개씩 (Todo1, Todo2) 중요도, 순서 setting
            char stdImportance = stdTodo.getPriority().getImportance();
            char nextImportance = nextTodo.getPriority().getImportance();

            int stdOrder = stdTodo.getPriority().getOrder();
            int nextOrder = nextTodo.getPriority().getOrder();

            //Compare 시나리오
            //1. 중요도가 같은 경우
            if (stdImportance == nextImportance) {

                // 순서가 같은 경우 (우선순위 동일) - (예 B0, B0)
                if (stdOrder == nextOrder) {
                    //stdTodo가 현재 updatedTodo 와 동일한지 여부 체크 (다르다면 next가 updatedTodo)
                    //같으면 현재 index + 1의 Todo부터 재정렬 필요
                    int indexOfFirstForChangingTodo = i + 1;

                    //다르면 Next가 updatedTodo 이므로 index 증가
                    if (stdTodo.getId() != updatedTodo.getId()) {
                        indexOfFirstForChangingTodo++;
                    }

                    //
                    char changingTodoImportance = sortedTodos.get(indexOfFirstForChangingTodo).getPriority().getImportance();

                    //updatedTodo 뒤 부터는 전부 우선 순위 변경 대상
                    //같은 중요도 내에서만
                    for (int j = indexOfFirstForChangingTodo; j < sortedTodos.size(); j++) {
                        Todo changingTodo = sortedTodos.get(j);

                        //다른 중요도면 break
                        if (changingTodo.getPriority().getImportance() != changingTodoImportance) {
                            break;
                        }

                        //기존 순서에 + 1을 적용
                        changingTodo.setPriority(new Priority(changingTodoImportance, changingTodo.getPriority().getOrder() + 1));
                        todoRepository.upsert(changingTodo);
                    }
                }

                // 순서가 누락인 경우 (예 B0 , ' ' , B2 - B1가 누락)
                if (stdOrder + 1 != nextOrder) {

                    //stdTodo + 1을 적용 (재정렬)
                    nextTodo.setPriority(new Priority(stdTodo.getPriority().getImportance()
                            , stdTodo.getPriority().getOrder() + 1));
                    todoRepository.upsert(nextTodo);
                }

                //2. 중요도가 다른 경우
            } else {
                //nextTodo와는 다르지만, stdTodo의 중요도가 updatedTodo의 중요도와 같으면 재정렬 체크 포인트
                char updatedImportance = updatedTodo.getPriority().getImportance();
                if (stdImportance == updatedImportance) {

                    //첫번째 index의 Todo의 순서가 누락인 경우 (예: B0가 없음)
                    if (i == 0 && stdOrder != 0) {
                        //순서를 0으로 초기화
                        stdTodo.setPriority(new Priority(updatedImportance, 0));
                        todoRepository.upsert(stdTodo);

                        //첫번째 index가 아닐때, stdTodo의 이전 순서가 누락인 경우 (예: B0,' ',B2(stdTodo))
                    } else if (i > 0 && stdOrder - 1 != sortedTodos.get(i - 1).getPriority().getOrder()) {
                        stdTodo.setPriority(new Priority(updatedImportance, sortedTodos.get(i - 1).getPriority().getOrder() + 1));
                        todoRepository.upsert(stdTodo);
                    }

                    //stdTodo와는 다르지만, nextTodo의 중요도가 updatedTodo의 중요도와 같으면 재정렬 체크 포인트
                } else if (nextImportance == updatedImportance) {

                    //nextTodo가 stdTodo와 등급이 다른데, 순서가 누락된 경우 (예, A5, ' ' ,B1(nextTodo))
                    if (nextOrder != 0) {
                        nextTodo.setPriority(new Priority(updatedImportance, 0));
                        todoRepository.upsert(nextTodo);
                    }
                }

            }
        }
    }

    private void sortTodosBySortedTodosAndDeletedTodoId(List<Todo> sortedTodos, long deletedTodoId) {
        //1. sortedTodos 상에서 삭제 되어야 할 deletedTodo 정렬기준 index 및 중요도를 위한 default setting
        //deletedTodoId가 0일 수도 있기에 -1
        int deletedIndex = -1;
        char deletedImportance = 0;

        //2. sortedTodos 상에서 삭제 되어야 할 deletedTodo의 정렬기준 index 및 중요도 찾기
        for (int i = 0; i < sortedTodos.size(); i++) {
            if (sortedTodos.get(i).getId() == deletedTodoId) {
                deletedIndex = i;
                deletedImportance = sortedTodos.get(i).getPriority().getImportance();
                break;
            }
        }

        //3. deletedTodo의 정렬기준 index + 1부터의 otherTodo들을 deletedTodo의 중요도를 바탕으로 순서를 앞으로 당김 (예 : B1 -> B0)
        for (int i = deletedIndex + 1; i < sortedTodos.size(); i++) {
            Todo otherTodo = sortedTodos.get(i);
            if (otherTodo.getPriority().getImportance() != deletedImportance) {
                break;
            }
            otherTodo.setPriority(new Priority(deletedImportance, otherTodo.getPriority().getOrder() - 1));
            todoRepository.upsert(otherTodo);
        }
    }


    private List<Todo> sortPriorityByTodos(List<Todo> todos) {
        List<Todo> sortedTodos = new ArrayList<>();

        // 기존 todos, 선 정렬
        Collections.sort(todos, new TodoPriorityComparator());

        //'S' 중요도가 있는 Todos 체크
        int size = todos.size();
        int lastIndex = size - 1;

        //마지막 인덱스부터 'S'가 존재하기에 sortedTodos에 추가하면서 기존 todos에서 제거
        for (int i = lastIndex; i >= 0; i--) {
            Todo sTodo = todos.get(i);

            if (sTodo.getPriority().getImportance() == 'S') {
                sortedTodos.add(sTodo);
                todos.remove(i);
            } else {
                break;
            }
        }

        // 'S' 중요도가 있는 sortedTodos 순서 정렬
        Collections.sort(sortedTodos, new TodoPriorityComparator());

        //sortedTodos에 정렬된 나머지 todos 추가
        sortedTodos.addAll(todos);

        return sortedTodos;
    }
}
