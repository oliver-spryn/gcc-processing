
Game g;
Main_Menu m;
boolean playing = true;
GameInitializerStruct gis;

public class GameInitializerStruct {
  public boolean playing;
  public boolean hotseat;
  public String room;
  
  public GameInitializerStruct(boolean playing_, boolean hotseat_, String room_) {
    playing = playing_;
    hotseat = hotseat_;
    room = room_;
  }
}

void setup() {
  size(1000, 600, P3D);
  //m = new Main_Menu();
  //m.setup();
  g = new Game(this, false, "Port Sigma");
  g.setup();
}

void draw() {
  if(playing) {
    playing = g.draw();
    
    if(!playing) {
      try { Thread.sleep(3000); } catch(InterruptedException e) {}
      g = null;
    }
  } else {
    gis = m.draw();
    playing = gis.playing;
    
    if(playing) {
      g = new Game(this, gis.hotseat, gis.room);
      g.setup();
    }
  }
}

void keyPressed() {
  if(playing && key == ' ') {
    g.stop();
    exit();
  }
}

void mouseClicked() {
  if(playing)
    g.mouseClicked();
}

void mousePressed() {
  if(playing)
    g.mousePressed();
}

void mouseDragged() {
  if(playing)
    g.mouseDragged();
}

