import picking.*;
import processing.opengl.*;

//TODO: put all of this in a PApplet, or otherwise abstract it from a GUI perspective
// when this happens, code should be moved out of `prototype2' into its final location


Space space;
Picker picker;
Player[] players;
int turn;
int startX = 0, startY = 0;
int x2D = 0, y2D = 0;
int oldX2D = 0, oldY2D = 0;
float cameraX, cameraY;
float cameraR;
int oldTime = 0, dt;

//for debugging:
/*
int counter = 0;
void push_matrix() {
  ++counter;
  println(counter);
  pushMatrix();
}
void pop_matrix() {
  --counter;
  popMatrix();
}
*/

void setup() {
  size(1000, 600, P3D);
  
  cameraX = 0;
  cameraY = 0;
  //int cameraZ = round((height / 2.0) / tan(PI / 6.0));
  cameraR = 1200; //sqrt(cameraX * cameraX + cameraY * cameraY + cameraZ * cameraZ);
  
  picker = new Picker(this);
  space = new Space(picker);
  players = new Player[2];
  //for now; make this user-customizable via GUI later
  for(int i=0; i < players.length; ++i) {
    players[i] = new TerminalPlayer();
  }
  turn = 0;
  
  space.buildBuckyball();
}

void draw() {
  dt = millis() - oldTime;
  
  int idx = players[turn].getMove();
  if(idx != -1) {
    space.remove(idx);
    turn = (turn + 1) % players.length;
  }
  
  //FIXME: put lights in constant position relative to the camera
  lights();
  translate(width / 2, height / 2, -600);
  
  float theta = (oldX2D + x2D) * 8 / cameraR; //approximation to account for scaling that looks reasonable
  float phi = (oldY2D + y2D) * 8 / cameraR;
  
  cameraX = cameraR * sin(theta);
  cameraY = cameraR * sin(phi);
  float cameraZ = cameraR * cos(theta) * cos(phi);
  float tmp = cameraR * cameraR - cameraX * cameraX - cameraY * cameraY;
  int sign = tmp < 0 ? -1 : 1;
  //float cameraZ = sqrt(abs(tmp)) * sign;
  //println("y: " + cameraY + " z: " + cameraZ);
  if(cameraZ < 0) {
    cameraY = -cameraY;
    cameraZ = -cameraZ;
  }
  if(cameraX < 0) {
    cameraY = -cameraY;
    cameraX = -cameraX;
  }
  println(cameraZ);
  camera(cameraX, cameraY, cameraZ, 0, 0, 0, 0, 1, 0);
  //rotateX(rotX * PI / 180);
  //rotateY(rotY * PI / 180);
  
  /*
  float theta = x2D * 8 / cameraR; //approximation to account for scaling that looks reasonable
  float phi = y2D * 8 / cameraR;
  float oldTheta = oldX2D * 8 / cameraR;
  float oldPhi = oldY2D * 8 / cameraR;
  
  //XXX: can we do this with custom rotation matrices?
  rotateY(oldTheta);
  rotateX(oldPhi);
  PMatrix m;
  pushMatrix();
  resetMatrix();
  rotateY(theta);
  rotateX(phi);
  m = getMatrix();
  popMatrix();
  applyMatrix(m);
  */
  
  space.simulate();
  space.draw();
  
  oldTime = millis();
}

void mouseClicked() {
  int id = picker.get(mouseX, mouseY);
  
  //FIXME: a time delay should be enforced in between turns
  // better yet, wait until everything has stopped moving
  
  if(id != -1 && players[turn] instanceof TerminalPlayer) {
    TerminalPlayer t = (TerminalPlayer)players[turn];
    t.setMove(id);
  }
}

void mousePressed() {
  startX = mouseX;
  startY = mouseY;
  oldX2D += x2D;
  oldY2D += y2D;
  x2D = y2D = 0;
}

void mouseDragged() {
  x2D = -(startX - mouseX);
  y2D = startY - mouseY;
}



public class Bond {
  private Atom[] atoms = new Atom[2];
  private float strength;
  
  public Bond(Atom a, Atom b) {
    atoms[0] = a;
    atoms[1] = b;
    strength = 0.1;
  }
  
  public Atom[] getParticipants() {
    return atoms;
  }
  
  public void draw() {
    stroke(255);
    strokeWeight(5);
    PVector a = atoms[0].p;
    PVector b = atoms[1].p;
    //TODO: make this a cylinder (complicated, but contained)
    line(a.x, a.y, a.z, b.x, b.y, b.z);
  }
  
  public float getStrength() {
    return strength;
  }
  
  public boolean equals(Bond bond) {
    return (atoms[0] == bond.atoms[0] && atoms[1] == bond.atoms[1]) || 
        (atoms[0] == bond.atoms[1] && atoms[1] == bond.atoms[0]);
  }
}

public class Atom {
  public PVector p, v, a;
  private float strength;
  private float r = 20;
  private Space space;
  private boolean active;
  
  private class AtomBond {
    public Atom atom;
    public Bond bond;
    
    public AtomBond(Atom a, Bond b) {
      atom = a;
      bond = b;
    }
  }
  private ArrayList<AtomBond> bondedTo;
  private int maxBonds;
  
  public Atom(Space space, int maxBonds, float strength, PVector position) {
    this.space = space;
    this.bondedTo = new ArrayList<AtomBond>(maxBonds);
    this.maxBonds = maxBonds;
    this.strength = strength;
    this.p = position;
    this.v = new PVector();
    this.a = new PVector();
    this.active = true;
  }
  
  public int bondsNeeded() {
    return maxBonds - bondedTo.size();
  }
  
  public boolean bondTo(Atom a, Bond b) {
    if(bondedTo.size() == maxBonds)
      return false;
    
    bondedTo.add(new AtomBond(a, b));
    return true;
  }
  public boolean unbondTo(Atom a) {
    // there will be very few bonds per atom, so this will be efficient
    for(int i=0; i < bondedTo.size(); ++i) {
      if(bondedTo.get(i).atom == a) {
        bondedTo.remove(i);
        return true;
      }
    }
    
    return false;
  }
  
  public void deactivate() {
    for(int i=bondedTo.size()-1; i >= 0; --i) {
      space.unbond(this, bondedTo.get(i).atom);
    }
    active = false;
  }
  public boolean isActive() {
    return active;
  }
  
  public void step(ArrayList<Atom> atoms) {
    //Try to equalize the length of each of its bonds by applying a velocity to itself
    //Calculate geometric center of atoms it is bonded to and move in that direction
    /*if(bondedTo.size() != 0) {
      PVector c = new PVector();
      for(AtomBond ab : bondedTo) {
        c.add(ab.atom.p);
      }
      c.mult(1 / bondedTo.size());
      
      c.sub(p);
      
      a = c;
    }*/
    /*
    for(Atom a : atoms) {
      if(a == this)
        continue;
      
      PVector acc = a.p.get();
      acc.sub(this.p);
      float d2 = acc.x * acc.x + acc.y * acc.y + acc.z * acc.z;
      acc.mult(strength / d2);
      
      a.a.add(acc);
    }*/
    /*
    for(AtomBond ab : bondedTo) {
      PVector acc = this.p.get();
      acc.sub(ab.atom.p);
      float d2 = acc.x * acc.x + acc.y * acc.y + acc.z * acc.z;
      acc.normalize();
      acc.mult(ab.bond.getStrength() / d2);
      
      ab.atom.a.add(acc);
    }*/
  }
  
  public void exertBonds() {
    if(bondedTo.size() > 0) {
      /*
      PVector c = new PVector();
      for(AtomBond ab : bondedTo) {
        c.add(ab.atom.p);
      }
      
      c.mult(strength / bondedTo.size());
      c.sub(p);
      
      v = c;
      */
      
      v.set(0,0,0);
      for(AtomBond ab : bondedTo) {
        PVector c = ab.atom.p.get();
        c.sub(p);
        c.mult(ab.bond.getStrength());
        v.add(c);
      }
    }
  }
  
  public void react() {
    PVector tmp = a.get();
    tmp.mult(dt);
    v.add(tmp);
    
    tmp = v.get();
    tmp.mult(dt);
    p.add(v);
  }
  
  public void draw() {
    pushMatrix();
    translate(p.x, p.y, p.z);
    noStroke();
    sphere(r);
    popMatrix();
  }
}

public class Space {
  private ArrayList<Atom> atoms;
  private ArrayList<Bond> bonds;
  private Picker picker;
  private boolean changed;
  private ArrayList<Atom> needy;
  
  public Space(Picker picker) {
    this.picker = picker;
    atoms = new ArrayList<Atom>();
    bonds = new ArrayList<Bond>();
    needy = new ArrayList<Atom>();
  }
  
  public void buildBuckyball() {
    //clear(); // ?
    
    final float R = 100;
    
    for(PVector v : Buckyball.vertices) {
      PVector w = v.get();
      w.mult(R);
      atoms.add(new Atom(this, 3, 1, w));
    }
    
    for(int i=0; i < atoms.size(); ++i) {
      Atom a = atoms.get(i);
      for(int j=0; j < Buckyball.bonds[i].length; ++j) {
        int b = Buckyball.bonds[i][j];
        if(b > i)
          bond(a, atoms.get(b));
      }
    }
  }
  
  public void remove(int id) {
    atoms.get(id).deactivate();
    atoms.remove(id);
    changed = true;
  }
  
  public void clear() {
    atoms.clear();
    bonds.clear();
  }
  
  public boolean bond(Atom a, Atom b) {
    if(a == b)
      throw new RuntimeException();
    
    if(a.bondsNeeded() == 0 || b.bondsNeeded() == 0)
      return false;
    
    Bond bond = new Bond(a, b);
    
    for(Bond i : bonds) {
      if(bond.equals(i))
        return false;
    }
    
    a.bondTo(b, bond);
    b.bondTo(a, bond);
    bonds.add(bond);
    
    return true;
  }
  public void unbond(Atom a, Atom b) {
    a.unbondTo(b);
    b.unbondTo(a);
    
    //inefficient, but simple. It can be optimized if necessary
    Bond bond = new Bond(a, b);
    for(int i=bonds.size()-1; i >= 0; --i) {
      if(bonds.get(i).equals(bond)) {
        bonds.remove(i);
        break;
      }
    }
  }
  
  public void simulate() {
    for(Atom a : atoms) {
      a.step(atoms);
    }
    for(Atom a : needy) {
      a.exertBonds();
    }
    for(Atom a : atoms) {
      a.react();
    }
    
    
    if(!changed)
      return;
    changed = false;
    
    //needy will always be a short list, so the following operations will be efficient
    for(int i=needy.size()-1; i >= 0; --i) {
      if(!needy.get(i).isActive() || needy.get(i).bondsNeeded() == 0)
        needy.remove(i);
    }
    
    //find all atoms that are missing bonds
    for(Atom a : atoms) {
      if(a.bondsNeeded() > 0) {
        boolean shouldAdd = true;
        for(Atom a2 : needy) {
          if(a2 == a) {
            shouldAdd = false;
            break;
          }
        }
        
        if(shouldAdd)
          needy.add(a);
      }
    }
    Collections.shuffle(needy);
    
    boolean bondingOccured = true;
    while(bondingOccured) {
      bondingOccured = false;
      for(int i=0; i < needy.size(); ++i) {
        for(int j=0; j < needy.size(); ++j) {
          if(i == j)
            continue;
          
          if(bond(needy.get(i), needy.get(j))) {
            bondingOccured = true;
            /*PVector acc = needy.get(j).p.get();
            acc.sub(needy.get(i).p);
            acc.normalize();
            acc.mult(.5);
            needy.get(i).v.add(acc);
            acc.mult(-1);
            needy.get(j).v.add(acc);*/
          }
        }
      }
      
      /*int biggest = 0;
      int biggest2 = 1;
      
      Atom a = needy.get(biggest);
      Atom b = needy.get(biggest2);
      
      if(bond(a, b))
        openings -= 2;
      
      if(a.bondsNeeded() < needy.get((biggest + 1) % needy.size()).bondsNeeded())
        biggest = (biggest + 1) % needy.size();
      if(b.bondsNeeded() < needy.get((biggest2 + 1) % needy.size()).bondsNeeded())
        biggest2 = (biggest2 + 1) % needy.size();*/
    }
  }
  
  public void draw() {
    background(0);
    for(int i=0; i < atoms.size(); ++i) {
      picker.start(i);
      atoms.get(i).draw();
    }
    picker.stop();
    for(Bond b : bonds) {
      b.draw();
    }
  }
}

public interface Player {
  public int getMove();
}

public class TerminalPlayer implements Player {
  private int idx;
  
  public TerminalPlayer() {
    idx = -1;
  }
  
  ///to be called by mouseClicked(), or similar
  public boolean setMove(int idx) {
    //TODO: needs to ensure that packets are sent to all NetworkPlayers
    
    if(this.idx == -1) {
      this.idx = idx;
      return true;
    }
    
    return false;
  }
  
  public int getMove()  {
    int tmp = idx;
    idx = -1;
    return tmp;
  }
}

public class NetworkPlayer implements Player {
  //TODO: build network abstraction
  
  public int getMove() {
    //TODO: check for network packet
    //TODO: return index of atom removed, or -1 if no packet was in the queue
    return -1;
  }
}

