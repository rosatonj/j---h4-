package pass;

public class ForLoopTest{

        public static void main(String [] args){
            for (int i =20 ; i>0 ; i--){
                System.out.println("test that foor loop baby!");
            }

            String [] names = {"Bob","Nick", "James"};

            for (String n: names){
                System.out.println(n);
            }
        }

}