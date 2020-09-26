# echo5 Introduction

tbd.

TODO: write [great documentation](http://jacobian.org/writing/what-to-write/)

The following content is only a proposal on how this documentation can be structured.

## Conceptual Overview

echo5 is designed to sit at the boundaries of your software system to inspect, match and mock certain service requests for third party services. This is needed when you integratively test your software system but also want to cut your traffic at system edges with full and flexible control of "third party response behavior".

### Register traffic scenarios

tbd.

#### Sequence
```
+-----------+                                    +-------+        
| Consumer  |                                    | echo5 |        
+-----------+                                    +-------+        
      | -----------------------\                     |            
      |-| Consumer preparation |                     |            
      | |----------------------|                     |            
      |                                              |            
      | Register scenarios to mock in the future     |            
      |--------------------------------------------->|            
      |                                              | ------------------------------------------\
      |                                              |-| echo5 validates the delivered scenarios |
      |                                              | | and stores them for future use          |
      |                                              | |-----------------------------------------|
      |                                              | validate() 
      |                                              |----------- 
      |                                              |          | 
      |                                              |<---------- 
      |                                              |            
      |                                              | store()    
      |                                              |--------    
      |                                              |       |    
      |                                              |<-------    
      |                                              |            
      |                                           OK |            
      |<---------------------------------------------|            
      |                                              |            
```

<details>
<summary>
<i>[Sequence raw][]</i>
</summary>
```
object Consumer echo5
note right of Consumer: Consumer preparation
Consumer->echo5: Register scenarios to mock in the future
note right of echo5: echo5 validates the delivered scenarios\nand stores them for future use
echo5->echo5: validate()
echo5->echo5: store()
echo5-> Consumer:OK
```
</details>

[Sequence raw]: https://textart.io/sequence
