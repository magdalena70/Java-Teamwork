package breakout;

public class RankingRows implements Comparable<RankingRows> {
    public final String theString;
    public final long long1;
    public final long long2;

    public RankingRows(String theString, long long1, long long2) {
        this.theString = theString;
        this.long1 = long1;
        this.long2 = long2;
   }

   public int compareTo(RankingRows other) {
       if(this.long1 == other.long1) {
           return new Long (this.long2).compareTo(other.long2);
       }

       return new Long(other.long1).compareTo(this.long1);
   }
   public String getUserName() {
	   return theString;
   }
   public long getPoints() {
	   return long1;
   }
   public long getTime() {
	   return long2;
   }
}