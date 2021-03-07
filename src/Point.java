final class Point
{
   private final int x;
   private final int y;
   private int gvalue;
   private int hvalue;
   private int fvalue;
   private Point prior;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public int getX(){
      return this.x;
   }
   public int getY(){
      return this.y;
   }

   public int getGvalue(){
      return gvalue;
   }
   public int getHvalue() {
      return hvalue;
   }
   public int getFvalue(){
      return fvalue;
   }
   public Point getPrior() {
      return prior;
   }

   public void setGvalue(int gvalue) {
      this.gvalue = gvalue;
   }
   public void setHvalue(int hvalue) {
      this.hvalue = hvalue;
   }
   public void setFvalue(int fvalue) {
      this.fvalue = fvalue;
   }
   public void setPrior(Point p){
      this.prior = p;
   }
}
