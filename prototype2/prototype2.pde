import picking.*;
import processing.opengl.*;

//TODO: put all of this in a PApplet, or otherwise abstract it from a GUI perspective
// when this happens, code should be moved out of `prototype2' into its final location


Space space;
Picker picker;
Player[] players;
int turn;

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
  int idx = players[turn].getMove();
  if(idx != -1) {
    space.remove(idx);
    turn = (turn + 1) % players.length;
  }
  
  lights();
  
  translate(width / 2, height / 2, -600);
  
  space.simulate();
  space.draw();
}

void mouseClicked() {
  int id = picker.get(mouseX, mouseY);
  
  //FIXME: a time delay should be enforced in between turns
  
  if(id != -1 && players[turn] instanceof TerminalPlayer) {
    TerminalPlayer t = (TerminalPlayer)players[turn];
    t.setMove(id);
  }
}



public class Bond {
  private Atom[] atoms = new Atom[2];
  
  public Bond(Atom a, Atom b) {
    atoms[0] = a;
    atoms[1] = b;
  }
  
  public Atom[] getParticipants() {
    return atoms;
  }
  
  public void draw() {
    stroke(255);
    strokeWeight(5);
    PVector a = atoms[0].getPosition();
    PVector b = atoms[1].getPosition();
    //TODO: make this a cylinder (complicated, but contained)
    line(a.x, a.y, a.z, b.x, b.y, b.z);
  }
}

public class Atom {
  private PVector p, v, a;
  private float strength;
  private float r = 20;
  private Space space;
  
  private ArrayList<Atom> bondedTo;
  private int maxBonds;
  
  public Atom(Space space, int maxBonds, float strength, PVector position) {
    this.space = space;
    this.bondedTo = new ArrayList<Atom>(maxBonds);
    this.maxBonds = maxBonds;
    this.strength = strength;
    this.p = position;
  }
  
  public PVector getPosition() {
    return p;
  }
  
  public boolean bondTo(Atom a) {
    if(bondedTo.size() == maxBonds)
      return false;
    
    bondedTo.add(a);
    return true;
  }
  public boolean unbondTo(Atom a) {
    // there will be very few bonds per atom, so this will be efficient
    for(int i=0; i < bondedTo.size(); ++i) {
      if(bondedTo.get(i) == a) {
        bondedTo.remove(i);
        return true;
      }
    }
    
    return false;
  }
  
  public void deactivate() {
    for(int i=bondedTo.size()-1; i >= 0; --i) {
      space.unbond(this, bondedTo.get(i));
    }
  }
  
  public void step(ArrayList<Atom> atoms) {
    for(Atom a : atoms) {
      if(a == this)
        continue;
      
      
    }
  }
  
  public void react() {
    
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
  
  public Space(Picker picker) {
    this.picker = picker;
    atoms = new ArrayList<Atom>();
    bonds = new ArrayList<Bond>();
  }
  
  public void buildBuckyball() {
    //clear(); // ?
    
    final float R = 100;
    
    for(PVector v : Buckyball.vertices) {
      PVector w = v.get();
      w.mult(R);
      atoms.add(new Atom(this, 3, 4, w));
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
  
  public void remove(int idx) {
    atoms.get(idx).deactivate();
    atoms.remove(idx);
  }
  
  public void clear() {
    atoms.clear();
    bonds.clear();
  }
  
  public void bond(Atom a, Atom b) {
    a.bondTo(b);
    b.bondTo(a);
    bonds.add(new Bond(a, b));
  }
  public void unbond(Atom a, Atom b) {
    a.unbondTo(b);
    b.unbondTo(a);
    
    //inefficient, but simple. It can be optimized if necessary
    for(int i=bonds.size()-1; i >= 0; --i) {
      Atom participants[] = bonds.get(i).getParticipants();
      if((participants[0] == a && participants[1] == b) || (participants[0] == b && participants[1] == a)) {
        bonds.remove(i);
        break;
      }
    }
  }
  
  public void simulate() {
    for(Atom a : atoms) {
      a.step(atoms);
    }
    for(Atom a : atoms) {
      a.react();
    }
    
    //TODO: find geometric center and point camera at it (via translate(), not camera())
    //TODO: also adjust zoom so that all atoms are on screen (?)
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

