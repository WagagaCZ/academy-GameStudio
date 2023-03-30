# academy-GameStudio

Každý si udělá branch pro svoji hru a až bude plně funkční mergne do Mainu přes pull request

Na Issues si klidně zakládejte nové branche nebo pracujte v té od hry

### Pozor
Nefunguje zabezpečení branchů, tak pozor, aby jste nepushovali rovnou do Mainu

### controllers:
#### scoreController
- get top scores for Minesweeper
```
http://localhost:8081/api/v2/score/top/Minesweeper
```
- post score for Minesweeper
```
http://localhost:8081/api/v2/score
```
```
JSON:
{
  "game":"Minesweeper",
  "player":"palyer",
  "points": 500
}
```
 - date timestamp added in controller

</br >

#### ratingController
- get average rating for Minesweeper
```
http://localhost:8080/api/v2/rating/avg/Minesweeper
```
- post rating for Minesweeper
```
http://localhost:8080/api/v2/rating
```
```
JSON:
{
  "username":"test user",
  "game": "Minesweeper",
  "rating": 5
}
```
 - date timestamp added in controller

</br >

#### commentController
- get top 10 comments for minesweeper

```
http://localhost:8080/api/v2/comment/Minesweeper
```

- post comment
```
http://localhost:8080/api/v2/comment
```

```
JSON:
{
  "game":"Minesweeper",
  "player":"player",
  "comment":"Hello"
}
```
- date timestamp added in controller
