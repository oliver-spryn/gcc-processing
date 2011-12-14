
Game g;
//Main_Menu m;
boolean playing = true;

void setup() {
  size(1000, 600, P3D);
  g = new Game(this, false, "Port Sigma");
  //m = new Main_Menu();
  g.setup();
  //m.setup();
}

void draw() {
  if(playing) {
    playing = g.draw();
    
    if(!playing) {
      try { Thread.sleep(3000); } catch(InterruptedException e) {}
    }
  } else {
    //playing = m.draw();
    background(0);
  }
}

void stop() {
  g.stop();
  //m.stop();
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

