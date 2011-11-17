import processing.opengl.*;

class Bond {
  float strength = 10;
  Atom a, b;
  
  Bond(Atom A, Atom B) {
    a = A;
    b = B;
  }
  
  void display() {
    stroke(255);
    strokeWeight(5);
    //TODO: make this a cylinder
    line(a.x, a.y, a.z, b.x, b.y, b.z);
  }
}

PApplet app = this;

public class Atom {
  float x, y, z;
  float r = 20;
  PVector force;
  PVector velocity;
  float charge = 10;
  Bond[] bonds = new Bond[2];
  int bonded = 0;
  Atom(float X, float Y, float Z) {
    x = X;
    y = Y;
    z = Z;
    
    app.registerMouseEvent(this);
  }
  
  public void mouseEvent(MouseEvent e) {
    float ex = e.getX();
    float ey = e.getY();
    if(abs((x * x + y * y) - (ex * ex + ey * ey)) < 100)
      r = 30;
  }
  
  void display() {
    pushMatrix();
    translate(x, y, z);
    noStroke();
    sphere(r);
    stroke(255);
    strokeWeight(10);
    popMatrix();
    
    for(Bond b : bonds) {
      if(b == null)
        break;
      
      b.display();
    }
  }
  boolean addBond(Bond b) {
    if(bonded == bonds.length)
      return false;
    
    bonds[bonded++] = b;
    
    return true;
  }
}


Atom[] atoms;
Bond[] bonds;
float tX, tY;

void setup() {
  size(1000, 600, P3D);
  
  atoms = new Atom[5];
  bonds = new Bond[5];
  
  makePentagon(100);
  
  tX = width / 2;
  tY = height / 2;
}

void draw() {
  background(0);
  
  fill(255, 255, 255);
  lights();
  noStroke();
  
  translate(width / 2, height / 2, 0);
  drawPentagon();
}

void mouseClicked() {
  //very very crude 3d picking algorithm:
  int i=0;
  for(Atom a : atoms) {
    float dx = (mouseX - tX) - a.x;
    float dy = (mouseY - tY) - a.y;
    if(dx * dx + dy * dy < 100) {
      a.r = 30;
    }
  }
}


void makePentagon(float R) {
  float theta = 0;
  float dTheta = TWO_PI / 5;
  for(int i=0; i < atoms.length; ++i) {
    atoms[i] = new Atom(R * cos(theta), R * sin(theta), 0);
    theta += dTheta;
  }
  
  for(int i=0; i < bonds.length; ++i) {
    bonds[i] = new Bond(atoms[i], atoms[(i + 1) % atoms.length]);
  }
}

void drawPentagon() {
  for(Atom a : atoms) {
    a.display();
  }
  for(Bond b : bonds) {
    b.display();
  }
}

void cylinder(float h, float r) {
  int sides = int(r * 3);//some experimentally derived factor of r
  float theta = 0;
  float dTheta = TWO_PI / sides;
  
  beginShape(QUAD_STRIP);
  for(int i=0; i < sides + 1; ++i) {
    vertex(r * cos(theta), 0, r * sin(theta));
    vertex(r * cos(theta), h, r * sin(theta));
    theta += dTheta;
  }
  endShape();
  
  beginShape(TRIANGLE_FAN);
  
}

