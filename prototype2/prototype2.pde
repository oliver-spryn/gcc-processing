import picking.*;
import processing.opengl.*;

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
    //TODO: make this a cylinder
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
    //TODO: implement
    
    //dummy code
    float R = 100;
    float theta = 0;
    float dTheta = TWO_PI / 5;
    for(int i=0; i < 5; ++i) {
      atoms.add(new Atom(this, 2, 0, 
          new PVector(R * cos(theta), R * sin(theta), 0)));
      theta += dTheta;
    }
    
    for(int i=0; i < atoms.size(); ++i) {
      bond(atoms.get(i), atoms.get((i + 1) % atoms.size()));
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
  //inefficient, but simple. This can be optimized if necessary
  public void unbond(Atom a, Atom b) {
    a.unbondTo(b);
    b.unbondTo(a);
    
    for(int i=0; i < bonds.size(); ++i) {
      Atom atoms[] = bonds.get(i).getParticipants();
      if((atoms[0] == a && atoms[1] == b) || (atoms[0] == b && atoms[1] == a)) {
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
    
    //TODO: find geometric center and point camera at it
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


Space space;
Picker picker;
Player[] players;
int turn;

int showMe = -99999;

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
  
  translate(width / 2, height / 2);
  
  space.simulate();
  space.draw();
  
  showMe = turn;
  text(showMe, 15, 30, 0);
}

void mouseClicked() {
  int id = picker.get(mouseX, mouseY);
  
  //FIXME: a time delay should be enforced in between turns
  
  if(id != -1 && players[turn] instanceof TerminalPlayer) {
    TerminalPlayer t = (TerminalPlayer)players[turn];
    t.setMove(id);
  }
}
