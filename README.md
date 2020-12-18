# Astar_Pathfinding_Visualization 📉📈📊
A* (star) Pathfinding Algorithm in Processing 

<p> 
  <img widht=400 height=417 align='Right' src="https://github.com/Raziz1/Astar_Pathfinding_Visualization/blob/main/images/astar_visualization.gif? raw=true">
</p>

## Overview 
* I followed the **[Coding Train's tutorial on A* (star) pathfinding](https://www.youtube.com/watch?time_continue=80&v=aKYlikFAV4k&feature=emb_logo)** to get a better understanding of the algorithm involved.
* I have used their code and modified it for a better visualization of the algorithm
* I have included a delay in the code so it is easier to see the steps of the algorithm.

## Instructions 📃
1. Left click once to set the start node
2. Left click once to set the end node
3. The algorithm will commence
4. Left click to speed up the algorithm
5. Right click to pause/unpause the algorithm

## A* (star) Pathfinding
* A* is a pathfinding algorithm that is popular for its ability to find the optimal solution fo rthe shortest distance between two nodes.
* A* is considered an extension of the [Dijkstra's algorithm](https://www.programiz.com/dsa/dijkstra-algorithm)(It simply explores the graph in a different (and less optimized) order.)
* A* determines the best path based on the cost of the path and an estimate of the cost required to extend the path to the goal
   * f(n) = g(n) + h(n)
   * g(n) is the cost of the path from the start node to n
   * h(n) is a heuristic function that estimates the cost of the cheapest path from n to the goal.
      * The Manhattan distance (explained below) from node n to the goal is often used. This is a standard heuristic for a grid.

## Resources 📚
* [Wikipedia A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm)
* [Easy A* (star) Pathfinding](https://medium.com/@nicholas.w.swift/easy-a-star-pathfinding-7e6689c7f7b2)
  * This website has a great step by step explenation of how the algorithm finds the shortest path
* [Coding Train Github A* (star)](https://github.com/CodingTrain/website/tree/main/CodingChallenges/CC_051_astar/Processing/CC_051_astar)
  * [YOUTUBE Coding Train A* (star) Pathfinding](https://www.youtube.com/watch?time_continue=80&v=aKYlikFAV4k&feature=emb_logo)
* [Brilliant A* Search](https://brilliant.org/wiki/a-star-search/)
* [Pathfinding Algorithms](https://medium.com/@urna.hybesis/pathfinding-algorithms-the-four-pillars-1ebad85d4c6b) Explores the essential pathfinding algorithms 
