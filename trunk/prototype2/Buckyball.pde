

public static class Buckyball {
  
  //phi from http://www.goldennumber.net/phi20000.htm
  public static final float phi = 1.6180339887498948482045868343656381177203091798057628621354486227052;
  
  //vertices from http://www.goldennumber.net/buckyball.htm
  public static final PVector[] vertices = {
    new PVector(0,1,3*phi),
    new PVector(0,1,-3*phi),
    new PVector(0,-1,3*phi),
    new PVector(0,-1,-3*phi),
    
    new PVector(1,3*phi,0),
    new PVector(1,-3*phi,0),
    new PVector(-1,3*phi,0),
    new PVector(-1,-3*phi,0),
    
    new PVector(3*phi,0,1),
    new PVector(3*phi,0,-1),
    new PVector(-3*phi,0,1),
    new PVector(-3*phi,0,-1),
    
    new PVector(2,(1+2*phi),phi),
    new PVector(2,(1+2*phi),-phi),
    new PVector(2,-(1+2*phi),phi),
    new PVector(2,-(1+2*phi),-phi),
    new PVector(-2,(1+2*phi),phi),
    new PVector(-2,(1+2*phi),-phi),
    new PVector(-2,-(1+2*phi),phi),
    new PVector(-2,-(1+2*phi),-phi),
    
    new PVector((1+2*phi),phi,2),
    new PVector((1+2*phi),phi,-2),
    new PVector((1+2*phi),-phi,2),
    new PVector((1+2*phi),-phi,-2),
    new PVector(-(1+2*phi),phi,2),
    new PVector(-(1+2*phi),phi,-2),
    new PVector(-(1+2*phi),-phi,2),
    new PVector(-(1+2*phi),-phi,-2),
    
    new PVector(phi,2,(1+2*phi)),
    new PVector(phi,2,-(1+2*phi)),
    new PVector(phi,-2,(1+2*phi)),
    new PVector(phi,-2,-(1+2*phi)),
    new PVector(-phi,2,(1+2*phi)),
    new PVector(-phi,2,-(1+2*phi)),
    new PVector(-phi,-2,(1+2*phi)),
    new PVector(-phi,-2,-(1+2*phi)),
    
    new PVector(1,(2+phi),2*phi),
    new PVector(1,(2+phi),-2*phi),
    new PVector(1,-(2+phi),2*phi),
    new PVector(1,-(2+phi),-2*phi),
    new PVector(-1,(2+phi),2*phi),
    new PVector(-1,(2+phi),-2*phi),
    new PVector(-1,-(2+phi),2*phi),
    new PVector(-1,-(2+phi),-2*phi),
    
    new PVector((2+phi),2*phi,1),
    new PVector((2+phi),2*phi,-1),
    new PVector((2+phi),-2*phi,1),
    new PVector((2+phi),-2*phi,-1),
    new PVector(-(2+phi),2*phi,1),
    new PVector(-(2+phi),2*phi,-1),
    new PVector(-(2+phi),-2*phi,1),
    new PVector(-(2+phi),-2*phi,-1),
    
    new PVector(2*phi,1,(2+phi)),
    new PVector(2*phi,1,-(2+phi)),
    new PVector(2*phi,-1,(2+phi)),
    new PVector(2*phi,-1,-(2+phi)),
    new PVector(-2*phi,1,(2+phi)),
    new PVector(-2*phi,1,-(2+phi)),
    new PVector(-2*phi,-1,(2+phi)),
    new PVector(-2*phi,-1,-(2+phi))
  };
  
  //calculated by my code, see below
  public static final int[][] bonds = {
    { 2, 28, 32 },
    { 3, 29, 33 },
    { 0, 30, 34 },
    { 1, 31, 35 },
    { 6, 12, 13 },
    
    { 7, 14, 15 },
    { 4, 16, 17 },
    { 5, 18, 19 },
    { 9, 20, 22 },
    { 8, 21, 23 },
    
    { 11, 24, 26 },
    { 10, 25, 27 },
    { 4, 36, 44 },
    { 4, 37, 45 },
    { 5, 38, 46 },
    
    { 5, 39, 47 },
    { 6, 40, 48 },
    { 6, 41, 49 },
    { 7, 42, 50 },
    { 7, 43, 51 },
    
    { 8, 44, 52 },
    { 9, 45, 53 },
    { 8, 46, 54 },
    { 9, 47, 55 },
    { 10, 48, 56 },
    
    { 11, 49, 57 },
    { 10, 50, 58 },
    { 11, 51, 59 },
    { 0, 36, 52 },
    { 1, 37, 53 },
    
    { 2, 38, 54 },
    { 3, 39, 55 },
    { 0, 40, 56 },
    { 1, 41, 57 },
    { 2, 42, 58 },
    
    { 3, 43, 59 },
    { 12, 28, 40 },
    { 13, 29, 41 },
    { 14, 30, 42 },
    { 15, 31, 43 },
    
    { 16, 32, 36 },
    { 17, 33, 37 },
    { 18, 34, 38 },
    { 19, 35, 39 },
    { 12, 20, 45 },
    
    { 13, 21, 44 },
    { 14, 22, 47 },
    { 15, 23, 46 },
    { 16, 24, 49 },
    { 17, 25, 48 },
    
    { 18, 26, 51 },
    { 19, 27, 50 },
    { 20, 28, 54 },
    { 21, 29, 55 },
    { 22, 30, 52 },
    
    { 23, 31, 53 },
    { 24, 32, 58 },
    { 25, 33, 59 },
    { 26, 34, 56 },
    { 27, 35, 57 }
  };
  
  //code to calculate bonds (put in Space.buildBuckyball(), where R = 100):
  /*
  for(int i=0; i < atoms.size(); ++i) {
        print("{ ");
        int counter = 0;
        for(int j=0; j < atoms.size(); ++j) {
          if(i != j) {
            if(round(atoms.get(i).getPosition().dist(atoms.get(j).getPosition())) == 200) {
              ++counter;
              print(j);
              if(counter == 3)
                break;
              print(", ");
            }
          }
        }
        println(" },");
      }
  */
}

