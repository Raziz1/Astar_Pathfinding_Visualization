import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Astar_pathfinding extends PApplet {

// Daniel Shiffman
// http://codingtra.in
// http://patreon.com/codingtrain
// Part 1: https://youtu.be/aKYlikFAV4k
// Part 2: https://youtu.be/EaZxUCWAjb0
// Part 3: https://youtu.be/jwRT4PCT6RU
// Processing transcription: Chuck England

//Resources
//https://en.wikipedia.org/wiki/A*_search_algorithm
//https://medium.com/@nicholas.w.swift/easy-a-star-pathfinding-7e6689c7f7b2
//https://github.com/CodingTrain/website/blob/main/CodingChallenges/CC_051_astar/Processing/CC_051_astar/CC_051_astar.pde



// An object to describe a spot in the grid
class Spot {
  //X and Y position
  int i;
  int j;
  // f, g, and h values for A*
  float f = 0; //f(n) = g(n) + h(n)
  float g = 0; //g(n) is the cost of the path from the start node to n
  float heuristic = 0; //h(n) is a heuristic function that estimates the cost of the cheapest path from n to the goal

  // Neighbors
  List<Spot> neighbors = new ArrayList<Spot>();

  // Where did I come from?
  Spot previous = null;

  boolean wall = false;

  boolean end = false;

  //Constructor
  Spot(int i_, int j_) {

    // Location
    i = i_;
    j = j_;

    // Am I a wall?
    wall = false;
    //Randomly assign nodes a wall
    if (random(1) < 0.4f) {
      wall = true;
    }
  }

  // Display walls and end nodes
  public void show() {
    if (wall) {
      fill(0);
      noStroke();
      ellipse(i * w + w / 2.0f, j * h + h / 2.0f, w / 2.0f, h / 2.0f);
    } else if (end) { 
      fill(0, 255, 0);
      noStroke();
      rect(i * w, j * h, w, h);
    }
  }

  //Color nodes based on whether they are in the openSet, closedSet, or are a wall
  public void show(int col) {
    if (wall) {
      fill(0);
      noStroke();
      ellipse(i * w + w / 2.0f, j * h + h / 2.0f, w / 2.0f, h / 2.0f);
    } else {
      fill(col);
      rect(i * w, j * h, w, h);
    }
  }

  // Figure out who my neighbors are
  //OOO
  //OXO
  //OOO
  public void addNeighbors(Spot[][] grid) {
    //Right
    if (i < cols - 1) {
      neighbors.add(grid[i + 1][j]);
    }
    //Left
    if (i > 0) {
      neighbors.add(grid[i - 1][j]);
    }
    //Top
    if (j < rows - 1) {
      neighbors.add(grid[i][j + 1]);
    }
    //Bottom
    if (j > 0) {
      neighbors.add(grid[i][j - 1]);
    }
    //Bottom Left
    if (i > 0 && j > 0) {
      neighbors.add(grid[i - 1][j - 1]);
    }
    //Bottom Right
    if (i < cols - 1 && j > 0) {
      neighbors.add(grid[i + 1][j - 1]);
    }
    //Top Left
    if (i > 0 && j < rows - 1) {
      neighbors.add(grid[i - 1][j + 1]);
    }
    //Top Right
    if (i < cols - 1 && j < rows - 1) {
      neighbors.add(grid[i + 1][j + 1]);
    }
  }
};

// An educated guess of how far it is between two points
public float heuristic(Spot a, Spot b) {
  float d = dist(a.i, a.j, b.i, b.j);
  return d;
}

//========================================Variables========================================
// How many columns and rows?
int cols = 10;
int rows = 10;

// This will be the 2D array
Spot[][] grid = new Spot[cols][rows];

// Open and closed set
List<Spot> openSet = new ArrayList<Spot>();
List<Spot> closedSet = new ArrayList<Spot>();

// Start, end, and current nodes
Spot start;
Spot end;
Spot current;

// Width and height of each cell of grid
float w, h;

//Keep track of the mouseX and mouseY
int mousX;
int mousY;

//Have we selected the start and end nodes booleans
boolean startSelect=false;
boolean endSelect=false;

//Speed of pathfinding
int speed = 1000;

boolean pause=false;

//========================================MouseClicked========================================
public void mouseClicked() {
  if (mouseButton==RIGHT) {
    if (endSelect & startSelect) {
      pause=!pause;
      if (pause) {
        println("PAUSED");
      } else {
        println("UNPAUSED");
      }
    }
  }

  //Changing the speed of the simulation
  if (mouseButton==LEFT) {
    if (endSelect & startSelect & speed-100>=0) {
      speed=speed-100;
      println("SPEED: " + ((1000-speed)/100)+"x");
    }
  }

  //If we have selected the start node
  if (endSelect==false & startSelect==true) {
    mousX = round(mouseX/100);
    mousY = round(mouseY/100);
    end = grid[mousX][mousY];
    end.wall = false;
    end.end=true;
    endSelect=true;
    println("END NODE SELECTED");
    println("Click on the screen to speed up");
  }

  //Selecting the start node
  if (startSelect==false) {
    startSelect=true;
    mousX = round(mouseX/100);
    mousY = round(mouseY/100);
    start = grid[mousX][mousY];
    start.wall = false;
    openSet.add(start);
    println("START NODE SELECTED");
  }
}

//========================================Seutp========================================
public void setup() {
  //Set the screen size
  
  println("A* Pathfinding");

  // Grid cell size
  w = PApplet.parseFloat(width) / cols;
  h = PApplet.parseFloat(height) / rows;

  //Create a 2D grid array of nodes
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      grid[i][j] = new Spot(i, j);
    }
  }

  // Calculate the neigbors for each node
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      grid[i][j].addNeighbors(grid);
    }
  }
  println("Select the start node and end node");
}

//========================================Main Draw Function========================================
public void draw() {
  //If we have selected the start and end nodes
  if (startSelect & endSelect) {
    if (pause==false) {
      // Am I still searching?
      if (openSet.size() > 0) {

        // Find node with the lowest F score in the open set list and set it as the current
        int winner = 0;
        for (int i = 0; i < openSet.size(); i++) {
          if (openSet.get(i).f < openSet.get(winner).f) {
            winner = i;
          }
        }
        current = openSet.get(winner);

        // Have we reached the end of the path
        if (current == end) {
          noLoop();
          println("DONE!");
        }

        // Best option moves from openSet to closedSet
        //Remove it from the open set and add it to the closedset because we no longer need to calculate it 
        openSet.remove(current);
        closedSet.add(current);

        // Check all the neighbors of the current node
        List<Spot> neighbors = current.neighbors;
        for (int i = 0; i < neighbors.size(); i++) {
          Spot neighbor = neighbors.get(i);

          // Check that the neighbor hasn't been visited and is not a wall
          if (!closedSet.contains(neighbor) && !neighbor.wall) {

            //tempG is the distance traveled to the current node plus the distance from the current node to the neighbor node 
            float tempG = current.g + heuristic(neighbor, current);

            // Is this a better path than before?
            boolean newPath = false;
            //Is the neighbor already in the openlist
            if (openSet.contains(neighbor)) {
              if (tempG < neighbor.g) {//If going through the current node to the neighbor from the start node is shorter than the distance from the start directly to the neighbor node 
                neighbor.g = tempG; //Then set the neighbor.g to tempG
                newPath = true;
              }
            } else {
              neighbor.g = tempG; //Set the neighbor.g to tempG
              newPath = true;
              openSet.add(neighbor); //Consider it for the next path
            }

            // Yes, it's a better path
            if (newPath) {
              neighbor.heuristic = heuristic(neighbor, end); //Calculate the cost from the neighbor to the end
              neighbor.f = neighbor.g + neighbor.heuristic;  //Calculate the cost of the distance from the start through previous nodes plus the cost of the distance to the end
              neighbor.previous = current; //Set the parent node of the neighbors
            }
          }
        }
      } else {
        // If the openList is empty and there is no solution
        println("no solution");
        noLoop();
        return;
      }
      //Delay the speed of calculation so people can see it
      delay(speed);
    }
  }


  // Draw current state of everything
  background(255);

  //Draw grid
  for (int i = 0; i < cols; i++) {
    for (int j = 0; j < rows; j++) {
      grid[i][j].show();

      //If the grid node is not a wall print the f value of the grid 
      if (grid[i][j].wall==false) {
        pushStyle();
        textSize(20);
        textAlign(CENTER);
        fill(0);
        //Limit the decimal places to 2 points
        text("F:"+nf(grid[i][j].f, 0, 2), grid[i][j].i* w + w / 2, (grid[i][j].j* h + h / 2)-25);
        text("H:"+nf(grid[i][j].heuristic, 0, 2), grid[i][j].i* w + w / 2, grid[i][j].j* h + h / 2);
        text("G:"+nf(grid[i][j].g, 0, 2), grid[i][j].i* w + w / 2, (grid[i][j].j* h + h / 2)+25);
        popStyle();
      }
    }
  }


  //Draw items in the closed list as RED
  for (int i = 0; i < closedSet.size(); i++) {
    closedSet.get(i).show(color(255, 0, 0, 50));
  }

  //Draw items in the open list as GREEN
  for (int i = 0; i < openSet.size(); i++) {
    openSet.get(i).show(color(0, 255, 0, 50));
  }

  //If we have selected the start and end points
  if (startSelect && endSelect) {
    // Find the path by working backwards
    List<Spot> path = new ArrayList<Spot>();
    //Find the parent of the current node all the way to the starting point
    Spot temp = current;
    path.add(temp);
    while (temp.previous != null) {
      path.add(temp.previous);
      temp = temp.previous;
    }

    // Drawing path as continuous line
    pushStyle();
    noFill();
    strokeWeight(w / 2);
    stroke(255, 0, 200, 50);
    beginShape();
    for (int i = 0; i < path.size(); i++) {
      vertex(path.get(i).i * w + w / 2, path.get(i).j * h + h / 2);
    }
    endShape();
    popStyle();
  }
}

  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Astar_pathfinding" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
