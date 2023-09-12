# pagination 
### 목표 : 일자별로 게시물을 리스트 형태로 반환하는데, offset 형태로 반환하는 api를 만들어보자.
Offset-based pagination 
> 페이지네이션을 서버단에서 쿼리를 통해 작성하게 되는게 페이지 단위로 데이터를 불러올 시에 LIMIT을 사용해서 데이터를 가져오는 것.
> 보통 게시판 페이지마다 데이터를 가져올 경우 흔히 사용하게 된다 .

### 코드 구현

### API

#### Controller
```
@PostMapping("/member/{memberId}")
    public Page<Post> getPosts(
            @PathVariable Long memberId,
            Pageable pageable

    ){
        return postReadService.getPosts(memberId, pageable);
    }


```

#### Repository
``` 
public Page<Post> findAllByMemberId(Long memberId, Pageable Pageable){
        // page와 pageRequest 사용하는 이유는 함수 이름을 Spring DATA JPA naming 방식을 따라가려함.
        //Pageable 사용가능한데, 어차피 PageRequest 인터페이스를 구현한 것이 저것임.
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", Pageable.getPageSize())
                .addValue("offset", Pageable.getOffset());

        var sql = String.format("""
                SELECT * 
                FROM %s 
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size 
                OFFSET :offset  
                """, Table, PageHelper.orderBy(Pageable.getSort()));

        var posts= namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
        return new PageImpl(posts, Pageable, getCount(memberId));
    }

```
위에 보면 sql 을 정의하는 String.format에 보면 Order By 항목에 해당하는 값을 정의해 주어, 페이지네이션 시에 정렬을 주었다.<br>
pageable에 Sort라는 메서드가 있다. 이 sort는 정렬을 맡는 파트이며, pageHelper라는 클래스를 만들어 리턴 값을 받도록 하여, 값을 주입하도록 하겠다. < br><br>

또, 여기서  PageImpl이라는 객체를 만들어서 반환하는데, <br>
>	public PageImpl(List<T> content, Pageable pageable, long total) {
pageImpl 객체의 경우에는 위의 값을 받기에, 총 갯수를 나타내어 주는 getCount메서드를 따로 만들어주었다. <br><br>

#### getCount 메서
```
 private Long getCount(Long memberId){
        var sql = String.format("""
                Select count(id)
                From %s
                WHERE :memberId = :memberId
                """, Table);

        var params = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

```

#### pageHelper 클래스 
```

public class PageHelper {
    public static String orderBy(Sort sort){
        if(sort.isEmpty()){
            return "id DESC";
        }

        List<Sort.Order> orders = sort.toList();
        var orderBys= orders.stream()
                .map(order -> order.getProperty()+ " "+order.getDirection())
                .toList();

        return String.join(",", orderBys);

    }
}

```

### 동작

```
memberId = 3
{
  "page": 0,
  "size": 2,
  "sort": [
    "createdDate"
  ]
}

```
위 정보를 주입하게 되면 memberId=3에 해당하는 data를 size(2개)에 맞는 페이지를 보여주게된다.<br>

```

{
  "content": [
    {
      "id": 2001107,
      "memberId": 3,
      "contents": "R",
      "createdDate": "2023-08-03",
      "createdAt": "2023-09-05T00:55:45"
    },
    {
      "id": 2001109,
      "memberId": 3,
      "contents": "eqNLQZf",
      "createdDate": "2023-08-03",
      "createdAt": "2023-08-03T19:10:56"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 2,
    "unpaged": false,
    "paged": true
  },
  "last": false,
  "totalPages": 1500511,
  "totalElements": 3001022,
  "size": 2,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}

```

### 생각해볼 점
Offset 기반의 페이지 네이션의 경우에는 마지막 페이지를 구하기 위하여 전체 갯구를 알아야한다.<br>
위 코드의 경우에는 getCount라는 메서드를 만들어서 전체 count를 받아서 페이지네이션을 진행하였다.<br>
많은 데이터가 올 경우, 전체 데이터를 구하는 것이 부하가 올 수 있겠다라는 생각을 하였습니다. <br>
예를 들어서 생각해본다면 1-10번까지의 게시글이 있다고하자. 페이징 처리로 위 방법의 경우에는 offset과 limit을 사용하는데, 예를들어 size가 2라고했을 때, 3번째 페이지에 해당하는 5, 6번 세시글을 표시하기 위해서 1-4번까지의 게시글을 조회해야하는 것이다. <br>
즉, 데이터 양이 많아질수록 불필요한 데이터 조회가 발생한다는 것이다.<br>

### 그렇다면?
커서 기반 페이징을 알아보자!
> 커서 기반 페이징? : 클라이언트가 가져간 마지막 row의 순서상 다음 row들을 n개 요청/응답하게 구현
장점 :  커서 기반 페이징은 키를 기준으로 데이터 탐색범위를 최소화
단점 :  커서 기반 페이징은 전체 데이터를 조회하지 않기 때문에 아래 UI구현이 어렵다.

다른 레퍼지 토리를 이용할 예정!



