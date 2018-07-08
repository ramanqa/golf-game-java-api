# golf-game-java-api
A game of golf to be played in Java. There are 6 difficulty levels in course. Each course has one hole. Par is 5. Wind effect and Field size increases with course difficulty.

## how to play
### Start new game
`GolfCourse course = new GolfCourse(difficulty); // where difficulty is an integer between 0 to 5 (including 0 and 5)`

### get game id
`course.getUUID(); // returns UUID of current game`

### get game details
`course.getJSON(); // returns JSONObject of current game`
or
`course.toString(); // returns String representation of current game`

### load existing game known UUID
`GolfCourse course = new GolfCourse(UUID); // where UUID = course.getUUID() from previous calls`

### Hit the ball
`course.hitGolfBall(Power, Direction);`
Where 
 - Power is integer between 0 and 10, including 0 and 10
 - Direction is any of R, RD, D, LD, L, LU, U or RU. Where R - Right, L - Left, D - Down and U - Up.
For Example:
`course.hitGolfBall(10, "RU"); // if I want to hit the ball with max power in Right Upward direction (45 degrees clockwise).` 

### Wind
Wind resisitance will affect final position of the ball. Wind X and Y will be added or deducted to power depending on wind direction and shot direction.

### get computed result
`course.getResult()` 
This will return one of:
 - "Hole in one! Condor! 4 under par"
 - "Albatross! 3 under par"
 - "Eagle! 2 under par"
 - "Birdie! 1 under par"
 - "Par!"
 - "Bogey! 1 over par"
 - "Double Bogey! 2 over par"
 - "Triple Bogey! 3 over par"
 - "Quadruple Bogey! 4 over par"
