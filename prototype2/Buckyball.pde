//package buckyball;

final float phi = 1.61803398874989;

PVector[] vertices = {
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

