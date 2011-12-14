
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
boolean isSetup = false;

void draw() {
  if(willClose) {
    try { Thread.sleep(5000); } catch(InterruptedException e) {}
    exit();
  }
  
  if(playing) {
    playing = g.draw();
    
    if(!playing) {
      TabAlerts tab = new TabAlerts(this);
      tab.message = "Game Over. Window will close.";
      tab.build();
      
      willClose = true;
      
      g = null;
      isSetup = false;
      m.setPlaying(playing);
    }
  } else {
    if(!isSetup) {
      gis = m.draw();
    }
    
    if(gis != null) {
      if(playing != gis.playing) {
        if(!isSetup) {
          g = new Game(this, gis.hotseat, gis.room);
          g.setup();
          isSetup = true;
        }
        
        if(!gis.hotseat) {
          if(mc.roomTotal(gis.room) != 2) {
            try { Thread.sleep(200); } catch(InterruptedException ex) {}
            return;
          }
        }
        
        playing = gis.playing;
      }
    }
  }
}

void keyPressed() {
  m.keyPressed();
  if(key == ' ') {
    if(isSetup)
      g.stop();
    m.stop();
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

