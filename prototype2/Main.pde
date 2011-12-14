
import edu.gcc.processing.gui.*;

Game g;
Main_Menu m;
boolean playing = false;
GameInitializerStruct gis;
boolean willClose = false;

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
  m = new Main_Menu(this);
  m.setup();
  //g = new Game(this, false, "Port Sigma");
  //g.setup();
}

Multicaster mc = new Multicaster(this);
String waitOnRoom = null;
boolean needsWait = false;

void draw() {
  if(willClose) {
    try { Thread.sleep(5000); } catch(InterruptedException e) {}
    exit();
  }
  
  if(playing) {
    playing = g.draw();
    
    if(needsWait && waitOnRoom != null) {
      while(mc.roomTotal(waitOnRoom) != 2) {
        try { Thread.sleep(200); } catch(InterruptedException e) {}
      }
    }
    needsWait = true;
    
    if(!playing) {
      TabAlerts tab = new TabAlerts(this);
      tab.message = "Game Over. Window will close.";
      tab.build();
      
      willClose = true;
      
      g = null;
      waitOnRoom = null;
      needsWait = false;
      m.setPlaying(playing);
    }
  } else {
    gis = m.draw();
    if(gis != null) {
      playing = gis.playing;
      
      if(playing) {
        g = new Game(this, gis.hotseat, gis.room);
        g.setup();
        waitOnRoom = gis.room;
      }
    }
  }
}

void keyPressed() {
  m.keyPressed();
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

