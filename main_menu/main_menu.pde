class Main_Menu
{
  
  
  
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
 
void setup()
{
  size(1000,600);
  background(#00B9C6);
}

void draw()
{
  float w = 100.0, h = 50.0, port1_x = 1.0, port1_y = 1.0, port2_x = 1.0, port2_y = 1.0, port3_x = 1.0,
  port3_y = 1.0, port4_x = 1.0, port4_y = 1.0, port5_x = 1.0, port5_y = 1.0, quit_x = 850.0, quit_y = 500.0, 
  Hotseat_x = 100.0, Hotseat_y = 100.0;
    
  img title = new img("Game_name.png", 350.0, 0.0);
  img legend = new img("Legend.png", 350.0, 500.0);
  img instr = new img("instructions.png", 500.0, 100.0);
  img credits = new img("Credits.png", 520.0, 580.0);
  Button Hotseat = new Button("Hotseat.png","Hotseat_roll.png","Hotseat_click.png", 100.0, 100.0);
  Button quit = new Button("Quit.png", "Quit_roll.png", "Quit_click.png", 850.0, 500.0);

  if(mousePressed && mouseX>quit_x && mouseX <quit_x+w && mouseY>quit_y && mouseY <quit_y+h)
    exit();
    
  if(mousePressed && mouseX>port1_x && mouseX <port1_x+w && mouseY>port1_y && mouseY <port1_y+h)
  
  
  if(mousePressed && mouseX>port2_x && mouseX <port2_x+w && mouseY>port2_y && mouseY <port2_y+h)
  
  
  if(mousePressed && mouseX>port3_x && mouseX <port3_x+w && mouseY>port3_y && mouseY <port3_y+h)
  
  
  if(mousePressed && mouseX>port4_x && mouseX <port4_x+w && mouseY>port4_y && mouseY <port4_y+h)
  
  
  if(mousePressed && mouseX>port5_x && mouseX <port5_x+w && mouseY>port5_y && mouseY <port5_y+h)
 {}
}



}
