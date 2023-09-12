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


