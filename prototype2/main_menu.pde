
class Main_Menu
{
  private boolean playing;
  private PApplet papplet;
  
  Main_Menu(PApplet papplet_)
  {
    papplet = papplet_;
    playing = false;
  }
  
  void setPlaying(boolean playing_) 
  {
    playing = playing_;
  }
  
  
  class Button
  {
    Button(String base, String roll, String click, float x, float y)
    {
      float w = 100.0, h = 50.0;    
      PImage baseI, rollI, clickI;
      baseI = loadImage(base);
      rollI = loadImage(roll);
      clickI = loadImage(click);
      image(baseI, x, y);
    
      if(mousePressed && mouseX>x && mouseX <x+w && mouseY>y && mouseY <y+h)
        image(clickI, x, y); 
        
      if(!mousePressed && mouseX>x && mouseX <x+w && mouseY>y && mouseY <y+h)
        image(rollI, x, y);
    }
  }
 
  class img
  {  
     img(String base, float x, float y)
     {
       PImage img = loadImage(base);
       image(img, x, y);
     }
  }
  
  ArrayList<Integer> totals = new ArrayList<Integer>();
  
  void setup()
  {
    ArrayList<String> stringTotals = mc.roomTotals();
    for(String st : stringTotals) {
      totals.add(Integer.parseInt(st));
    }
  }
  
  void keyPressed() {
    if(!playing && key == 'r') {
      ArrayList<String> stringTotals = mc.roomTotals();
      for(int i=0; i < stringTotals.size(); ++i) {
        totals.set(i, Integer.parseInt(stringTotals.get(i)));
      }
    }
  }
  
  Multicaster mc = new Multicaster(papplet);
  
  GameInitializerStruct draw()
  {
    background(#00B9C6);
    
    float w = 100.0, h = 50.0, port1_x = 100.0, port1_y = 200.0, port2_x = 100.0, port2_y = 260.0, port3_x = 100.0,
    port3_y = 320.0, port4_x = 100.0, port4_y = 380.0, port5_x = 100.0, port5_y = 440.0, quit_x = 850.0, quit_y = 500.0, 
    Hotseat_x = 100.0, Hotseat_y = 100.0;
      
    img title = new img("Game_name.png", 350.0, 20.0);
    img legend = new img("Legend.png", 230.0, 300.0);
    img instr = new img("instructions.png", 500.0, 100.0);
    img credits = new img("Credits.png", 520.0, 580.0);
    Button Hotseat = new Button("Hotseat.png","Hotseat_roll.png","Hotseat_click.png", Hotseat_x, Hotseat_y);
    Button quit = new Button("Quit.png", "Quit_roll.png", "Quit_click.png", quit_x, quit_y);
    
    if(totals.get(0) == 0) {
      Button port1_empty = new Button("LEGO_empty.png", "LEGO_empty_roll.png", "LEGO_empty_click.png", port1_x, port1_y);
    } else if(totals.get(0) == 1) {
      Button port1_open = new Button("LEGO_open.png", "LEGO_open_roll.png", "LEGO_open_click.png", port1_x, port1_y);
    } else {
      
    }
    if(totals.get(1) == 0) {
      Button port2_empty = new Button("O_empty.png", "O_empty_roll.png", "O_empty_click.png", port2_x, port2_y);
    } else if(totals.get(1) == 1) {
      Button port2_open = new Button("O_open.png", "O_open_roll.png", "O_open_click.png", port2_x, port2_y);
    } else {
      
    }
    if(totals.get(2) == 0) {
      Button port3_empty = new Button("sigma_empty.png", "sigma_empty_roll.png", "sigma_empty_click.png", port3_x, port3_y);
    } else if(totals.get(2) == 1) {
      Button port3_open = new Button("sigma_open.png", "sigma_open_roll.png", "sigma_open_click.png", port3_x, port3_y);
    } else {
      
    }
    if(totals.get(3) == 0) {
      Button port4_empty = new Button("smile_empty.png", "smile_empty_roll.png", "smile_empty_click.png", port4_x, port4_y);
    } else if(totals.get(3) == 1) {
      Button port4_open = new Button("smile_open.png", "smile_open_roll.png", "smile_open_click.png", port4_x, port4_y);
    } else {
      
    }
    if(totals.get(4) == 0) {
      Button port5_empty = new Button("TARDIS_empty.png", "TARDIS_empty_roll.png", "TARDIS_empty_click.png", port5_x, port5_y);
    } else if(totals.get(4) == 1) {
      Button port5_open = new Button("TARDIS_open.png", "TARDIS_open_roll.png", "TARDIS_open_click.png", port5_x, port5_y);
    } else {
      
    }
    
    
    GameInitializerStruct gis = null;
  
    if(!playing && mousePressed && mouseX>quit_x && mouseX <quit_x+w && mouseY>quit_y && mouseY <quit_y+h) 
    {
      exit();
    }
    else if(!playing && mousePressed && mouseX>Hotseat_x && mouseX <Hotseat_x+w && mouseY>Hotseat_y && mouseY <Hotseat_y+h)
    {
      gis = new GameInitializerStruct(true, true, "");
      playing = true;
    }
    else if(!playing && totals.get(0) != 2 && mousePressed && mouseX>port1_x && mouseX <port1_x+w && mouseY>port1_y && mouseY <port1_y+h)
    {
      gis = new GameInitializerStruct(true, false, "Lego");
      playing = true;
    }
    else if(!playing && totals.get(1) != 2 && mousePressed && mouseX>port2_x && mouseX <port2_x+w && mouseY>port2_y && mouseY <port2_y+h)
    {
      gis = new GameInitializerStruct(true, false, "Port Omega");
      playing = true;
    }
    else if(!playing && totals.get(2) != 2 && mousePressed && mouseX>port3_x && mouseX <port3_x+w && mouseY>port3_y && mouseY <port3_y+h)
    {
      gis = new GameInitializerStruct(true, false, "Port Sigma");
      playing = true;
    }
    else if(!playing && totals.get(3) != 2 && mousePressed && mouseX>port4_x && mouseX <port4_x+w && mouseY>port4_y && mouseY <port4_y+h)
    {
      gis = new GameInitializerStruct(true, false, "Port Smile");
      playing = true;
    }
    else if(!playing && totals.get(4) != 2 && mousePressed && mouseX>port5_x && mouseX <port5_x+w && mouseY>port5_y && mouseY <port5_y+h)
    {
      gis = new GameInitializerStruct(true, false, "Tardis");
      playing = true;
    } else {
      gis = new GameInitializerStruct(false, true, "");
    }
    
    return gis;
  }
  
  void stop() {
    
  }
}

